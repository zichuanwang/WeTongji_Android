/**
 * 
 */
package com.wetongji_android.net;

import android.os.Bundle;

import com.wetongji_android.net.http.HttpClient;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.exception.WTException;


/**
 * @author nankonami
 *
 */
public class WTClient 
{
	//Implement the singleton
	private WTClient()
	{
		//Forbidden being instantiated. 
	}
	
	private static class WTClientContainer
	{
		private static WTClient client = new WTClient();
	}
	
	public static synchronized WTClient getInstance()
	{
		return WTClientContainer.client;
	}
	
	public String execute(HttpMethod httpMethod, Bundle params) throws WTException
	{
		return new HttpClient().execute(httpMethod, params);
	}
}
