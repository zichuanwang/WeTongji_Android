package com.wetongji_android.ui.account;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.factory.AccountFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class AccountListFragment extends WTBaseFragment implements
		LoaderCallbacks<HttpRequestResult> 
{
	private Activity mActivity;
	private View mView;
	
	private ListView mListAccounts;
	private AccountListAdapter mAdapter;
	
	private AccountFactory mAccountFactory;
	
	public static AccountListFragment newInstance(StartMode startMode, Bundle args)
	{
		AccountListFragment f = new AccountListFragment();
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
			break;
		case FRIENDS:
			break;
		case ATTEND:
			break;
		}
		
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		
		mActivity = activity;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		mListAccounts = (ListView)mView.findViewById(R.id.lst_accounts);
		mAdapter = new AccountListAdapter(this, mListAccounts);
		mListAccounts.setAdapter(mAdapter);
		mListAccounts.setOnItemClickListener(itemClickListener);
		
		switch(getCurrentState(savedInstanceState))
		{
		case FIRST_TIME_START:
			loadDataLiked(1);
			break;
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			break;
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null){
			int modeCode = bundle.getInt(BUNDLE_KEY_START_MODE);
			mStartMode = (modeCode == 1) ? StartMode.BASIC : 
				((modeCode == 2) ? StartMode.USERS : StartMode.LIKE);
		}
		
		setRetainInstance(true);
		
		setHasOptionsMenu(true);
	}

	private void loadDataLiked(int page)
	{
		mAdapter.setIsLoadingData(true);
		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		Bundle args = apiHelper.getLikedObjectsListWithModelType(page, "Account");
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		if(container == null)
		{
			return null;
		}else
		{
			mView = inflater.inflate(R.layout.fragment_account, container, false);
		}
		
		return mView;
	}

	@Override
	public void onPause() 
	{
		super.onPause();
	}


	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) 
	{
		return new NetworkLoader(mActivity, HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) 
	{
		if(result.getResponseCode() == 0)
		{
			if(mAccountFactory == null)
				mAccountFactory = new AccountFactory();
			
			List<Account> accounts = mAccountFactory.createObjects(result.getStrResponseCon());
			
			mAdapter.addAll(accounts);
			mAdapter.setIsLoadingData(false);
		}else{
			ExceptionToast.show(mActivity, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) 
		{
			
		}
	};
}
