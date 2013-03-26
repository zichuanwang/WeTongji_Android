/**
 * 
 */
package com.wetongji_android.util.common;

import com.wetongji_android.data.User;

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
	public static final String API_VERSION="1.2";
	// api arguments
	public static final String API_ARGS_DEVICE="D";
	public static final String API_ARGS_METHOD="M";
	public static final String API_ARGS_VERSION="V";
	public static final String API_ARGS_UID="U";
	public static final String API_ARGS_PAGE="P";
	public static final String API_ARGS_SESSION="S";
	// api methods
	public static final String API_METHOD_USER_LOGON="User.LogOn";
	// api arguments used in methods
	public static final String API_METHOD_ARGS_NO="NO";
	public static final String API_METHOD_ARGS_PASSWORD="Password";
	// loader ids
	public static final int NETWORK_LOADER=1;
	
	//singleton
	private static WTApplication application = null;
	
	//current user info
	private User currentUser = null;
	
	public boolean bStarted = false;
	
	public static WTApplication getInstance()
	{
		return application;
	}

	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
	}
}
