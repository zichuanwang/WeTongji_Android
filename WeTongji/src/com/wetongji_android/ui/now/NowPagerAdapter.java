package com.wetongji_android.ui.now;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.now.week.NowWeekListAdapter;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.data.Event;
import com.wetongji_android.factory.EventFactory;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NowPagerAdapter extends PagerAdapter {
	
	public static final int PAGE_LEFT=0;
	public static final int PAGE_RIGHT=2;
	public static final int PAGE_MIDDILE=1;
	
	private LayoutInflater inflater;
	private Fragment fragment;
	private NowWeekListAdapter leftListAdapter, middleListAdapter, rightListAdapter;
	private PageNetworkLoaderCallbacks leftCallbacks,rightCallbacks,middleCallbacks;
	private Calendar leftBegin, rightBegin, middleBegin;
	private Calendar leftEnd, rightEnd, middleEnd;
	private Context context;
	
	public NowPagerAdapter(Fragment fragment) {
		super();
		this.fragment=fragment;
		context=fragment.getActivity();
		this.inflater=fragment.getActivity().getLayoutInflater();
		initCalendars();
		initAdapters();
		
	}

	private void initAdapters() {
		leftListAdapter=new NowWeekListAdapter(fragment, leftBegin, leftEnd);
		rightListAdapter=new NowWeekListAdapter(fragment, rightBegin, rightEnd);
		middleListAdapter=new NowWeekListAdapter(fragment, middleBegin, middleEnd);
		leftCallbacks=new PageNetworkLoaderCallbacks(leftListAdapter);
		rightCallbacks=new PageNetworkLoaderCallbacks(rightListAdapter);
		middleCallbacks=new PageNetworkLoaderCallbacks(middleListAdapter);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		AmazingListView nowWeekList=(AmazingListView) inflater.inflate(R.layout.page_now_week, null);
		Bundle args=null;
		switch(position){
		case PAGE_LEFT:
			nowWeekList.setAdapter(leftListAdapter);
			args=ApiHelper.getInstance(context).getSchedule(leftBegin, leftEnd);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_1, args, leftCallbacks);
		case PAGE_RIGHT:
			nowWeekList.setAdapter(rightListAdapter);
			args=ApiHelper.getInstance(context).getSchedule(rightBegin, rightEnd);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_2, args, rightCallbacks);
		default:
			nowWeekList.setAdapter(middleListAdapter);
			args=ApiHelper.getInstance(context).getSchedule(middleBegin, middleEnd);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_3, args, middleCallbacks);
		}
		container.addView(nowWeekList);
		return nowWeekList;
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
		leftBegin=DateParser.getFirstDayOfWeek();
		leftEnd=DateParser.getLastDayOfWeek();
		rightBegin=DateParser.getFirstDayOfWeek();
		rightEnd=DateParser.getLastDayOfWeek();
		middleBegin=DateParser.getFirstDayOfWeek();
		middleEnd=DateParser.getLastDayOfWeek();
		leftBegin.add(Calendar.DAY_OF_YEAR, -7);
		leftEnd.add(Calendar.DAY_OF_YEAR, -7);
		rightBegin.add(Calendar.DAY_OF_YEAR, 7);
		rightEnd.add(Calendar.DAY_OF_YEAR, 7);
	}
	
	private void setCalendars(int pageScrolledTo){
		switch(pageScrolledTo){
		case PAGE_LEFT:
			leftBegin.add(Calendar.DAY_OF_YEAR, 7);
			leftEnd.add(Calendar.DAY_OF_YEAR, 7);
			middleBegin.add(Calendar.DAY_OF_YEAR, 7);
			middleEnd.add(Calendar.DAY_OF_YEAR, 7);
			rightBegin.add(Calendar.DAY_OF_YEAR, 7);
			rightEnd.add(Calendar.DAY_OF_YEAR, 7);
			break;
		case PAGE_RIGHT:
			leftBegin.add(Calendar.DAY_OF_YEAR, -7);
			leftEnd.add(Calendar.DAY_OF_YEAR, -7);
			middleBegin.add(Calendar.DAY_OF_YEAR, -7);
			middleEnd.add(Calendar.DAY_OF_YEAR, -7);
			rightBegin.add(Calendar.DAY_OF_YEAR, -7);
			rightEnd.add(Calendar.DAY_OF_YEAR, -7);
			break;
		}
	}
	
	public void setContent(int pageScrolledTo){
		setCalendars(pageScrolledTo);
		Bundle args=null;
		switch(pageScrolledTo){
		case PAGE_RIGHT:
			leftListAdapter.setData(middleListAdapter.getData());
			middleListAdapter.setData(rightListAdapter.getData());
			args=ApiHelper.getInstance(context).getSchedule(rightBegin, rightEnd);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, rightCallbacks);
			break;
		case PAGE_LEFT:
			rightListAdapter.setData(middleListAdapter.getData());
			middleListAdapter.setData(leftListAdapter.getData());
			args=ApiHelper.getInstance(context).getSchedule(leftBegin, leftEnd);
			fragment.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, leftCallbacks);
			break;
		}
	}
	
	private class PageNetworkLoaderCallbacks implements LoaderCallbacks<HttpRequestResult>{
		
		private NowWeekListAdapter adapter;
		private EventFactory factory;
		
		public PageNetworkLoaderCallbacks(NowWeekListAdapter adapter) {
			super();
			this.adapter=adapter;
			this.factory=new EventFactory(fragment);
		}
		
		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
			return new NetworkLoader(context, HttpMethod.Get, args);
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult result) {
			if(result.getResponseCode()==0){
				List<Event> events=factory.createObjects(result.getStrResponseCon());
				adapter.setRawData(events);
			}
			else{
				ExceptionToast.show(context, result.getResponseCode());
			}
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		}
		
	}
	
}
