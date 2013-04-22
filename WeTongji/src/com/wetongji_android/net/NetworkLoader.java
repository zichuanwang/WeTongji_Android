package com.wetongji_android.net;

import com.wetongji_android.net.http.HttpMethod;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.HttpRequestResult;

public class NetworkLoader extends AsyncTaskLoader<HttpRequestResult> 
{
	private Bundle mArgs;
	private HttpMethod mMethod;
	private WTClient mClient;

	public NetworkLoader(Context context) 
	{
		super(context);
		mClient=WTClient.getInstance();
	}
	
	public NetworkLoader(Context context, HttpMethod method, Bundle args)
	{
		super(context);
		mMethod=method;
		mArgs=args;
		mClient=WTClient.getInstance();
	}

	@Override
	public HttpRequestResult loadInBackground() 
	{
		try {
			return mClient.execute(mMethod, mArgs);
		} catch (WTException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}
	
}
