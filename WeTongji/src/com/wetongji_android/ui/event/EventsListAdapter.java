package com.wetongji_android.ui.event;

import java.util.ArrayList;
import java.util.List;
import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.activity.ActivitiesLoader;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.net.ApiHelper;

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
import android.widget.ImageView;
import android.widget.TextView;

public class EventsListAdapter extends AmazingAdapter implements LoaderCallbacks<List<Activity>>{
	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private List<Activity> mLstEvent;
	private Fragment mFragment;
	private ApiHelper apiHelper;
	
	private BitmapDrawable mBmDefaultThumbnails;

	public EventsListAdapter(Fragment fragment) { 
		mInflater = LayoutInflater.from(fragment.getActivity());
		mContext = fragment.getActivity();
		mListAq = WTApplication.getInstance().getAq(fragment.getActivity());
		mFragment = fragment;
		mLstEvent=new ArrayList<Activity>();
		apiHelper=ApiHelper.getInstance(mContext);
		mFragment.getLoaderManager().initLoader(WTApplication.ACTIVITIES_LOADER, null, this);
		
		mBmDefaultThumbnails = (BitmapDrawable) mContext.getResources()
				.getDrawable(R.drawable.event_list_thumbnail_place_holder);
		
		WTApplication app = WTApplication.getInstance();
		app.setActivity(fragment.getActivity());
		DisplayMetrics dm = app.getDisplayMetrics();
		if(dm.widthPixels <= 1080) {
			LIST_THUMBNAILS_TARGET_WIDTH = (int)(dm.widthPixels / LIST_THUMBNAILS_TARGET_WIDTH_FACTOR);
		}
		Log.d("target", String.valueOf(LIST_THUMBNAILS_TARGET_WIDTH));
	}

	public void setContentList(List<Activity> lstEvent) {
		mLstEvent.clear();
		mLstEvent.addAll(lstEvent);
		notifyDataSetChanged();
	}
	
	public void addData(List<Activity> nextPageData){
		mLstEvent.addAll(nextPageData);
	}
	
	static class ViewHolder {
		TextView tvEventTitle;
		TextView tvEventTime;
		TextView tvEventLocation;
		ImageView ivEventThumb;	
	}

	@Override
	public int getCount() {
		return mLstEvent.size();
	}

	@Override
	public Activity getItem(int arg0) {
		return mLstEvent.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	protected void onNextPageRequested(int page) {
		Log.d("EventListAdapter", "onNextPagerRequest...");
		
		Bundle args = apiHelper.getActivities(page, 15, ApiHelper.API_ARGS_SORT_BY_ID_DESC, false);
		mFragment.getLoaderManager()
			.initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, (EventsFragment)mFragment);
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
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
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
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

		AQuery aq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL)){
			
	        if(aq.shouldDelay(position, convertView, parent, strUrl)) {
	        	aq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
	        }
	        else {
	        	aq.id(holder.ivEventThumb).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1.33f);
	        }
		}
		else{
			aq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
		}
        
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
	}

	@Override
	public int getPositionForSection(int section) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
	
	@Override
	public Loader<List<Activity>> onCreateLoader(int arg0, Bundle args) {
		return new ActivitiesLoader(mContext, null);
	}

	@Override
	public void onLoadFinished(Loader<List<Activity>> arg0, List<Activity> activities) {
		setContentList(activities);
	}

	@Override
	public void onLoaderReset(Loader<List<Activity>> arg0) {
	}
	
	
}