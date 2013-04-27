package com.wetongji_android.ui.event;

import java.util.ArrayList;
import java.util.List;
import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.activity.ActivitiesLoader;
import com.wetongji_android.util.net.ApiHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventsListAdapter extends AmazingAdapter implements LoaderCallbacks<List<Activity>>{
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private List<Activity> mLstEvent;
	private Fragment mFragment;
	private ApiHelper apiHelper;
	
	public EventsListAdapter(Fragment fragment) { 
		mInflater = LayoutInflater.from(fragment.getActivity());
		mContext = fragment.getActivity();
		mListAq = WTApplication.aq;
		mFragment = fragment;
		mLstEvent=new ArrayList<Activity>();
		apiHelper=ApiHelper.getInstance(mContext);
		mFragment.getLoaderManager().initLoader(WTApplication.ACTIVITIES_LOADER, null, this);
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
		TextView tv_event_title;
		TextView tv_event_time;
		TextView tv_event_location;
		ImageView img_event_thumbnails;	
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
		
		Bundle args = apiHelper.getActivities(page, "", "", false);
		mFragment.getLoaderManager()
			.initLoader(WTApplication.NETWORK_LOADER, args, (EventsFragment)mFragment);
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
			holder.tv_event_title=
					(TextView)convertView.findViewById(R.id.tv_event_title);
			holder.tv_event_time=
					(TextView)convertView.findViewById(R.id.tv_event_time);
			holder.tv_event_location = 
					(TextView)convertView.findViewById(R.id.tv_event_location);
			holder.img_event_thumbnails=
					(ImageView)convertView.findViewById(R.id.img_event_thumbnails);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		Activity event=getItem(position);
		
		holder.tv_event_title.setText(event.getTitle());
		//holder.tv_event_time.setText(
		//		DateParser.parseBeginAndEndTime(event.getBegin(), event.getEnd()));
		
		holder.tv_event_time.setText("10:00-12:00");
		
		holder.tv_event_location.setText(event.getLocation());
		
		// Set thumbnails
		//String strUrl = event.getImage();
		//AQuery aq = mListAq.recycle(convertView);
		
        //int imageId = holder.img_event_thumbnails.getId();
        //Bitmap bmThumbnails = BitmapFactory.decodeResource(mContext.getResources(),
        //        R.drawable.default_avatar);
        //if(aq.shouldDelay(position, convertView, parent, strUrl))
        //	aq.image(bmThumbnails);
        //else
        //	aq.id(imageId).image(strUrl, false, true, 0, R.drawable.default_avatar, bmThumbnails,
        //			AQuery.FADE_IN_NETWORK, 1.0f);
		
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