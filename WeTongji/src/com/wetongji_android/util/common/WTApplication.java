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
