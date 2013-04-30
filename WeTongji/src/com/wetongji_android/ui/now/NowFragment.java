package com.wetongji_android.ui.now;

import java.util.Calendar;

import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link NowFragment#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
public class NowFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>, 
OnPageChangeListener{
	
	private View view;
	private int selectedPage;
	//private TextView tvWeekNumber;
	private TextView tvNowTime;
	private ViewPager vpWeeks;
	private NowPagerAdapter adapter;
	//private ApiHelper apiHelper;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment NowFragment.
	 */
	public static NowFragment newInstance() {
		NowFragment fragment = new NowFragment();
		return fragment;
	}

	public NowFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		selectedPage=0;
		adapter=new NowPagerAdapter(this);
		//apiHelper=ApiHelper.getInstance(getActivity());
		//Bundle args=apiHelper.getTimetable();
		//getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view=inflater.inflate(R.layout.fragment_now, container, false);
		//tvWeekNumber=(TextView) view.findViewById(R.id.tv_now_week_number);
		vpWeeks=(ViewPager) view.findViewById(R.id.vp_weeks);
		vpWeeks.setAdapter(adapter);
		vpWeeks.setOnPageChangeListener(this);
		vpWeeks.setCurrentItem(NowPagerAdapter.PAGE_MIDDILE, false);
		return view;
	}
	
	public void setNowTime(String dateFromServer){
		if(tvNowTime==null){
			tvNowTime=(TextView) view.findViewById(R.id.tv_now_time);
		}
		Calendar cal=DateParser.parseDateAndTime(dateFromServer);
		@SuppressWarnings("deprecation")
		String time=DateUtils.formatDateTime(getActivity(), cal.getTimeInMillis(),
				DateUtils.FORMAT_24HOUR|DateUtils.FORMAT_SHOW_TIME);
		tvNowTime.setText(time);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if(result.getResponseCode()==0){
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if(state==ViewPager.SCROLL_STATE_IDLE){
			adapter.setContent(selectedPage);
			vpWeeks.setCurrentItem(NowPagerAdapter.PAGE_MIDDILE, false);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		selectedPage=position;
		Log.v("viewpager", "selecte page="+selectedPage);
	}
	
}
