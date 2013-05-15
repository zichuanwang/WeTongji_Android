package com.wetongji_android.ui.event;

import java.util.List;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.ui.event.EventsListAdapter.ViewHolder;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.data.activity.ActivitiesLoader;
import com.wetongji_android.util.date.DateParser;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndlessEventListAdapter extends EndlessListAdapter<Activity> implements LoaderCallbacks<List<Activity>>{

	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private Fragment mFragment;
	
	private BitmapDrawable mBmDefaultThumbnails;
	
	public EndlessEventListAdapter(Fragment fragment, AbsListView listView) {
		super(fragment.getActivity(), listView, R.layout.amazing_lst_view_loading_view);
		mInflater = LayoutInflater.from(fragment.getActivity());
		mContext = fragment.getActivity();
		mListAq = WTApplication.getInstance().getAq(fragment.getActivity());
		mFragment = fragment;
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
	protected View doGetView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.row_event, parent, false);
			holder.tvEventTitle=
					(TextView)convertView.findViewById(R.id.tv_event_title);
			holder.tvEventTime=
					(TextView)convertView.findViewById(R.id.tv_event_time);
			holder.tvEventLocation = 
					(TextView)convertView.findViewById(R.id.tv_event_location);
			holder.ivEventThumb=
					(ImageView)convertView.findViewById(R.id.img_event_thumbnails);
			holder.llEventRow = 
					(LinearLayout)convertView.findViewById(R.id.layout_event_row);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		//Set background color
		if(position % 2 != 0) {
			holder.llEventRow.setBackgroundColor(mContext.
					getResources().getColor(R.color.layout_event_list_row1));
		}else {
			holder.llEventRow.setBackgroundColor(mContext.
					getResources().getColor(R.color.layout_event_list_row2));
		}
		
		Activity event=getItem(position);
		
		holder.tvEventTitle.setText(event.getTitle());
		holder.tvEventTime.setText(
				DateParser.getEventTime(mContext, event.getBegin(), event.getEnd()));
		
		if(DateParser.isNow(event.getBegin(), event.getEnd())) {
			int timeColor = mContext.getResources().getColor(R.color.tv_eventlst_time_now);
			holder.tvEventTime.setTextColor(timeColor);
		}else {
			int timeColor = mContext.getResources().getColor(R.color.tv_eventlst_time);
			holder.tvEventTime.setTextColor(timeColor);
		}
		
		holder.tvEventLocation.setText(event.getLocation());
		
		// Set thumbnails
		String strUrl = event.getImage();

		mShouldDelayAq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL)){
			
	        if(mShouldDelayAq.shouldDelay(position, convertView, parent, strUrl)) {
	        	mShouldDelayAq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
	        }
	        else {
	        	mShouldDelayAq.id(holder.ivEventThumb).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1.33f);
	        }
		}
		else{
			mShouldDelayAq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
		}
		
		return convertView;
	}

	@Override
	public Loader<List<Activity>> onCreateLoader(int arg0, Bundle arg1) {
		return new ActivitiesLoader(mContext, null);
	}

	@Override
	public void onLoadFinished(Loader<List<Activity>> arg0, List<Activity> arg1) {

		WTUtility.log("data", "DBLoadeFinished");
		
		if (arg1 != null && arg1.size() != 0) {
			getData().addAll(arg1);
			setIsLoadingData(false);
			notifyDataSetChanged();
		} else {
			((EventsFragment)mFragment).refreshData();
		}
		
	}

	@Override
	public void onLoaderReset(Loader<List<Activity>> arg0) {
	}
	
	public void loadDataFromDB() {
		mFragment.getLoaderManager().initLoader(WTApplication.ACTIVITIES_LOADER, null, this);
		setIsLoadingData(true);
	}
	
}
