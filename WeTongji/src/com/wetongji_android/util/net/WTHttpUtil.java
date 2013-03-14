/**
 * 
 */
package com.wetongji_android.util.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @author nankonami
 *
 */
public class WTHttpUtil 
{
	private WTHttpUtil()
	{
		//Forbidden being instantiated.
	}
	
	public static String encodeUrl(Map<String, String> params)
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
				sb.append(URLEncoder.encode(strKey, "UTF-8")).append("=").append(URLEncoder.encode(params.get(strKey), "UTF-8"));
			} catch (UnsupportedEncodingException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}
