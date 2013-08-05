package com.wetongji_android.ui.notification;

import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Notification;
import com.wetongji_android.factory.NotificationFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>
{
	private static final String TAG = "NotificationFragment";
	
	private View mView;
	private ListView mListNotifications;
	private NotificationListAdapter mAdapter;
	private Activity mActivity;
	
	private ProgressDialog mProDialog = null;
	
	private NotificationFactory mFactory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		if(container == null)
		{
			return null;
		}else
		{
			mView = inflater.inflate(R.layout.notification_frame, container, false);
		}
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		//set list adapter
		mListNotifications = (ListView)mView.findViewById(R.id.lst_notification);
		mAdapter = new NotificationListAdapter(this);
		mListNotifications.setAdapter(mAdapter);
		
		if(WTApplication.getInstance().hasAccount)
		{
			//init loader(this loader is used for loading data from network)
			ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
			Bundle bundle = apiHelper.getNotifications(false);
			//showProgressDialog();
			getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
		}
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		mActivity = activity;
	}

	@Override
	public void onPause() 
	{
		super.onPause();
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loadId, Bundle arg1) 
	{
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) 
	{
		hideProgressDialog();
		
		if(result.getResponseCode() == 0)
		{
			if(mFactory == null)
				mFactory = new NotificationFactory();
			
			List<Notification> results = mFactory.createObjects(result.getStrResponseCon());
			mAdapter.setContentList(results);
			Log.v(TAG, "" + results.size());
		}else
		{
			
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
	}
	
	public void loadNotifications()
	{
		//init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.getNotifications(false);
		showProgressDialog();
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
	}
	
	private void showProgressDialog()
	{
		if(mProDialog == null)
		{
			mProDialog = new ProgressDialog(mActivity);
			mProDialog.setIndeterminate(true);
			mProDialog.show();
		}
	}
	
	private void hideProgressDialog()
	{
		if(mProDialog != null)
		{
			mProDialog.dismiss();
			mProDialog = null;
		}
	}
	
	public void startAcceptAction(Notification notifcation) {
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.acceptFriendInvitation(
				String.valueOf(notifcation.getSourceId()));
		getLoaderManager().restartLoader(
					WTApplication.NETWORK_LOADER_ACCEPT_FRIEND,
					bundle, this);
	}
	
	public void startIngoreAction(Notification notifcation) {
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.ignoreFriendInvitation(
				String.valueOf(notifcation.getSourceId()));
		getLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_IGNORE_FRIEDN,
				bundle, this);
	}
}
