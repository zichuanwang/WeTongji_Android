/**
 * 
 */
package com.wetongji_android.util.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author nankonami
 *
 */
public class WTUtility 
{
	private WTUtility()
	{
		//Forbidden being instantiated.
	}
	
	/*
	 * From java version 7.x this method is
	 * not necessary
	 */
	public static void closeResource(Closeable closeable)
	{
		if(closeable != null)
		{
			try
			{
				closeable.close();
			}catch(IOException e)
			{
				
			}
		}
	}
}
