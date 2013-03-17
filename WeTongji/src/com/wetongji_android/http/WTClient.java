/**
 * 
 */
package com.wetongji_android.http;

import java.util.Map;

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
	
	public String execute(WTHttpMethod httpMethod, Map<String, String> params) throws WTException
	{
		return new WTHttpClient().execute(httpMethod, params);
	}
}
