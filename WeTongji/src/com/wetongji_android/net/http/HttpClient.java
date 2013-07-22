/**
 * 
 */
package com.wetongji_android.net.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.wetongji_android.ui.setting.DevSettingActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.util.net.HttpUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

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
	// private static final String API_DOMAIN =
	// "http://we.tongji.edu.cn/api/call";
	private static final String API_DOMAIN = "http://leiz.name:8080/api/call";
	// private static String API_DOMAIN;

	private static final String HTTP_TIMEOUT = "HttpTimeout";

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
			// urlConnection.setRequestProperty("Content-Type",
			// "binary/octet-stream");

			// set read timeout
			urlConnection.setReadTimeout(UPLOAD_FILE_READ_TIMEOUT);
			
			String BOUNDARYSTR = getBoundry();
			String path = body.getString("Image");
			File targetFile = new File(path);
			byte[] barry = null;
			int contentLength = 0;
			String sendStr = "";

			try {
				barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");
				sendStr = getBoundaryMessage(BOUNDARYSTR, "Image",
						"upload_avatar.jpg", "image/*");
				contentLength = sendStr.getBytes("UTF-8").length
						+ (int) targetFile.length() + 2 * barry.length;
			} catch (UnsupportedEncodingException e) {
			}
			int totalSent = 0;
			String lenstr = Integer.toString(contentLength);
			BufferedOutputStream out = null;
			FileInputStream fis = null;
			urlConnection.setRequestProperty("Content-type",
					"multipart/form-data;boundary=" + BOUNDARYSTR);
			urlConnection.setRequestProperty("Content-Length", lenstr);
			urlConnection.setFixedLengthStreamingMode(contentLength);
			urlConnection.connect();
			
			out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(sendStr.getBytes("UTF-8"));
            totalSent += sendStr.getBytes("UTF-8").length;
            fis = new FileInputStream(targetFile);
            int bytesRead;
            int bytesAvailable;
            int bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024;

            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fis.read(buffer, 0, bufferSize);
            long transferred = 0;
            final Thread thread = Thread.currentThread();
            while (bytesRead > 0) {

                if (thread.isInterrupted()) {
                    targetFile.delete();
                    throw new InterruptedIOException();
                }
                out.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                transferred += bytesRead;
                if (transferred % 50 == 0)
                    out.flush();

            }
            out.write(barry);
            totalSent += barry.length;
            out.write(barry);
            totalSent += barry.length;
            out.flush();
            out.close();
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
			throw new WTException(HTTP_TIMEOUT, e);
		} finally {
			WTUtility.closeResource(is);
			WTUtility.closeResource(bfReader);
			urlConnection.disconnect();
		}
	}

	private static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	private String getBoundaryMessage(String boundary, String fileField,
			String fileName, String fileType) {
		StringBuffer res = new StringBuffer("--").append(boundary).append(
				"\r\n");
		res.append("Content-Disposition: form-data; name=\"").append(fileField)
				.append("\"; filename=\"").append(fileName).append("\"\r\n")
				.append("Content-Type: ").append(fileType).append("\r\n\r\n");

		return res.toString();
	}
}
