/**
 * 
 */
package com.wetongji_android.util.common;

import android.app.Application;

/**
 * @author zihe
 *
 */
public class WTApplication extends Application 
{
	/**
	 * static constants
	 */
	// account type
	public static final String ACCOUNT_TYPE="com.wetongji";
	public static final String AUTHTOKEN_TYPE="com.wetongji";
	// api_constrants
	public static final String API_DEVICE="android";
	public static final String API_VERSION="3.0";
	// loader ids
	public static final int NETWORK_LOADER=1;
	public static final int DB_LOADER=2;
	public static final int DB_SAVER=3;
	public static final int ACTIVITY_LOADER=4;
	
	public static final String FLURRY_API_KEY="GN5KJMW6XWCSD5DTCWRW";
	
	//singleton
	private static WTApplication application = null;
	
	public boolean bStarted = false;
	
	public static WTApplication getInstance()
	{
		return application;
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		application = this;
	}
}
