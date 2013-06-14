package com.wetongji_android.ui.event;

import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.ActivityList;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.QueryHelper;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;


public class EventsFragment extends SherlockFragment implements LoaderCallbacks<HttpRequestResult>,
OnScrollListener{
	
	private boolean isFirstTimeStartFlag = true;
	private final int FIRST_TIME_START = 0; //when activity is first time start
	private final int SCREEN_ROTATE = 1;    //when activity is destroyed and recreated because a configuration change, see setRetainInstance(boolean retain)
	private final int ACTIVITY_DESTROY_AND_CREATE = 2; 
	
	public static final String BUNDLE_KEY_ACTIVITY = "bundle_key_activity";
	public static final String BUNDLE_KEY_ACTIVITY_LIST = "bundle_key_activity_list";
	public static final String BUNDLE_KEY_LOAD_FROM_DB_FINISHED = "bundle_key_load_from_db_finished";
	public static final String SHARE_PREFERENCE_EVENT = "EventSettings";
	public static final String PREFERENCE_EVENT_EXPIRE = "EventExpire";
	public static final String PREFERENCE_EVENT_SORT = "EventSort";
	public static final String PREFERENCE_EVENT_TYPE = "EventType";
	
	public ListView mListActivity;
	public EventListAdapter mAdapter;
	private int mCurrentPage = 0;
	private boolean isRefresh = false;
	private ActivityFactory mFactory;
	
	/** Sort preferences by default**/
	private boolean mExpire = true;
	private int mSortType = 1;
	private int mSelectedType = 15;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, null);
		
		mListActivity = (ListView) view.findViewById(R.id.lst_events);
		mAdapter = new EventListAdapter(this, mListActivity);
		mListActivity.setAdapter(mAdapter);
		mListActivity.setOnItemClickListener(onItemClickListener);
		AQuery aq = WTApplication.getInstance().getAq(getActivity());
		aq.id(mListActivity).scrolled(this);
		
		return view;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		switch(getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			mAdapter.loadDataFromDB(getQueryArgs());
			break;
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			ActivityList activityList = (ActivityList) savedInstanceState
				.getSerializable(BUNDLE_KEY_ACTIVITY_LIST);
			mAdapter.addAll(activityList.getList());
			break;
		}
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// save activity list for next time resume;
		ActivityList activityList = new ActivityList();
		activityList.setItems(mAdapter.getData());
		outState.putSerializable(BUNDLE_KEY_ACTIVITY_LIST, activityList);
	}
	
	@Override
	public Loader<HttpRequestResult> onCreateLoader(int id, Bundle args) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, args);
	}
	
	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		
		if(result.getResponseCode()==0){
			if(mFactory==null){
				mFactory=new ActivityFactory(this);
			}
			
			List<Activity> activities =
					mFactory.createObjects(result.getStrResponseCon(), isRefresh);
			
			if(mCurrentPage == 0) {
				mAdapter.getData().clear();
			}
			
			mAdapter.addAll(activities);

			mCurrentPage++;
			mAdapter.setIsLoadingData(false);
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			
			Intent intent = new Intent(getActivity(), EventDetailActivity.class);
			Activity activity = mAdapter.getItem(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable(BUNDLE_KEY_ACTIVITY, activity);
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		}
		
	};
	
	public void refreshData() {
		isRefresh = true;
		// scroll the listview to top
		
		mAdapter.clear();
		mAdapter.setIsLoadingData(true);
		mListActivity .setSelection(0);
		mCurrentPage = 0;
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(1, mSelectedType, mSortType, mExpire);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	private void loadMoreData(int page) {
		isRefresh = false;
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(page, mSelectedType, mSortType, mExpire);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	

	@Override
	public void onPause() {
		super.onPause();
		getLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
		getLoaderManager().destroyLoader(WTApplication.ACTIVITIES_LOADER);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if(mAdapter.shouldRequestNextPage(arg1, arg2, arg3)) {
			loadMoreData(mCurrentPage + 1);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
	
	private int getCurrentState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            isFirstTimeStartFlag = false;
            return ACTIVITY_DESTROY_AND_CREATE;
        }


        if (!isFirstTimeStartFlag) {
            return SCREEN_ROTATE;
        }

        isFirstTimeStartFlag = false;
        return FIRST_TIME_START;
    }

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,
			com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.menu_eventlist, menu);
		
		readPreference();
		setMenuStatus(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_eventlist_reload:
			refreshData();
			break;
		case R.id.cb_event_sort_publish_time_reverse_order:
			item.setChecked(true);
			mSortType = ApiHelper.API_ARGS_SORT_BY_PUBLISH_DESC;
			break;
		case R.id.cb_activity_sort_like_all:
			item.setChecked(true);
			mSortType = ApiHelper.API_ARGS_SORT_BY_LIKE_DESC;
			break;
		case R.id.cb_event_sort_publish_time_order:
			item.setChecked(true);
			mSortType = ApiHelper.API_ARGS_SORT_BY_PUBLISH_ASC;
			break;
		case R.id.cb_academic:
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mSelectedType += ApiHelper.API_ARGS_CHANNEL_ACADEMIC_MASK;
			} else {
				mSelectedType -= ApiHelper.API_ARGS_CHANNEL_ACADEMIC_MASK;
			}
			break;
		case R.id.cb_competition:
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mSelectedType += ApiHelper.API_ARGS_CHANNEL_COMPETITION_MASK;
			} else {
				mSelectedType -= ApiHelper.API_ARGS_CHANNEL_COMPETITION_MASK;
			}
			break;
		case R.id.cb_entertainment:
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mSelectedType += ApiHelper.API_ARGS_CHANNEL_ENTERTAINMENT_MASK;
			} else {
				mSelectedType -= ApiHelper.API_ARGS_CHANNEL_ENTERTAINMENT_MASK;
			}
			break;
		case R.id.cb_employ:
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				mSelectedType += ApiHelper.API_ARGS_CHANNEL_EMPLOYMENT_MASK;
			} else {
				mSelectedType -= ApiHelper.API_ARGS_CHANNEL_EMPLOYMENT_MASK;
			}
			break;
		case R.id.cb_event_expired:
			item.setChecked(!item.isChecked());
			mExpire = !item.isChecked();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		writePreference();
		return true;
	}
	
	/*
	 * Read preference and by default the sort type is by
	 * publish time descending, the expire is true and also
	 * we select all the type
	 */
	private void readPreference() 
	{
		SharedPreferences sp = 
				getActivity().getSharedPreferences(SHARE_PREFERENCE_EVENT, Context.MODE_PRIVATE);
		mExpire = sp.getBoolean(PREFERENCE_EVENT_EXPIRE, true);
		mSortType = sp.getInt(PREFERENCE_EVENT_SORT, ApiHelper.API_ARGS_SORT_BY_PUBLISH_DESC);
		mSelectedType = sp.getInt(PREFERENCE_EVENT_TYPE, 15);
	}
	
	private void writePreference() {
		SharedPreferences.Editor edit = 
				getActivity().getSharedPreferences(SHARE_PREFERENCE_EVENT, Context.MODE_PRIVATE).edit();
		edit.putBoolean(PREFERENCE_EVENT_EXPIRE, mExpire);
		edit.putInt(PREFERENCE_EVENT_SORT, mSortType);
		edit.putInt(PREFERENCE_EVENT_TYPE, mSelectedType);
		
		edit.commit();
	}
	
	private void setMenuStatus(Menu menu) {
		if (mSortType == ApiHelper.API_ARGS_SORT_BY_PUBLISH_DESC) {
			menu.getItem(1).getSubMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
		} else if (mSortType == ApiHelper.API_ARGS_SORT_BY_LIKE_DESC) {
			menu.getItem(1).getSubMenu().getItem(0).getSubMenu().getItem(2).setChecked(true);
		} else if (mSortType == ApiHelper.API_ARGS_SORT_BY_PUBLISH_ASC) {
			menu.getItem(1).getSubMenu().getItem(0).getSubMenu().getItem(1).setChecked(true);
		}
		
		menu.getItem(1).getSubMenu().getItem(1).getSubMenu().getItem(0).setChecked(
				(mSelectedType & ApiHelper.API_ARGS_CHANNEL_ACADEMIC_MASK) != 0);
		menu.getItem(1).getSubMenu().getItem(1).getSubMenu().getItem(1).setChecked(
				(mSelectedType & ApiHelper.API_ARGS_CHANNEL_COMPETITION_MASK) != 0);
		menu.getItem(1).getSubMenu().getItem(1).getSubMenu().getItem(2).setChecked(
				(mSelectedType & ApiHelper.API_ARGS_CHANNEL_ENTERTAINMENT_MASK) != 0);
		menu.getItem(1).getSubMenu().getItem(1).getSubMenu().getItem(3).setChecked(
				(mSelectedType & ApiHelper.API_ARGS_CHANNEL_EMPLOYMENT_MASK) != 0);

		menu.getItem(1).getSubMenu().getItem(2).setChecked(!mExpire);
	}
	
	private Bundle getQueryArgs() {
		String orderBy = QueryHelper.ARGS_ORDER_BY_PUBLISH_TIME;
		boolean assending = false;
		
		if (mSortType == ApiHelper.API_ARGS_SORT_BY_PUBLISH_DESC) {
			orderBy = QueryHelper.ARGS_ORDER_BY_PUBLISH_TIME;
			assending = false;
		} else if (mSortType == ApiHelper.API_ARGS_SORT_BY_LIKE_DESC) {
			orderBy = QueryHelper.ARGS_ORDER_BY_LIKE;
			assending = false;
		} else if (mSortType == ApiHelper.API_ARGS_SORT_BY_PUBLISH_ASC) {
			orderBy = QueryHelper.ARGS_ORDER_BY_PUBLISH_TIME;
			assending = true;
		}
		
		boolean hasCH1 = (mSelectedType & ApiHelper.API_ARGS_CHANNEL_ACADEMIC_MASK) != 0;
		boolean hasCH2 = (mSelectedType & ApiHelper.API_ARGS_CHANNEL_COMPETITION_MASK) != 0;
		boolean hasCH3 = (mSelectedType & ApiHelper.API_ARGS_CHANNEL_ENTERTAINMENT_MASK) != 0;
		boolean hasCH4 = (mSelectedType & ApiHelper.API_ARGS_CHANNEL_EMPLOYMENT_MASK) != 0;
		
		Bundle b = QueryHelper.getActivitiesQueryArgs(orderBy, assending, mExpire,
				hasCH1, hasCH2, hasCH3, hasCH4);
		return b;
	}
}
