package com.wetongji_android.ui.friend;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.net.HttpRequestResult;

public class FriendInviteFragment extends WTBaseFragment implements
		LoaderCallbacks<HttpRequestResult>, OnScrollListener 
{
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) 
	{
		

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		

	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) 
	{
				
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		
		
	}
}
