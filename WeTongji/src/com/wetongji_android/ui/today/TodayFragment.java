package com.wetongji_android.ui.today;

import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Banner;
import com.wetongji_android.data.Event;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.TodayFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.QueryHelper;
import com.wetongji_android.util.data.event.EventLoader;
import com.wetongji_android.util.data.event.EventUtil;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.util.net.HttpUtil;

public class TodayFragment extends Fragment {

	private View view;
	private Context context;
	private ViewPager vpBanner;
	private UnderlinePageIndicator indicator;
	private GridView gvNews, gvEvents, gvFeatures;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_today, container, false);

			setTodayGrids(view);
			setTodaySectionTitles(view);
			Bundle args = QueryHelper.getEventQueryArgs(Calendar.getInstance());
			getLoaderManager().initLoader(WTApplication.EVENT_LOADER, args,
					new DbLoaderCallbacks()).forceLoad();
		}
		return view;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onAttach(android.app.Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		//getActivity().invalidateOptionsMenu();
	}

	private void setTodayBanner(View view, TodayBannerPagerAdapter bannerAdapter) {
		view.findViewById(R.id.pb_today_banner).setVisibility(View.GONE);
		view.findViewById(R.id.pc_banner).setVisibility(View.VISIBLE);
		vpBanner = (ViewPager) view.findViewById(R.id.vp_banner);
		vpBanner.setAdapter(bannerAdapter);
		vpBanner.setOffscreenPageLimit(bannerAdapter.getCount());
		vpBanner.setClipChildren(false);
		indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.vp_indicator_today);
		indicator.setViewPager(vpBanner);
		indicator.setFades(false);
	}

	private void setTodaySectionTitles(View view) {
		TextView tvNews = (TextView) view
				.findViewById(R.id.tv_today_information);
		TextView tvEvents = (TextView) view
				.findViewById(R.id.tv_today_activities);
		TextView tvFeatures = (TextView) view
				.findViewById(R.id.tv_today_features);
		ClickListener listener = new ClickListener();
		tvNews.setOnClickListener(listener);
		tvEvents.setOnClickListener(listener);
		tvFeatures.setOnClickListener(listener);
	}

	private void setTodayGrids(View view) {
		gvNews = (GridView) view.findViewById(R.id.gv_today_information);
		gvEvents = (GridView) view.findViewById(R.id.gv_today_activities);
		gvFeatures = (GridView) view.findViewById(R.id.gv_today_features);
		Bundle bundle = ApiHelper.getInstance(context).getHome();
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				bundle, new NetwordLoaderCallbacks());
	}

	private void setTodayNowContent(Event event) {
		view.findViewById(R.id.fl_now_today).setVisibility(View.VISIBLE);
		TextView tvNowTitle = (TextView) view
				.findViewById(R.id.tv_today_now_title);
		TextView tvNowTime = (TextView) view
				.findViewById(R.id.tv_today_now_time);
		TextView tvNowLocation = (TextView) view
				.findViewById(R.id.tv_today_now_location);
		ImageView ivNowThumb = (ImageView) view
				.findViewById(R.id.iv_today_now_thumb);

		tvNowTitle.setText(event.getTitle());
		tvNowTime.setText(EventUtil.getEventDisplayTime(event, context));
		tvNowLocation.setText(event.getLocation());

		if (event instanceof Activity) {
			// Set thumb nails
			String strUrl = ((Activity) event).getImage();
			AQuery aq = new AQuery(context);
			if (!strUrl.equals(WTApplication.MISSING_IMAGE_URL)) {
				aq.id(ivNowThumb).image(HttpUtil.replaceURL(strUrl), true,
						true, 300,
						R.drawable.event_list_thumbnail_place_holder, null,
						AQuery.FADE_IN_NETWORK, 1.33f);
			}
		} else {
			ivNowThumb.setVisibility(View.GONE);
		}

	}

	private class NetwordLoaderCallbacks implements
			LoaderCallbacks<HttpRequestResult> {

		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
			return new NetworkLoader(context, HttpMethod.Get, args);
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult result) {
			if (result.getResponseCode() == 0) {
				TodayFactory factory = new TodayFactory(TodayFragment.this);
				String strResult = result.getStrResponseCon();
				List<Banner> banners = factory.createBanners(strResult);
				List<Activity> activities = factory.createActivities(strResult);
				List<Information> infomation = factory.createInfos(strResult);
				List<Object> features = factory.createFeatures(strResult);

				TodayBannerPagerAdapter bannerAdapter = new TodayBannerPagerAdapter(
						banners, context);
				setTodayBanner(view, bannerAdapter);

				TodayGridNewsAdapter newsAdapter = new TodayGridNewsAdapter(
						context, infomation);
				view.findViewById(R.id.pb_today_information).setVisibility(
						View.GONE);
				gvNews.setAdapter(newsAdapter);
				gvNews.setVisibility(View.VISIBLE);

				TodayGridEventAdapter eventAdapter = new TodayGridEventAdapter(
						context, activities);
				view.findViewById(R.id.pb_today_activities).setVisibility(
						View.GONE);
				gvEvents.setAdapter(eventAdapter);
				gvEvents.setOnItemClickListener(eventAdapter);
				gvEvents.setVisibility(View.VISIBLE);

				TodayGridFeatureAdapter featureAdapter = new TodayGridFeatureAdapter(
						context, features);
				view.findViewById(R.id.pb_today_features).setVisibility(
						View.GONE);
				gvFeatures.setAdapter(featureAdapter);
				gvFeatures.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		}

	}

	private class DbLoaderCallbacks implements LoaderCallbacks<List<Event>> {

		@Override
		public Loader<List<Event>> onCreateLoader(int arg0, Bundle arg1) {
			return new EventLoader(context, arg1);
		}

		@Override
		public void onLoadFinished(Loader<List<Event>> arg0, List<Event> events) {
			if (events != null && !events.isEmpty()) {
				setTodayNowContent(events.get(0));
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Event>> arg0) {
		}

	}

	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_today_activities:
				((MainActivity) context).switchContent(new EventsFragment());
				break;
			}
		}

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.v("menu", "create");
		// TODO Auto-generated method stub
		//inflater.inflate(R.menu.menu_main, menu);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
}