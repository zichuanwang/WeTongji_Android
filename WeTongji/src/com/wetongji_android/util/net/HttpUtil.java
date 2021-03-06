/**
 * 
 */
package com.wetongji_android.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author nankonami
 *
 */
public class HttpUtil 
{
	private HttpUtil()
	{
		//Forbidden being instantiated.
	}
	
	/**
	 * Build the url query string according to the params bundle
	 * @param params
	 * @return
	 */
	public static String encodeUrl(Bundle params)
	{
		if(params == null || params.isEmpty())
			return "";
		
		StringBuilder sb = new StringBuilder();
		boolean bFirst = true;
		
		Set<String> setKeys = new HashSet<String>(params.keySet());
		
		for(String strKey : setKeys)
		{
            if (TextUtils.isEmpty(strKey)) {
                continue;
            }
			if(bFirst)
				bFirst = false;
			else
				sb.append("&");
			
			try 
			{
				sb.append(URLEncoder.encode(strKey, "UTF-8"))
					.append("=")
					.append(URLEncoder.encode(params.getString(strKey), "UTF-8"));
			} catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	public static String getPostBody(Bundle params) throws JSONException
	{
		JSONObject json = new JSONObject();
	
		json.put("DisplayName", params.get(""));
	    json.put("Email", params.get(""));
	    json.put("SinaWeibo", params.get(""));
		json.put("QQ", params.get(""));
        
		return "";
	}
	
	/**
	 * Update user profile
	 * @param name
	 * @param email
	 * @param qq
	 * @param phone
	 * @param weibo
	 * @return
	 */
	public static String getUserUpdateBody(String name, String email, String qq, String 
			phone, String weibo) throws JSONException
	{
		JSONObject json = new JSONObject();
		
		json.put("DisplayName", name);
		json.put("Email", email);
		json.put("SinaWeibo", weibo);
		json.put("Phone", phone);
		json.put("QQ", qq);
		
		JSONObject user = new JSONObject();
		user.put("User", json.toString());
		
		return user.toString();
	}
	
	/**
	 * Get Net Type
	 * @param context
	 * @return
	 */
	public static int getNetType(Context context)
	{
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conManager.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnected())
		{
			return netInfo.getType();
		}
		return -1;
	}
	
	/**
	 * If the net is wifi, return true, otherwise false
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context)
	{
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conManager.getActiveNetworkInfo();
		if(netInfo != null && netInfo.isConnected())
		{
			if(netInfo.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		
		return false;
	}
	
	/**
	 * If the net is connected return true, otherwise false
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context)
	{
		boolean bConnected = false;
		
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		if(conManager != null)
		{
			NetworkInfo netInfo = conManager.getActiveNetworkInfo();
			if(netInfo != null && netInfo.isConnected())
				bConnected = true;
		}
		
		return bConnected;
	}
	
	public static String generateUserIDArrayString(String ids)
	{
		if(ids == null){
			return null;
		}else{
			return ids.substring(0, ids.length() - 1);
		}
	}
	
	public static int getFriendsCountWithResponse(String response){
		int result = 0;
		
		try {
			JSONObject json = new JSONObject(response);
			JSONArray users = json.getJSONArray("Users");
			result = users.length();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}