package com.wetongji_android.ui.notification;

import java.util.List;

import com.foound.widget.AmazingAdapter;
import com.wetongji_android.data.Notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationListAdapter extends AmazingAdapter implements LoaderCallbacks<List<Notification>>
{
	private List<Notification> mLstNotifications;
	private Fragment mFragment;
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Loader<List<Notification>> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return null;
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
	public void onLoadFinished(Loader<List<Notification>> arg0,
			List<Notification> arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<List<Notification>> arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onNextPageRequested(int page) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPositionForSection(int section)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
