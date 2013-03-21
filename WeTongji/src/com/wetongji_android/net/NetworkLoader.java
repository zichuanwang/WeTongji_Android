package com.wetongji_android.net;

import com.wetongji_android.net.http.HttpMethod;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import com.wetongji_android.util.exception.WTException;

public class NetworkLoader extends AsyncTaskLoader<String> {
	Bundle mArgs;
	HttpMethod mMethod;
	WTClient mClient;

	public NetworkLoader(Context context) {
		super(context);
		mClient=WTClient.getInstance();
	}
	
	public NetworkLoader(Context context, HttpMethod method, Bundle args){
		super(context);
		mMethod=method;
		mArgs=args;
		mClient=WTClient.getInstance();
	}

	@Override
	public String loadInBackground() {
		try {
			return mClient.execute(mMethod, mArgs);
		} catch (WTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
