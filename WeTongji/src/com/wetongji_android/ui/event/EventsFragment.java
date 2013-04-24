package com.wetongji_android.ui.event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foound.widget.AmazingListView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.support.extras.AndroidBaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.EventFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>{
	
	private static final String NEXTPAGER = "next_pager";
	
	private AmazingListView mListActivity;
	private EventsListAdapter mAdapter;
	private List<Activity> mEventsList;
	private DbHelper mDbHelper;
	private AndroidBaseDaoImpl<Activity, Integer> mEventDao;
	private EventFactory mEventFactory;
	private SharedPreferences sharedPref;
	
	private int mNextPage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_events, null);		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mDbHelper = OpenHelperManager.getHelper(getActivity(), DbHelper.class);
		try {
			mEventDao = mDbHelper.getActDao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			mEventsList = queryEvents();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(1, "", "", false);
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER, args, this);
		
		//createEventList(12);
		mListActivity = (AmazingListView)getActivity().findViewById(R.id.lst_events);
		mAdapter = new EventsListAdapter(this);
		mListActivity.setAdapter(mAdapter);
		
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}
	
	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		
		if(result.getResponseCode()==0){
			
			try {
				createAcitvityUpdates(result.getStrResponseCon());
			} catch (Exception e) {
				Log.d("createActivityUpdates Error", e.getMessage());
			}
			
			mAdapter.resetEventLst();
			mAdapter.notifyDataSetChanged();
			mAdapter.nextPage();
		}
		else{
			
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
	public List<Activity> getEvents() {
		return mEventsList;
	}
	
	private List<Activity> queryEvents() throws SQLException {
		QueryBuilder<Activity, Integer> queryBuilder = mEventDao.queryBuilder();
		/*if(sortPreference.equals(SORT_LIKE_UNEXPIRED)
				||sortPreference.equals(SORT_LIKE_ALL))
			qb.orderBy("Like", false);
		else if(sortPreference.equals(SORT_TIME))
			qb.orderBy("Begin", true);
		else
			qb.orderBy("Id", false);

		Where<Activity, Integer> where=qb.where();
		int count=0;
		if(isAcademicSelected){
			where.eq("Channel_Id", 1);
			count++;
		}
		if(isCompetitionSelected){
			where.eq("Channel_Id", 2);
			count++;
		}
		if(isEntertainmentSelected){
			where.eq("Channel_Id", 3);
			count++;
		}
		if(isEmploymentSelected){
			where.eq("Channel_Id", 4);
			count++;
		}

		if(count>1)
			where.or(count);
		if(isExpiredFiltered){
			where.ge("End", WTDateParser.buildCurrentDateAndTime());
			where.and(2);
		}
*/
		queryBuilder.orderBy("Id", false);
		Where<Activity, Integer> where = queryBuilder.where();
		where.eq("Channel_Id", 1);
		where.eq("Channel_Id", 2);
		where.eq("Channel_Id", 3);
		where.eq("Channel_Id", 4);
		where.or(4);
		
		return mEventDao.query(queryBuilder.prepare());
	}
	
	private void createAcitvityUpdates(String jsonStr) throws Exception{
		//最外层的JSONObject
		JSONObject jsonOuter=new JSONObject(jsonStr);
		//设置当前页
		mNextPage = jsonOuter.getInt("NextPager");
		Editor editor = sharedPref.edit();
		editor.putInt(NEXTPAGER, mNextPage);
		editor.commit();


		//内层的JSONArray
		JSONArray jsonArray=jsonOuter.getJSONArray("Activities");
		//每条新闻的JSONObject
		JSONObject jsonObject = new JSONObject();
		

		//依次读入activity并写入
		for(int i = 0; i != jsonArray.length(); ++i){
			jsonObject=jsonArray.getJSONObject(i);
			mEventFactory.create(jsonObject);
			Activity activity = mEventFactory.getActivity();
			mEventsList.add(activity);
			
		}
		
		mEventDao.callBatchTasks(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for(Activity activity:mEventsList)
					mEventDao.createOrUpdate(activity);
				return null;
			}

		});
		
		//mAdapter.notifyDataSetChanged();
	}
	
	private void createEventList(int count) {
		mEventsList = new ArrayList<Activity>();
		Activity activity;
		for(int i = 0; i < count; i++) {
			activity = new Activity();
			
			if(i % 2 == 0) {
				activity.setTitle("Carmeq & Volkswagen Design Workshop " + count);
			}else {
				activity.setTitle("PSA Day " + count);
			}
			activity.setBegin("10:00");
			activity.setEnd("12:00");
			activity.setLocation("Center of Digital Innovation");
			mEventsList.add(activity);
		}
		
	}


	
}
