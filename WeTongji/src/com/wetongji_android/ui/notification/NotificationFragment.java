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
import android.widget.TextView;

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
			mView = inflater.inflate(R.layout.notification_frame, container, false);
		}
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		//set list adapter
		mListNotifications = (ListView)mView.findViewById(R.id.lst_notification);
		mAdapter = new NotificationListAdapter(this);
		mListNotifications.setAdapter(mAdapter);
		
		//init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.getNotifications(false);
		//showProgressDialog();
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		mActivity = activity;
	}

	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) 
	{
		// TODO Auto-generated method stub
		hideProgressDialog();
		
		if(result.getResponseCode() == 0)
		{
			if(mFactory == null)
				mFactory = new NotificationFactory();
			
			List<Notification> results = mFactory.createObjects(result.getStrResponseCon());
			Log.v(TAG, "" + results.size());
		}else
		{
			
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
	}
	
	public void loadNotifications()
	{
		//init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.getNotifications(false);
		//showProgressDialog();
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
}
