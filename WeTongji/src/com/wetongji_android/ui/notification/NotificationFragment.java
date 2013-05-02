package com.wetongji_android.ui.notification;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>
{

	private View mView;
	private AmazingListView mListNotifications;
	private NotificationListAdapter mAdapter;
	
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
		
		//init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.getNotifications();
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
		
		//set list adapter
		mListNotifications = (AmazingListView)mView.findViewById(R.id.lst_notification);
		mAdapter = new NotificationListAdapter(this);
		mListNotifications.setAdapter(mAdapter);
		
		//set loading text
		TextView text = new TextView(getActivity());
		text.setText("loading....");
		mListNotifications.setLoadingView(text);
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
		if(result.getResponseCode() != 0)
		{
			
		}else
		{
			
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
	}
}
