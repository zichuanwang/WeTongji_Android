package com.wetongji_android.ui.friend;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.ui.EndlessListAdapter;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.user.UserLoader;
import com.wetongji_android.util.net.HttpUtil;

public class FriendListAdapter extends EndlessListAdapter<User> implements
		LoaderCallbacks<List<User>> 
{
	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private Fragment mFragment;
	
	private BitmapDrawable mBmDefaultThumbnails;
	
	static class ViewHolder
	{
		TextView tvFriendName;
		TextView tvFriendDepart;
		ImageView imgFriendAvatar;
		RelativeLayout rlFriendItem;
	}
	
	public FriendListAdapter(Fragment fragment, AbsListView listView)
	{
		super(fragment.getActivity(), listView, R.layout.amazing_lst_view_loading_view);
		
		mContext = fragment.getActivity();
		mInflater = LayoutInflater.from(mContext);
		mFragment = fragment;
		mListAq = WTApplication.getInstance().getAq(mFragment.getActivity());
		mBmDefaultThumbnails = (BitmapDrawable) mContext.getResources()
				.getDrawable(R.drawable.event_list_thumbnail_place_holder);
		
		WTApplication app = WTApplication.getInstance();
		app.setActivity(fragment.getActivity());
		DisplayMetrics dm = app.getDisplayMetrics();
		if(dm.widthPixels <= 1080) {
			LIST_THUMBNAILS_TARGET_WIDTH = (int)(dm.widthPixels / LIST_THUMBNAILS_TARGET_WIDTH_FACTOR);
		}
	}
	
	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if(convertView ==  null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_friend, parent, false);
			holder.tvFriendName = (TextView)convertView.findViewById(R.id.friend_name);
			holder.tvFriendDepart = (TextView)convertView.findViewById(R.id.friend_department);
			holder.imgFriendAvatar = (ImageView)convertView.findViewById(R.id.friend_avatar);
			holder.rlFriendItem = (RelativeLayout)convertView.findViewById(R.id.friend_item);
			
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		//Set background color
		if(position % 2 == 0)
		{
			holder.rlFriendItem.setBackgroundColor(mContext.
					getResources().getColor(R.color.layout_event_list_row1));
		}else
		{
			holder.rlFriendItem.setBackgroundColor(mContext.
					getResources().getColor(R.color.layout_event_list_row2));
		}
		
		//Set text view
		User user = getItem(position);
		holder.tvFriendName.setText(user.getName());
		holder.tvFriendDepart.setText(user.getDepartment());
		
		//Set avatar
		String strUrl = HttpUtil.replaceURL(user.getAvatar());
		mShouldDelayAq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL))
		{
			if(mShouldDelayAq.shouldDelay(position, convertView, parent, strUrl))
			{
				mShouldDelayAq.id(holder.imgFriendAvatar).image(mBmDefaultThumbnails);
			}else
			{
				mShouldDelayAq.id(holder.imgFriendAvatar).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1.0f);
			}
		}else
		{
			mShouldDelayAq.id(holder.imgFriendAvatar).image(mBmDefaultThumbnails);
		}
		
		return convertView;
	}

	@Override
	public Loader<List<User>> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new UserLoader(mContext);
	}

	@Override
	public void onLoadFinished(Loader<List<User>> arg0, List<User> arg1) 
	{
		// TODO Auto-generated method stub
		if(arg1 != null && arg1.size() > 1)
		{
			Log.v("adapter", "" + arg1.size());
		}else
		{
			((FriendListFragment)mFragment).refreshData();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<User>> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void loadDataFromDB() 
	{
		mFragment.getLoaderManager().initLoader(WTApplication.USER_LOADER, null, this);
		setIsLoadingData(true);
	}
}
