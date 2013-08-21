package com.wetongji_android.util.data.notification;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.Notification;
import com.wetongji_android.util.data.DbListLoader;

public class NotificationsLoader extends DbListLoader<Notification, Integer> 
{
	
	public NotificationsLoader(Context context) 
	{
		super(context, Notification.class);
	}

	@Override
	public List<Notification> loadInBackground() 
	{
		// TODO Auto-generated method stub
		try 
		{
			return mDao.queryForAll();
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}