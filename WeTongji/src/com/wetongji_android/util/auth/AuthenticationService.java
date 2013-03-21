package com.wetongji_android.util.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {
	private Authenticator mAuthenticator;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mAuthenticator.getIBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mAuthenticator=new Authenticator(this);
	}

}
