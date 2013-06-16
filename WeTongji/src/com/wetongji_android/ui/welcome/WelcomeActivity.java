package com.wetongji_android.ui.welcome;

import java.io.IOException;

import com.wetongji_android.R;
import com.wetongji_android.ui.auth.IntroActivity;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity 
{
	public static final int SPLASH_TIME = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		jumpToMainActivity();
	}
	
	private void jumpToMainActivity()
	{
		final AccountManager accountManager = AccountManager.get(this);
		final Account[] accounts = accountManager.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		
		if(accounts.length > 0)
		{
			accountManager.getAuthToken(accounts[0], WTApplication.AUTHTOKEN_TYPE, null, null, 
					new AccountManagerCallback<Bundle>()
					{
						@Override
						public void run(AccountManagerFuture<Bundle> result) 
						{
							// TODO Auto-generated method stub
							try 
							{
								Bundle bundle = result.getResult();
								String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
								WTApplication app = WTApplication.getInstance();
								app.session = authToken;
								app.uid = accountManager.getUserData(accounts[0], AccountManager.KEY_USERDATA);
								app.hasAccount = true;
								
								finish();
								Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
								startActivity(intent);
							} catch (OperationCanceledException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AuthenticatorException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				
					}, null);
		}else
		{
			new Handler().postDelayed(new Runnable() 
			{
				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					WelcomeActivity.this.finish();
					Intent intent = new Intent(WelcomeActivity.this, IntroActivity.class);
					startActivity(intent);
				}		
			},  SPLASH_TIME);
		}
	}
}
