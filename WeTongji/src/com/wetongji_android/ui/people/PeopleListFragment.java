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

import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;
import com.wetongji_android.factory.PersonFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class PeopleListFragment extends WTBaseFragment implements LoaderCallbacks<HttpRequestResult>,
OnScrollListener{

	public static final String BUNDLE_KEY_PERSON = "BUNDLE_KEY_PERSON";
	public static final String BUNDLE_KEY_IS_CURRENT = "BUNDLE_KEY_IS_CURRENT";
	private ListView mLvPeople;
	private PeopleListAdapter mAdapter;
	private int mCurrentPage = 0;
	private boolean isRefresh = false;
	private PersonFactory mFactory;
	private int mNextPager = 1;
	
	public static PeopleListFragment newInstance(StartMode startMode, Bundle args){
		PeopleListFragment f = new PeopleListFragment();
		Bundle bundle;
		
		if(args != null){
			bundle = args;
		}else{
			bundle = new Bundle();
		}
		
		switch(startMode) {
		case BASIC:
			bundle.putInt(BUNDLE_KEY_START_MODE, 1);
			break;
		case USERS:
			bundle.putInt(BUNDLE_KEY_START_MODE, 2);
			break;
		case LIKE:
		    bundle.putInt(BUNDLE_KEY_START_MODE, 3);
		case FRIENDS:
			break;
		case ATTEND:
			break;
		case TODAY:
			break;
		}
		
		f.setArguments(bundle);
		return f;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		switch(getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			if(mStartMode == StartMode.BASIC){
				mAdapter.loadDataFromDB();
			}else{
				loadDataLiked(1);
			}
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
		Bundle b = getArguments();
		if (b != null) {
			int modeCode = b.getInt(BUNDLE_KEY_START_MODE);
			mStartMode = (modeCode == 1) ? StartMode.BASIC : 
				((modeCode == 2) ? StartMode.USERS : StartMode.LIKE);
		}
		
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

	private void loadDataLiked(int page){
		isRefresh = false;
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getLikedObjectsListWithModelType(page, "Person");
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
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
			boolean like = false;
			
			try {
				json = new JSONObject(result.getStrResponseCon());
				if(json.has("Like")){
					strPeople = json.getString("Like");
					like = true;
				}else{
					strPeople = json.getString("People");
				}
				mNextPager = json.getInt("NextPager");
			} catch (JSONException e) {
			}
			
			List<Person> people = 
					mFactory.createObjects(strPeople, isRefresh, like);
			
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
		
		if(mStartMode != StartMode.LIKE) {
			menu.clear();
			inflater.inflate(R.menu.menu_people, menu);
		}
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.text_people);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_eventlist_reload:
			refreshData();
			break;
		case android.R.id.home:
			getActivity().finish();
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
			
			Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
			Person person = mAdapter.getItem(position);
			Bundle bundle = new Bundle();
			bundle.putParcelable(BUNDLE_KEY_PERSON, person);
			bundle.putBoolean(BUNDLE_KEY_IS_CURRENT, (position == 0));
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		}
		
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int id = data.getIntExtra(WTBaseDetailActivity.KEY_OBJECT_ID, 0);
	    int like = data.getIntExtra(WTBaseDetailActivity.KEY_LIKE_NUMBER, 0);
	    boolean canLike = data.getBooleanExtra(WTBaseDetailActivity.KEY_CAN_LIKE, true);
	    for (int i = 0; i < mAdapter.getCount(); i++) {
	        Person person = (Person) mAdapter.getItem(i);
	        if (person.getId() == id) {
	        	person.setLike(like);
	        	person.setCanLike(canLike);
	            mAdapter.setObjectAtPosition(i, person);
	        }
	    }
	}
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
