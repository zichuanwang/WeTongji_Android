package com.wetongji_android.ui.now;

import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.HttpRequestResult;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link NowFragment#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
public class NowFragment extends SherlockFragment implements LoaderCallbacks<HttpRequestResult> 
{
	
	private View view;
	private int selectedPage;
	private int weekNumber;
	private TextView tvWeekNumber;
	private TextView tvNowTime;
	private ViewPager vpWeeks;
	private NowPagerAdapter adapter;
	//private ApiHelper apiHelper;
	
	private Activity mActivity;
	
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
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.text_now_indicator);

		
		selectedPage=0;
		weekNumber=8;
		//apiHelper=ApiHelper.getInstance(mActivity);
		//Bundle args=apiHelper.getTimetable();
		//getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if(view==null){
			view=inflater.inflate(R.layout.fragment_now, container, false);
			tvWeekNumber=(TextView) view.findViewById(R.id.tv_now_week_number);
			tvWeekNumber.setText(weekNumber+"");
			vpWeeks=(ViewPager) view.findViewById(R.id.vp_weeks);
			adapter=new NowPagerAdapter(this, vpWeeks);
			vpWeeks.setAdapter(adapter);
			vpWeeks.setOnPageChangeListener(new PageChangeListener());
			vpWeeks.setCurrentItem(NowPagerAdapter.PAGE_LEFT, false);
			ImageButton btnNowNext=(ImageButton) view.findViewById(R.id.btn_now_next);
			ImageButton btnNowPre=(ImageButton) view.findViewById(R.id.btn_now_previous);
			ButtonClickListener btnListener=new ButtonClickListener();
			btnNowNext.setOnClickListener(btnListener);
			btnNowPre.setOnClickListener(btnListener);
		}
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		mActivity = activity;
	}

	public void setNowTime(Date dateFromServer){
		if(tvNowTime==null){
			tvNowTime=(TextView) view.findViewById(R.id.tv_now_time);
		}
		Calendar cal=DateParser.parseDateAndTime(dateFromServer);
		@SuppressWarnings("deprecation")
		String time=DateUtils.formatDateTime(mActivity, cal.getTimeInMillis(),
				DateUtils.FORMAT_24HOUR|DateUtils.FORMAT_SHOW_TIME);
		tvNowTime.setText(time);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
		return new NetworkLoader(mActivity, HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if(result.getResponseCode()==0){
		}
		else{
			ExceptionToast.show(mActivity, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	private class PageChangeListener implements OnPageChangeListener{
		
		@Override
		public void onPageScrollStateChanged(int state) {
			if(state==ViewPager.SCROLL_STATE_IDLE){
				adapter.setContent(selectedPage);
			}
		}
	
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	
		@Override
		public void onPageSelected(int position) {
			selectedPage=position;
			if(position==NowPagerAdapter.PAGE_LEFT){
				weekNumber--;
			}
			else if(position==NowPagerAdapter.PAGE_RIGHT){
				weekNumber++;
			}
			tvWeekNumber.setText(weekNumber+"");
		}
	
	}
	
	private class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_now_next:
				vpWeeks.setCurrentItem(NowPagerAdapter.PAGE_RIGHT, true);
				break;
			case R.id.btn_now_previous:
				vpWeeks.setCurrentItem(NowPagerAdapter.PAGE_LEFT, true);
			default:
				break;
			}
		}
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		getSherlockActivity().getSupportActionBar()
				.setDisplayShowCustomEnabled(true);
		getSherlockActivity().getSupportActionBar().setCustomView(
				R.layout.actionbar_today);
		getActivity().findViewById(R.id.notification_button)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						NotificationHandler.getInstance().finish();
						if (WTApplication.getInstance().hasAccount) {
							((MainActivity) getActivity()).showRightMenu();
						} else {
							Toast.makeText(
									getActivity(),
									getResources().getText(
											R.string.no_account_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.notification_button) {
//			if (WTApplication.getInstance().hasAccount) {
//				((MainActivity)mActivity).showRightMenu();
//			} else {
//				Toast.makeText(mActivity, getResources().getText(R.string.no_account_error),
//						Toast.LENGTH_SHORT).show();
//			}
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	public void onResume() {
		super.onResume();
		NotificationHandler.getInstance().checkNotification();
	}
	
	
}
