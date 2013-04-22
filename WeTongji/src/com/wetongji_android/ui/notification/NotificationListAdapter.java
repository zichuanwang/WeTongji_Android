package com.wetongji_android.ui.notification;

import java.util.List;

import com.wetongji_android.data.Notification;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationListAdapter extends ArrayAdapter<Notification> 
{
	public NotificationListAdapter(Context context, int resource,
			int textViewResourceId, List<Notification> objects) 
	{
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		Notification notification = getItem(position);
		if(notification.getType() == 1)
		{
			
		}else if(notification.getType() == 2)
		{
			
		}else
		{
			
		}
		
		return super.getView(position, convertView, parent);
	}

	static class InformViewHolder
	{
		TextView tv_notification_time;
		TextView tv_notification_content;
		ImageView img_notification_thumbnails;
	}
	
	static class ConsultViewHolder
	{
		TextView tv_notification_time;
		TextView tv_notification_content;
		ImageView img_notification_thumbnails;
		Button btn_notification_ignore;
		Button btn_notification_accept;
	}
}
