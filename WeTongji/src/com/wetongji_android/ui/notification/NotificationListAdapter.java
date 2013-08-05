package com.wetongji_android.ui.notification;

import java.util.ArrayList;
import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Notification;
import com.wetongji_android.util.data.notification.NotificationsLoader;
import com.wetongji_android.util.date.DateParser;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter implements LoaderCallbacks<List<Notification>>
{
	private List<Notification> mListNotifications;
	private Fragment mFragment;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public NotificationListAdapter(Fragment fragment)
	{
		this.mFragment = fragment;
		this.mContext = fragment.getActivity();
		this.mListNotifications = new ArrayList<Notification>();
		this.mInflater = LayoutInflater.from(mContext);
		//this.mFragment.getLoaderManager().initLoader(WTApplication.NOTIFICATIONS_LOADER, null, this);
	}
	
	public void setContentList(List<Notification> notifications)
	{
		this.mListNotifications.clear();
		this.mListNotifications.addAll(notifications);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return this.mListNotifications.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return this.mListNotifications.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Loader<List<Notification>> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new NotificationsLoader(mContext, null);
	}
	
	@Override
	public void onLoadFinished(Loader<List<Notification>> arg0,
			List<Notification> notifications) 
	{
		// TODO Auto-generated method stub
		setContentList(notifications);
	}

	@Override
	public void onLoaderReset(Loader<List<Notification>> arg0) 
	{
		// TODO Auto-generated method stub
		
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
	
	@Override
	public View getView(int arg0, View view, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ConsultViewHolder holder;
		
		if(view == null)
		{
			holder = new ConsultViewHolder();
			view = mInflater.inflate(R.layout.row_request_notification, parent, false);
			holder.tv_notification_content = (TextView)view.findViewById(R.id.tv_notification_content);
			holder.tv_notification_time = (TextView)view.findViewById(R.id.tv_notification_time);
			holder.btn_notification_ignore  = (Button)view.findViewById(R.id.btn_notification_ignore);
			holder.btn_notification_accept = (Button)view.findViewById(R.id.btn_notification_yes);
			view.setTag(holder);
		}else
		{
			holder = (ConsultViewHolder)view.getTag();
		}
		
		Notification notification = mListNotifications.get(arg0);
		String title = notification.getTitle();
		String from = notification.getFrom();
		SpannableString spanStr = new SpannableString(title);
		spanStr.setSpan(new TextAppearanceSpan(mContext, R.style.NotificationItemFrom), 0, from.length(), 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new TextAppearanceSpan(mContext, R.style.NotificationItemContent), from.length(), title.length() - 1, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.tv_notification_content.setText(spanStr, TextView.BufferType.SPANNABLE);
		holder.tv_notification_time.setText(DateParser.parseDateForNotification(notification.getSentAt()));
		holder.btn_notification_ignore.setText("Ignore");
		if(notification.getType() == 3)
		{
			holder.btn_notification_accept.setText("Learn More");
		}else
		{
			holder.btn_notification_accept.setText("Accept");
		}
		
		OnClickListener onClickListener = 
				new OnNotifActionClickListener(notification);
		holder.btn_notification_accept.setOnClickListener(onClickListener);
		holder.btn_notification_ignore.setOnClickListener(onClickListener);
		
		return view;
	}
	
	private class OnNotifActionClickListener implements OnClickListener {
		private Notification mNotification;
		
		public OnNotifActionClickListener(Notification notif) {
			super();
			mNotification = notif;
		}
		@Override
		public void onClick(View view) {
			NotificationFragment fragment = (NotificationFragment) mFragment;
			if (view.getId() == R.id.btn_notification_ignore) {
				fragment.startIngoreAction(mNotification);
			} else if (view.getId() == R.id.btn_notification_yes) {
				fragment.startAcceptAction(mNotification);
				
			}
		}
		
	};
}