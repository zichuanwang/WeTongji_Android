package com.wetongji_android.util.auth;

import org.json.JSONObject;

import com.wetongji_android.R;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.ui.auth.BaseAuthActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Authenticator extends AbstractAccountAuthenticator {
	
	private static final String TAG=Authenticator.class.getSimpleName();
	private final Context mContext;
	private ApiHelper apiHelper;

	public Authenticator(Context context) {
		super(context);
		mContext=context;
		apiHelper=ApiHelper.getInstance(mContext);
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
			String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		Log.v(TAG, "addAccount");
		final AccountManager am=AccountManager.get(mContext);
		if(am.getAccountsByType(WTApplication.ACCOUNT_TYPE).length==0){
			final Intent intent=new Intent(mContext, AuthActivity.class);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
			final Bundle bundle=new Bundle();
			bundle.putParcelable(AccountManager.KEY_INTENT, intent);
			return bundle;
		}
		else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Looper.prepare();
					Toast.makeText(mContext, R.string.text_only_one_account, Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}).start();
			return null;
		}
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		Log.v(TAG, "confirmCredentials");
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		Log.v(TAG, "editProperties");
		throw new UnsupportedOperationException();
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.v(TAG, "getAuthToken");
		
		if(!authTokenType.equals(WTApplication.AUTHTOKEN_TYPE)){
			final Bundle result=new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
			return result;
		}

		final AccountManager am=AccountManager.get(mContext);
		String authToken=am.peekAuthToken(account, WTApplication.AUTHTOKEN_TYPE);
		final String password=am.getPassword(account);
		if(password!=null){
			if(TextUtils.isEmpty(authToken)){
				WTClient client=WTClient.getInstance();
				try {
					Bundle args=apiHelper.getUserLogOn(account.name, password);
					HttpRequestResult httpResult=client.execute(HttpMethod.Get, args);
					if(httpResult.getResponseCode()==0){
						JSONObject json=new JSONObject(httpResult.getStrResponseCon());
						authToken=json.getString("Session");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(!TextUtils.isEmpty(authToken)){
				final Bundle result=new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, WTApplication.ACCOUNT_TYPE);
				result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
				return result;
			}
		}
		
		final Intent intent=new Intent(mContext, BaseAuthActivity.class);
		intent.putExtra(BaseAuthActivity.PARAM_USERNAME, account.name);
		intent.putExtra(BaseAuthActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle=new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		Log.v(TAG, "getAuthTokenLabel");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		Log.v(TAG, "hasFeatures");
		final Bundle bundle=new Bundle();
		bundle.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return bundle;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		Log.v(TAG, "updateCredentials");
		return null;
	}

}
