package com.wetongji_android.ui.now;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Event;
import com.wetongji_android.factory.ScheduleFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.course.CourseDetailActivity;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class NowPagerAdapter extends PagerAdapter {

	public static final int PAGE_LEFT = 0;
	public static final int PAGE_RIGHT = 2;
	public static final int PAGE_MIDDILE = 1;

	private final LayoutInflater inflater;
	private final Fragment fragment;
	private final ViewPager viewPager;
	private NowWeekListAdapter listAdapter;
	private AmazingListView nowWeekList;
	private PageNetworkLoaderCallbacks callbacks;
	private final Context context;
	private int weekNum;
	private boolean scrollToNow;

	public NowPagerAdapter(Fragment fragment, ViewPager viewPager) {
		super();
		this.fragment = fragment;
		context = fragment.getActivity();
		inflater = fragment.getActivity().getLayoutInflater();
		this.viewPager = viewPager;

		weekNum = DateParser.getWeekNumber();
		listAdapter = new NowWeekListAdapter(fragment, weekNum);
		callbacks = new PageNetworkLoaderCallbacks();

		loadScheduleFromServer();

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		switch (position) {
		case PAGE_LEFT:
		case PAGE_RIGHT:
			View view = inflater.inflate(R.layout.page_now_loading, null);
			container.addView(view);
			return view;
		default:
			nowWeekList = (AmazingListView) inflater.inflate(
					R.layout.page_now_week, null);
			
			listAdapter.setWeekNumber(weekNum);
			nowWeekList.setAdapter(listAdapter);
			View pinnedHeader = LayoutInflater.from(context).inflate(
					R.layout.section_bar_now, nowWeekList, false);
			nowWeekList.setPinnedHeaderView(pinnedHeader);
			nowWeekList.setOnItemClickListener(new ListItemClickListener());
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
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	private void setCalendars(int pageScrolledTo) {
		switch (pageScrolledTo) {
		case PAGE_LEFT:
			if (weekNum == 0) {
				weekNum = -19;
				break;
			} else if (weekNum < 0) {
				weekNum++;
				break;
			} else if (weekNum > 0) {
				weekNum--;
				break;
			}
			break;
		case PAGE_RIGHT:
			if (weekNum == -19) {
				weekNum = 0;
				break;
			} else if (weekNum < 0) {
				weekNum--;
				break;
			} else if (weekNum == 20) {
				break;
			}else if (weekNum >= 0) {
				weekNum++;
				break;
			} 
			break;
		}
	}

	public void setContent(int pageScrolledTo) {
		if (pageScrolledTo != PAGE_MIDDILE) {
			setCalendars(pageScrolledTo);
			
			// load from db
			listAdapter.setWeekNumber(weekNum);
			
			loadScheduleFromServer();
		}
	}

	private void loadScheduleFromServer() {
		if (WTUtility.isConnect(context)) {
			Bundle args = ApiHelper.getInstance(context).getSchedule(weekNum);
			fragment.getLoaderManager()
			.restartLoader(WTApplication.NETWORK_LOADER_1, args, callbacks).forceLoad();
		} else {
			((NowFragment) fragment).showCourse();
		}
		
	}

	private class PageNetworkLoaderCallbacks implements
			LoaderCallbacks<HttpRequestResult> {

		private final ScheduleFactory scheduleFactory;

		public PageNetworkLoaderCallbacks() {
			super();
			this.scheduleFactory = new ScheduleFactory(fragment);
		}

		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
			return new NetworkLoader(context, HttpMethod.Get, args);
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult result) {
			fragment.getLoaderManager().destroyLoader(
					WTApplication.NETWORK_LOADER_1);
			if (result.getResponseCode() == 0) {
				List<Event> events = new ArrayList<Event>();
				events = new ArrayList<Event>(
						scheduleFactory.createSchedule(weekNum,
								result.getStrResponseCon()));
				
				listAdapter.setRawData(events);
				
				if (scrollToNow) {
					nowWeekList.smoothScrollToPosition(listAdapter.getNowPosition());
					scrollToNow = false;
				}
			} else {
				ExceptionToast.show(context, result.getResponseCode());
			}
			if (viewPager.getCurrentItem() != PAGE_MIDDILE) {
				viewPager.setCurrentItem(PAGE_MIDDILE, false);
			}
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		}

	}

	private class ListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			Event event = listAdapter.getItem(position);
			Bundle bundle = new Bundle();
			Intent intent = null;
			if (event instanceof Activity) {
				intent = new Intent(context, EventDetailActivity.class);
				bundle.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY, event);
				intent.putExtras(bundle);
			} else if (event instanceof Course) {
				intent = new Intent(context, CourseDetailActivity.class);
				bundle.putParcelable(CourseDetailActivity.BUNDLE_COURSE, event);
				intent.putExtras(bundle);
			}

			fragment.startActivity(intent);
		}

	}
	
	public void returnToNow() {
		weekNum = DateParser.getWeekNumber();
		listAdapter.setWeekNumber(weekNum);
		Bundle args = ApiHelper.getInstance(context).getSchedule(weekNum);
		fragment.getLoaderManager()
				.initLoader(WTApplication.NETWORK_LOADER_1, args, callbacks)
				.forceLoad();
	}

}
