package com.wetongji_android.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;


public class EventsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>,
OnMenuItemSelectedListener, OnScrollListener{
	
	public static final String BUNDLE_KEY_ACTIVITY = "bundle_key_activity";
	
	public ListView mListActivity;
	public EndlessEventListAdapter mAdapter;
	private int mCurrentPage = 0;
	private boolean isRefresh = false;
	private ActivityFactory mFactory;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_events, null);
		
		mListActivity = (ListView) view.findViewById(R.id.lst_events);
		mAdapter = new EndlessEventListAdapter(this, mListActivity);
		mListActivity.setAdapter(mAdapter);
		mListActivity.setOnItemClickListener(onItemClickListener);
		
		AQuery aq = WTApplication.getInstance().getAq(getActivity());
		aq.id(mListActivity).scrolled(this);
		mAdapter.loadDataFormDB();
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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

			WTUtility.log("data", "" + activities.get(0).getTitle());
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
		}
		
	};
	
	public void refreshData() {
		isRefresh = true;
		
		// scroll the listview to top
		mListActivity .setSelection(0);
		
		mAdapter.clear();
		mAdapter.setIsLoadingData(true);
		mCurrentPage = 0;
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(1, 15, ApiHelper.API_ARGS_SORT_BY_ID_DESC, true);
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	private void loadMoreData(int page) {
		isRefresh = false;
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(page, 15, ApiHelper.API_ARGS_SORT_BY_ID_DESC, true);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
		WTUtility.log("data", "load page:" + args.getString("P"));
	}
	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getTitle().equals("Refresh")) {
			return true;
		}
		return false;
	}
	
	public Pair<Integer, List<Activity>> createActivities(String json) {
		List<Activity> list = new ArrayList<Activity>();
		String jsonStr = null;
		JSONObject outer = null;
		int nextPager = 0;
		try {
			outer = new JSONObject(json);
			nextPager=outer.getInt("NextPager");
			jsonStr = outer.getString("Activities");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		list.clear();
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JSONArray array;
		try {
			array = new JSONArray(jsonStr);
			for(int i=0;i!=array.length();i++){
				Activity t=gson.fromJson(array.getString(i).toString(), Activity.class);
				list.add(t);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Pair<Integer, List<Activity>> pair = new Pair<Integer, List<Activity>>(nextPager, list);
		
		return pair;
	}

	

	@Override
	public void onPause() {
		super.onPause();
		getLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if(mAdapter.shouldRequestNextPage(arg1, arg2, arg3)) {
			WTUtility.log("onNextPage", "loadMoreData: " + mCurrentPage + 1);
			loadMoreData(mCurrentPage + 1);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
	
}
