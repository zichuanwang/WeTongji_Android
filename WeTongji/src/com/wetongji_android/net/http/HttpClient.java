/**
 * 
 */
package com.wetongji_android.net.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.util.net.HttpUtil;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author nankonami
 * 
 */
public class HttpClient {
	private static final String TAG = "HttpClient";
	// Constant values
	private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 10 * 1000;
	private static final int UPLOAD_FILE_READ_TIMEOUT = 5 * 15 * 1000;
	private static final String API_DOMAIN = "http://we.tongji.edu.cn/api/call";

	private static final String HTTP_TIMEOUT = "HttpTimeout";
	
	private static final String UPLOAD_FILE_NAME = "upload_avatar.jpg";

	private String strCurrentMethod;

	public HttpRequestResult execute(HttpMethod httpMethod, Bundle params)
			throws WTException {
		switch (httpMethod) {
		case Post:
			return doPost(params);
		case Get:
			return doGet(params);
		}

		return null;
	}

	// Implement custom proxy in case proxy server
	private Proxy getProxy() {
		String strProxyHost = System.getProperty("http:proxyHost");
		String strProxyPort = System.getProperty("http:proxyPort");

		if (!TextUtils.isEmpty(strProxyHost)
				&& !TextUtils.isEmpty(strProxyPort))
			return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					strProxyHost, Integer.valueOf(strProxyPort)));
		else
			return null;
	}

	// Set common http property
	private void setRequestProperty(HttpURLConnection httpURLConnection) {
		httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
		httpURLConnection.setReadTimeout(READ_TIMEOUT);
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
		httpURLConnection
				.setRequestProperty("Accept-Encoding", "gzip, deflate");
	}

	// Implement http get request
	public HttpRequestResult doGet(Bundle params) throws WTException {
		strCurrentMethod = params.getString(ApiHelper.API_ARGS_METHOD);

		try {
			StringBuilder sb = new StringBuilder(API_DOMAIN);
			sb.append("?").append(HttpUtil.encodeUrl(params));
			WTUtility.log("HttpClient", "URL=" + sb.toString());
			Proxy proxy = getProxy();
			URL url = new URL(sb.toString());
			HttpURLConnection urlConnection;

			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);

			setRequestProperty(urlConnection);

			urlConnection.connect();

			return handleResponse(urlConnection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WTException("GET",
					params.getString(ApiHelper.API_ARGS_METHOD), HTTP_TIMEOUT,
					e);
		}
	}

	// Implement http post request
	public HttpRequestResult doPost(Bundle params) throws WTException {
		Bundle body = new Bundle();
		if (params.containsKey("User")) {
			body.putString("User", params.getString("User"));
			params.remove("User");
		} else if (params.containsKey("Image")) {
			body.putString("Image", params.getString("Image"));
			params.remove("Image");
		}

		HttpURLConnection urlConnection = null;
		try {
			StringBuilder sb = new StringBuilder(API_DOMAIN);
			sb.append("?").append(HttpUtil.encodeUrl(params));
			URL url = new URL(sb.toString());
			Proxy proxy = getProxy();

			WTUtility.log("HttpClient", "URL=" + sb.toString());

			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setInstanceFollowRedirects(false);

			setRequestProperty(urlConnection);
			putBodyData(urlConnection, body);

			if (body.containsKey("Image")) {
				//return new HttpRequestResult(200, "");
			}
			return handleResponse(urlConnection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WTException("POST",
					params.getString(ApiHelper.API_ARGS_METHOD), HTTP_TIMEOUT,
					e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}

	private void putBodyData(HttpURLConnection urlConnection, Bundle body)
			throws UnsupportedEncodingException, IOException {
		if (body.containsKey("User")) {
			urlConnection.setRequestProperty("Content-Type",
					"multipart/form-data");
			urlConnection.connect();
			DataOutputStream outStream = new DataOutputStream(
					urlConnection.getOutputStream());
			StringBuilder content = new StringBuilder();
			content.append("User=").append(body.get("User"));
			outStream.write(content.toString().getBytes("UTF-8"));
			outStream.flush();
			outStream.close();
		} else if (body.containsKey("Image")) {

			// set read timeout
			urlConnection.setReadTimeout(UPLOAD_FILE_READ_TIMEOUT);
			//DataInputStream inStream = null;
			// Is this the place are you doing something wrong.

			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";

			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			String filePath = body.getString("Image");
			
			FileInputStream fileInputStream = new FileInputStream(
					new File(filePath));
			urlConnection.setRequestProperty("Content-Type", 
					"multipart/form-data;boundary="+boundary);
			DataOutputStream outStream = new DataOutputStream(
					urlConnection.getOutputStream());
			outStream.writeBytes(twoHyphens + boundary + lineEnd);
			outStream.writeBytes("Content-Disposition: form-data; name=\"Image\"; filename=\""
					+ UPLOAD_FILE_NAME +"\"" + lineEnd);
			outStream.writeBytes("Content-Type: " + "image/*" +  lineEnd);
			outStream.writeBytes(lineEnd);
			
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			
			
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outStream.writeBytes(lineEnd);

			outStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			/*BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
//				tv.append(inputLine);
			}*/

			// close streams
			fileInputStream.close();
			outStream.flush();
			outStream.close();

		}
		
	}

	private HttpRequestResult handleResponse(HttpURLConnection urlConnection)
			throws WTException {
		int iStatus = 200;

		try {
			iStatus = urlConnection.getResponseCode();
			WTUtility.log(TAG, "Response Code: " + iStatus);
		} catch (IOException e) {
			urlConnection.disconnect();
			e.printStackTrace();
			throw new WTException(urlConnection.getRequestMethod(),
					strCurrentMethod, HTTP_TIMEOUT, iStatus, e);
		}

		// The connection is not successful
		if (iStatus != HttpURLConnection.HTTP_OK) {
			return handleError(urlConnection);
		}

		return handleResult(urlConnection);
	}

	private HttpRequestResult handleResult(HttpURLConnection urlConnection)
			throws WTException {
		String strResponse = readResponse(urlConnection);

		try {
			JSONObject json = new JSONObject(strResponse);
			JSONObject status = json.getJSONObject("Status");
			int id = Integer.valueOf(status.getString("Id"));
			String memo = status.getString("Memo");

			if (id != 0) {
				return new HttpRequestResult(id, memo);
			}

			return new HttpRequestResult(id, json.getJSONObject("Data")
					.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			throw new WTException();
		}
	}

	private HttpRequestResult handleError(HttpURLConnection urlConnection)
			throws WTException {
		String strError = readResponse(urlConnection);

		try {
			return new HttpRequestResult(urlConnection.getResponseCode(),
					strError);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WTException(strError, e);
		}
	}

	private String readResponse(HttpURLConnection urlConnection)
			throws WTException {
		InputStream is = null;
		BufferedReader bfReader = null;

		try {
			is = urlConnection.getInputStream();
			String strConEncode = urlConnection.getContentEncoding();

			if (strConEncode != null && !strConEncode.equals("")
					&& strConEncode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			bfReader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sbError = new StringBuilder();
			String strLine;

			while ((strLine = bfReader.readLine()) != null) {
				sbError.append(strLine);
			}

			return sbError.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new WTException("IOException", e);
		} finally {
			WTUtility.closeResource(is);
			WTUtility.closeResource(bfReader);
			urlConnection.disconnect();
		}
	}

}
