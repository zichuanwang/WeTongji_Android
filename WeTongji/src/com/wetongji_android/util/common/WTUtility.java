/**
 * 
 */
package com.wetongji_android.util.common;

import java.io.Closeable;
import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
	
	public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

	public static boolean isGprs(Context context) {
		ConnectivityManager cm = (ConnectivityManager)
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}
	
    public static int getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

}
