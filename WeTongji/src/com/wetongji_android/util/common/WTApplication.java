/**
 * 
 */
package com.wetongji_android.util.common;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.wetongji_android.util.data.DbHelper;

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
	public static final int NETWORK_LOADER_DEFAULT=1;
	public static final int NETWORK_LOADER_1=2;
	public static final int NETWORK_LOADER_2=3;
	public static final int NETWORK_LOADER_3=4;
	public static final int DB_LIST_LOADER=5;
	public static final int DB_LIST_SAVER=6;
	public static final int ACTIVITIES_LOADER=7;
	public static final int EVENTS_LOADER=8;
	public static final int NOTIFICATIONS_LOADER = 9;
	
	public static final String FLURRY_API_KEY="GN5KJMW6XWCSD5DTCWRW";
	
	//singleton
	private static WTApplication application = null;
	
	private DbHelper dbHelper;
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
		dbHelper=OpenHelperManager.getHelper(this, DbHelper.class);
		// Instantiate AQuery and configure cache directory
		aq = new AQuery(this);
		File ext = Environment.getExternalStorageDirectory();
        File cacheDir = new File(ext, "WeTongji/cache");
        AQUtility.setCacheDir(cacheDir);
	}

	public DbHelper getDbHelper() {
		return dbHelper;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		OpenHelperManager.releaseHelper();
	}
	
}
