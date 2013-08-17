package com.wetongji_android.ui.friend;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
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

import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.factory.UserFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.friend.FriendListAdapter.ViewHolder;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class FriendListFragment extends WTBaseFragment implements
		LoaderCallbacks<HttpRequestResult>, OnScrollListener 
{
	public static final String BUNDLE_KEY_USER = "User";
	
	private String mUID;
	private boolean event = true;
	private View mView;
	private Activity mActivity;
	private UserFactory mFactory;
	
	private ListView lstFriends;
	private FriendListAdapter mAdapter;
	
	private String iSelectedId;
	
	private StringBuilder selectedIdsBuilder = new StringBuilder();
	
	public static FriendListFragment newInstance(StartMode startMode, Bundle args)
	{
		FriendListFragment fragment = new FriendListFragment();
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
			break;
		case LIKE:
			bundle.putInt(BUNDLE_KEY_START_MODE, 2);
			break;
		case FRIENDS:
			bundle.putInt(BUNDLE_KEY_START_MODE, 4);
			break;
		case ATTEND:
			bundle.putInt(BUNDLE_KEY_START_MODE, 5);
			break;
		}
		
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		
		if(b != null){
			int modeCode = b.getInt(BUNDLE_KEY_START_MODE);
			switch(modeCode){
			case 1:
				mStartMode = StartMode.BASIC;
				break;
			case 2:
				mStartMode = StartMode.LIKE;
				break;
			case 4:
				mStartMode = StartMode.FRIENDS;
				break;
			case 5:
				mStartMode = StartMode.ATTEND;
				break;
			}
			
			mUID = b.getString(WTBaseFragment.BUNDLE_KEY_UID);
			event = b.getBoolean(FriendListActivity.TAG_COURSE, false);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		switch(getCurrentState(savedInstanceState))
		{
		case FIRST_TIME_START:
			if(mStartMode == StartMode.BASIC){
				//mAdapter.loadDataFromDB();
				refreshData();
			}else if(mStartMode == StartMode.FRIENDS){
				getFriendsListOfUser();
			}else if(mStartMode == StartMode.ATTEND){
				getFriendsListOfAttend();
			}else{
				getLikedFriends(1);
			}
			break;
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_friend, null);
		lstFriends = (ListView)mView.findViewById(R.id.lst_friends);
		mAdapter = new FriendListAdapter(this, lstFriends);
		lstFriends.setAdapter(mAdapter);
		lstFriends.setOnItemClickListener(new ItemClickListener());
		
		return mView;
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(mActivity, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if(result.getResponseCode() == 0) {
			if(mFactory == null) {
				mFactory = new UserFactory(this);
			}
			
			List<User> users = mFactory.createObjects(result.getStrResponseCon(), false);
			mAdapter.getData().clear();
			mAdapter.addAll(users);
			mAdapter.setIsLoadingData(false);
		}else
		{
			
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		
	}
	
	public void refreshData() {
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, apiHelper.getFriends(), this);
	}
	
	private void getFriendsListOfUser() {
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, apiHelper.getFriendsOfUser(mUID), this);
	}
	
	private void getFriendsListOfAttend() {
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		if(event)
		{
			getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, 
					apiHelper.getFriendsWithSameActivity(mUID), this);
		}else
		{
			getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, 
					apiHelper.getFriendsWithSameCourse(mUID), this);
		}
	}
	
	private void getLikedFriends(int page) {
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, apiHelper.getLikedObjectsListWithModelType(page, "User"), this);
	}
	
	public String getiSelectedId() {
		return selectedIdsBuilder.toString();
	}

	public void setiSelectedId(String iSelectedId) {
		this.iSelectedId = iSelectedId;
	}

	class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			if(mActivity instanceof FriendInviteActivity) {
				ViewHolder holder = (ViewHolder)view.getTag();
				holder.cbFriendInvite.toggle();
				FriendListAdapter.getIsSelected().put(position, holder.cbFriendInvite.isChecked());
				iSelectedId = mAdapter.getItem(position).getUID();
				selectedIdsBuilder.append(iSelectedId).append(",");
			}else {
				Intent intent = new Intent(mActivity, FriendDetailActivity.class);
				Bundle  bundle = new Bundle();
				bundle.putParcelable(BUNDLE_KEY_USER, mAdapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
				mActivity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		}
	}
}
