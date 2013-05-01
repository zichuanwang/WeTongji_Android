package com.wetongji_android.util.data.notification;

import android.content.Context;

import com.wetongji_android.data.Notification;
import com.wetongji_android.util.data.DbListLoader;

public class NotificationsLoader extends DbListLoader<Notification, Integer> 
{

	public NotificationsLoader(Context context, Class<Notification> clazz) 
	{
		super(context, clazz);
		// TODO Auto-generated constructor stub
	}

}
