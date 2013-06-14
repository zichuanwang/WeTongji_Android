package com.wetongji_android.ui.people;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;
import com.wetongji_android.factory.PersonFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class PeopleListFragment extends WTBaseFragment implements LoaderCallbacks<HttpRequestResult>,
OnScrollListener{

	private ListView mLvPeople;
	private PeopleListAdapter mAdapter;
	private int mCurrentPage = 0;
	private boolean isRefresh = false;
	private PersonFactory mFactory;
	private int mNextPager = 1;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		switch(getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			mAdapter.loadDataFromDB();
			break;
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			/*ActivityList activityList = (ActivityList) savedInstanceState
				.getSerializable(BUNDLE_KEY_ACTIVITY_LIST);
			mAdapter.addAll(activityList.getList());*/
			break;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_people, null);
		mLvPeople = (ListView) view.findViewById(R.id.lst_people);
		mAdapter = new PeopleListAdapter(this, mLvPeople);
		mLvPeople.setAdapter(mAdapter);
		mLvPeople.setOnItemClickListener(mOnItemClickListener);
		AQuery aq = WTApplication.getInstance().getAq(getActivity());
		aq.id(mLvPeople).scrolled(this);
		return view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
		getLoaderManager().destroyLoader(WTApplication.PERSON_LOADER);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if (result.getResponseCode() == 0){
			
			if(mFactory == null){
				mFactory = new PersonFactory(this);
			}
			
			JSONObject json = null;
			String strPeople = null;
			try {
				json = new JSONObject(result.getStrResponseCon());
				strPeople = json.getString("People");
				mNextPager = json.getInt("NextPager");
			} catch (JSONException e) {
			}
			
			List<Person> people = 
					mFactory.createObjects(strPeople, isRefresh);
			
			if(mCurrentPage == 0) {
				mAdapter.getData().clear();
			}
			
			mAdapter.addAll(people);

			mCurrentPage++;
			mAdapter.setIsLoadingData(false);
			
		} else {
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
		
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if(mAdapter.shouldRequestNextPage(arg1, arg2, arg3) &&
				mNextPager > 0) {
			loadMoreData(mCurrentPage + 1);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
	
	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,
			com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		menu.clear();
		inflater.inflate(R.menu.menu_people, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_eventlist_reload:
			refreshData();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			
			/*Intent intent = new Intent(getActivity(), EventDetailActivity.class);
			Activity activity = mAdapter.getItem(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable(BUNDLE_KEY_ACTIVITY, activity);
			intent.putExtras(bundle);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);*/
		}
		
	};
	
	public void refreshData() {
		isRefresh = true;
		
		mAdapter.clear();
		mAdapter.setIsLoadingData(true);
		mLvPeople .setSelection(0);
		mCurrentPage = 0;
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getPeople(mCurrentPage);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	private void loadMoreData(int page) {
		isRefresh = false;
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getPeople(mCurrentPage);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
}
