package com.wetongji_android.ui.now.week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Event;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbListLoader;
import com.wetongji_android.util.data.QueryHelper;
import com.wetongji_android.util.data.event.EventUtil;
import com.wetongji_android.util.net.ApiHelper;

public class NowWeekListAdapter extends AmazingAdapter implements
		LoaderCallbacks<List<Event>> {

	private LayoutInflater inflater;
	private Context context;
	private AQuery listAq;
	private List<Pair<String, List<Event>>> events;
	private Fragment fragment;
	private ApiHelper apiHelper;
	private Calendar begin;
	
	public NowWeekListAdapter(Fragment fragment, Calendar begin) {
		this.fragment = fragment;
		context=fragment.getActivity();
		inflater=LayoutInflater.from(context);
		listAq=new AQuery(context);
		events=new ArrayList<Pair<String,List<Event>>>();
		apiHelper=ApiHelper.getInstance(context);
		this.begin=begin;
		this.fragment.getLoaderManager().initLoader(
				WTApplication.EVENTS_LOADER, QueryHelper.getEventQueryArgs(begin), this);	
	}
	
	static class ViewHolder {
		ImageView ivNowIndicator;
		TextView tvNowIndicator;
		TextView tvNowTitle;
		TextView tvNowTime;
		TextView tvNowLocation;
		ImageView ivNowThumb;	
		TextView tvNowFriendsCounter;
	}

	@Override
	public int getCount() {
		int c = 0;
	    for (int i = 0; i < events.size(); i++) {
	        c += events.get(i).second.size();
	    }
	    return c;
	}

	@Override
	public Event getItem(int position) {
		int c = 0;
	    for (int i = 0; i < events.size(); i++) {
	        if (position >= c && position < c + events.get(i).second.size()) {
	                return events.get(i).second.get(position - c);
	        }
	        c += events.get(i).second.size();
	    }
	    return null;
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	protected void onNextPageRequested(int page) {
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.row_event, parent, false);
			holder.tvNowIndicator=(TextView) convertView.findViewById(R.id.tv_now_indicator);
			holder.ivNowIndicator=(ImageView) convertView.findViewById(R.id.iv_now_indicator);
			holder.tvNowFriendsCounter=(TextView) convertView.findViewById(R.id.tv_now_friends_counter);
			holder.tvNowTitle=
					(TextView)convertView.findViewById(R.id.tv_now_title);
			holder.tvNowTime=
					(TextView)convertView.findViewById(R.id.tv_now_time);
			holder.tvNowLocation = 
					(TextView)convertView.findViewById(R.id.tv_now_location);
			holder.ivNowThumb=
					(ImageView)convertView.findViewById(R.id.iv_now_thumb);
			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();
		
		Event event=(Event) getItem(position);
		if(EventUtil.isNextEvent(event)){
			convertView.setBackgroundResource(R.drawable.bg_row_now_current);
			holder.ivNowIndicator.setVisibility(View.VISIBLE);
			holder.tvNowIndicator.setVisibility(View.VISIBLE);
		}
		else if(EventUtil.isPastEvent(event)){
			convertView.setBackgroundResource(R.drawable.bg_row_now_passed);
		}
			
		holder.tvNowTitle.setText(event.getTitle());
		holder.tvNowTime.setText(EventUtil.getEventDisplayTime(event, context));
		holder.tvNowLocation.setText(event.getLocation());
		//TODO
		holder.tvNowFriendsCounter.setText("6 Friends");
		
		if(event instanceof Activity){
			// Set thumbnails
			/*String strUrl=event.getDescription();
			AQuery aq = listAq.recycle(convertView);
			File ext=Environment.getExternalStorageDirectory();
	        File cacheDir=new File(ext, "WeTongji/cache");
	        AQUtility.setCacheDir(cacheDir);
	        int imageId = holder.ivNowThumb.getId();
	        Bitmap resetAvatar = BitmapFactory.decodeResource(context.getResources(),
	                R.drawable.default_avatar);
	        if(aq.shouldDelay(position, convertView, parent, strUrl))
	        	aq.image(resetAvatar);
	        else
	        	aq.id(imageId).image(strUrl, true, true, 0, R.drawable.default_avatar, resetAvatar,
	        			AQuery.FADE_IN_NETWORK, 1.0f);
	        */
		}
		else{
			holder.ivNowThumb.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Loader<List<Event>> onCreateLoader(int arg0, Bundle arg1) {
		return new DbListLoader<Event, Integer>(context, Event.class);
	}

	@Override
	public void onLoadFinished(Loader<List<Event>> arg0, List<Event> events) {
		this.events=EventUtil.getSectionedEventList(events);
	}

	@Override
	public void onLoaderReset(Loader<List<Event>> arg0) {
	}

}
