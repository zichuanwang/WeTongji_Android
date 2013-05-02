package com.wetongji_android.ui.now;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Event;
import com.wetongji_android.factory.EventFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class NowPagerAdapter extends PagerAdapter {
	
	public static final int PAGE_LEFT=0;
	public static final int PAGE_RIGHT=2;
	public static final int PAGE_MIDDILE=1;
	
	private LayoutInflater inflater;
	private Fragment fragment;
	private ViewPager viewPager;
	private NowWeekListAdapter listAdapter;
	private PageNetworkLoaderCallbacks callbacks;
	private Calendar begin, end;
	private Context context;
	
	public NowPagerAdapter(Fragment fragment, ViewPager viewPager) {
		super();
		this.fragment=fragment;
		context=fragment.getActivity();
		this.inflater=fragment.getActivity().getLayoutInflater();
		this.viewPager=viewPager;
		initCalendars();
		initAdapters();
		
	}

	private void initAdapters() {
		listAdapter=new NowWeekListAdapter(fragment, begin, end);
		callbacks=new PageNetworkLoaderCallbacks();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch(position){
		case PAGE_LEFT:
		case PAGE_RIGHT:
			View view=inflater.inflate(R.layout.page_now_loading, null);
			container.addView(view);
			return view;
		default:
			AmazingListView nowWeekList=(AmazingListView) inflater.inflate(R.layout.page_now_week, null);
			Bundle args=null;
			nowWeekList.setAdapter(listAdapter);
			nowWeekList.setPinnedHeaderView(LayoutInflater.from(context).inflate(R.layout.section_bar_now, nowWeekList, false));
			nowWeekList.setOnItemClickListener(new ListItemClickListener());
			args=ApiHelper.getInstance(context).getSchedule(begin, end);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_1, args, callbacks).forceLoad();
			container.addView(nowWeekList);
			return nowWeekList;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	private void initCalendars(){
		begin=DateParser.getFirstDayOfWeek();
		end=DateParser.getLastDayOfWeek();
	}
	
	private void setCalendars(int pageScrolledTo){
		switch(pageScrolledTo){
		case PAGE_LEFT:
			Log.v("setcalendar", "page left");
			begin.add(Calendar.DAY_OF_YEAR, -7);
			end.add(Calendar.DAY_OF_YEAR, -7);
			break;
		case PAGE_RIGHT:
			Log.v("setcalendar", "page right");
			begin.add(Calendar.DAY_OF_YEAR, 7);
			end.add(Calendar.DAY_OF_YEAR, 7);
			break;
		}
	}
	
	public void setContent(int pageScrolledTo){
		if(pageScrolledTo!=PAGE_MIDDILE){
			Log.v("setContent", "page="+pageScrolledTo);
			setCalendars(pageScrolledTo);
			Bundle args=ApiHelper.getInstance(context).getSchedule(begin, end);
			fragment.getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_1, args, callbacks).forceLoad();
		}
	}
	
	private class PageNetworkLoaderCallbacks implements LoaderCallbacks<HttpRequestResult>{
		
		private EventFactory factory;
		
		public PageNetworkLoaderCallbacks() {
			super();
			this.factory=new EventFactory(fragment);
		}
		
		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
			Log.v("pagerAdapter", "createdLoader");
			return new NetworkLoader(context, HttpMethod.Get, args);
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult result) {
			if(result.getResponseCode()==0){
				List<Event> events=factory.createObjects(result.getStrResponseCon());
				listAdapter.setRawData(events);
			}
			else{
				ExceptionToast.show(context, result.getResponseCode());
			}
			viewPager.setCurrentItem(PAGE_MIDDILE, false);
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
			Log.v("pagerAdapter", "restartedLoader");
		}
		
	}
	
	private class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			Event event = listAdapter.getItem(position);
			Bundle bundle = new Bundle();
			bundle.putInt(EventsFragment.BUNDLE_KEY_ACTIVITY, event.getId());
			Intent intent=null;
			if(event instanceof Activity){
				intent = new Intent(context, EventDetailActivity.class);
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		}
		
	}
	
}
