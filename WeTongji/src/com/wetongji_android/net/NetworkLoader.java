package com.wetongji_android.net;

import com.wetongji_android.net.http.HttpMethod;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.ApiHelper;
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
		if (!WTUtility.isConnect(getContext())) {
			cancelLoad();
			return new HttpRequestResult(444, "");
		}
		try 
		{
			HttpRequestResult result=mClient.execute(mMethod, mArgs);
			Log.v("Network result: ", result.getStrResponseCon() + mArgs.getString(ApiHelper.API_ARGS_METHOD));
			return result;
		} catch (WTException e) 
		{
			e.printStackTrace();
			return new HttpRequestResult(200, "");
		}
	}

	@Override
	protected void onStartLoading() 
	{
		
		forceLoad();
	}

	@Override
	public void onCanceled(HttpRequestResult data) {
		super.onCanceled(data);
		if (data == null) {
			data = new HttpRequestResult(444, "");
			//deliverResult(data);
		}
	}
}
