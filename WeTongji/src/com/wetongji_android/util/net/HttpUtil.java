/**
 * 
 */
package com.wetongji_android.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

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
		if(params == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		boolean bFirst = true;
		
		Set<String> setKeys = params.keySet();
		
		for(String strKey : setKeys)
		{
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.v("The param string is: ", sb.toString());
		return sb.toString();
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
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conManager.getActiveNetworkInfo();
		
		return netInfo != null && netInfo.isConnected();
	}
}