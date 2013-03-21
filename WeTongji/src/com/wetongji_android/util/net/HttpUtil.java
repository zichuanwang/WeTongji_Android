/**
 * 
 */
package com.wetongji_android.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import android.os.Bundle;

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
		
		return sb.toString();
	}
}
