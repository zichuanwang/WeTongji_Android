/**
 * 
 */
package com.wetongji_android.util.common;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import java.io.File;

import android.app.Application;
import android.os.Environment;

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
	public static final int DB_LIST_LOADER=2;
	public static final int DB_LIST_SAVER=3;
	public static final int ACTIVITIES_LOADER=4;
	public static final int EVENTS_LOADER=5;
	
	public static final String FLURRY_API_KEY="GN5KJMW6XWCSD5DTCWRW";
	
	//singleton
	private static WTApplication application = null;
	
	public boolean bStarted = false;
	
	public static AQuery aq;
	
	public static WTApplication getInstance()
	{
		return application;
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		application = this;
		
		
		// Instantiate AQuery and configure cache directory
		aq = new AQuery(this);
		File ext = Environment.getExternalStorageDirectory();
        File cacheDir = new File(ext, "WeTongji/cache");
        AQUtility.setCacheDir(cacheDir);
	}
}
