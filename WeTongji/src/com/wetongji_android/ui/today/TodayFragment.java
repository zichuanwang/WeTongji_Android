package com.wetongji_android.ui.today;

import java.util.Calendar;
import java.util.List;

import com.androidquery.AQuery;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Event;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.QueryHelper;
import com.wetongji_android.util.data.event.EventLoader;
import com.wetongji_android.util.data.event.EventUtil;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayFragment extends Fragment 
{
	
	private View view;
	private ViewPager vpBanner;
	private TodayBannerPagerAdapter bannerAdapter;
	private UnderlinePageIndicator indicator;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment NowFragment.
	 */
	public static TodayFragment newInstance() {
		TodayFragment fragment = new TodayFragment();
		return fragment;
	}

	public TodayFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		if(view==null){
			view=inflater.inflate(R.layout.fragment_today, container, false);
			setTodayBanner(view);
			Bundle args=QueryHelper.getEventQueryArgs(Calendar.getInstance());
			getLoaderManager().initLoader(WTApplication.EVENT_LOADER, args, new DbLoaderCallbacks()).forceLoad();
		}
		return view;
	}
	
	private void setTodayBanner(View view){
		vpBanner=(ViewPager) view.findViewById(R.id.vp_banner);
		bannerAdapter=new TodayBannerPagerAdapter(null, getActivity());
		vpBanner.setAdapter(bannerAdapter);
		vpBanner.setOffscreenPageLimit(bannerAdapter.getCount());
		vpBanner.setClipChildren(false);
		indicator=(UnderlinePageIndicator) view.findViewById(R.id.vp_indicator_today);
		indicator.setViewPager(vpBanner);
		indicator.setFades(false);
	}
	
	private void setTodayNowContent(Event event){
		TextView tvNowFriendsCounter=(TextView) view.findViewById(R.id.tv_today_now_friends_counter);
		TextView tvNowTitle=
				(TextView)view.findViewById(R.id.tv_today_now_title);
		TextView tvNowTime=
				(TextView)view.findViewById(R.id.tv_today_now_time);
		TextView tvNowLocation = 
				(TextView)view.findViewById(R.id.tv_today_now_location);
		ImageView ivNowThumb=
				(ImageView)view.findViewById(R.id.iv_today_now_thumb);
		
		tvNowTitle.setText(event.getTitle());
		tvNowTime.setText(EventUtil.getEventDisplayTime(event, getActivity()));
		tvNowLocation.setText(event.getLocation());
		//TODO
		tvNowFriendsCounter.setText("6 Friends");
		
		if(event instanceof Activity){
			// Set thumb nails
			String strUrl = ((Activity)event).getImage();
			AQuery aq = new AQuery(getActivity());
			if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL)){
		        	aq.id(ivNowThumb).image(strUrl, true, true, 300, R.drawable.event_list_thumbnail_place_holder,
		        			null, AQuery.FADE_IN_NETWORK, 1.33f);
			}
		}
		else{
			ivNowThumb.setVisibility(View.GONE);
		}
		
	}
	
	private class NetwordLoaderCallbacks implements LoaderCallbacks<HttpRequestResult>{

		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class DbLoaderCallbacks implements LoaderCallbacks<List<Event>>{

		@Override
		public Loader<List<Event>> onCreateLoader(int arg0, Bundle arg1) {
			return new EventLoader(getActivity(), arg1);
		}

		@Override
		public void onLoadFinished(Loader<List<Event>> arg0, List<Event> events) {
			if(events!=null&&!events.isEmpty()){
				Log.d("today event", events.get(0).getTitle());
				setTodayNowContent(events.get(0));
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Event>> arg0) {
		}
		
	}
	
}