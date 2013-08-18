package com.wetongji_android.ui.auth;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseImagePickActivity;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class BaseAuthActivity extends WTBaseImagePickActivity implements
		LoaderCallbacks<HttpRequestResult> {

	public static final String PARAM_CONFIRM_CREDENTIALS = "confirmCredentials";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_AUTHTOKEN_TYPE = "authTokenType";

	private static final String TAG = BaseAuthActivity.class.getSimpleName();
	private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
	private Bundle mResultBundle = null;
	private AccountManager mAm;
	private ProgressDialog mPd = null;
	private boolean mConfirmCredentials = false;
	private String mPassword;
	private boolean mRequestNewAccount = false;
	protected String mUsername;
	private ApiHelper apiHelper;

	public final void setAccountAuthenticatorResult(Bundle result) {
		mResultBundle = result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate(" + savedInstanceState + ")");
		super.onCreate(savedInstanceState);

		apiHelper = ApiHelper.getInstance(this);
		mAccountAuthenticatorResponse = getIntent().getParcelableExtra(
				AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

		if (mAccountAuthenticatorResponse != null) {
			mAccountAuthenticatorResponse.onRequestContinued();
		}

		mAm = AccountManager.get(this);
		Log.i(TAG, "loading data from intent");
		final Intent intent = getIntent();
		mUsername = intent.getStringExtra(PARAM_USERNAME);
		mRequestNewAccount = mUsername == null;
		mConfirmCredentials = intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS,
				false);
		Log.i(TAG, "   request new:" + mRequestNewAccount);
	}

	public void finish() {
		if (mAccountAuthenticatorResponse != null) {
			// send the result bundle back if set, otherwise send an error.
			if (mResultBundle != null) {
				mAccountAuthenticatorResponse.onResult(mResultBundle);
			} else {
				mAccountAuthenticatorResponse.onError(
						AccountManager.ERROR_CODE_CANCELED, "canceled");
			}
			mAccountAuthenticatorResponse = null;
		}
		super.finish();
	}

	public void handleLogin(String username, String password) {
		if (mRequestNewAccount) {
			mUsername = username;
		}
		mPassword = password;

		showProgress();
		Bundle args = apiHelper.getUserLogOn(mUsername, mPassword);
		getSupportLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_DEFAULT, args, this).forceLoad();
	}

	private void finishConfirmCredentials(boolean result) {
		Log.i(TAG, "finishConfirmCredentials");
		final Account account = new Account(mUsername,
				WTApplication.ACCOUNT_TYPE);
		mAm.setPassword(account, mPassword);
		final Intent intent = new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void finishLogin(String result) {
		Log.i(TAG, "finishLogin");
		JSONObject user = null;
		final Account account = new Account(mUsername,
				WTApplication.ACCOUNT_TYPE);
		if (mRequestNewAccount) {
			mAm.addAccountExplicitly(account, mPassword, null);
		} else {
			mAm.setPassword(account, mPassword);
		}
		
		try {
			JSONObject data = new JSONObject(result);
			user = data.getJSONObject("User");
			String uid = user.getString("UID");
			String session = data.getString("Session");
			mAm.setUserData(account, AccountManager.KEY_USERDATA, uid);
			mAm.setAuthToken(account, WTApplication.AUTHTOKEN_TYPE, session);
			apiHelper.setSession(session);
			apiHelper.setUID(uid);
			WTApplication.getInstance().hasAccount = true;
			
			final Intent intent=new Intent(BaseAuthActivity.this, MainActivity.class);
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, WTApplication.ACCOUNT_TYPE);
			startActivity(intent);
			finish();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/*UserFactory userFactory = new UserFactory(this.getSupportFragmentManager().g);
		userFactory.createObject(user.toString());*/
	}

	public void onAuthenticationResult(String result) {
		boolean success = ((result != null) && (result.length() > 0));
		Log.i(TAG, "onAuthenticationResult(" + success + ")");
		hideProgress();
		if (success) {
			if (!mConfirmCredentials) {
				finishLogin(result);
			} else {
				finishConfirmCredentials(success);
			}
		} else {
			Log.e(TAG, "onAuthenticationResult: failed to authenticate");
		}
	}

	public void onAuthenticationCancel() {
		Log.i(TAG, "onAuthenticationCancel");
		hideProgress();
	}

	private void showProgress() {
		if (mPd == null) {
			mPd = new ProgressDialog(this);
			mPd.setIndeterminate(true);
		}
		mPd.show();
	}

	private void hideProgress() {
		if (mPd != null) {
			mPd.dismiss();
			mPd = null;
		}
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int id, Bundle args) {
		return new NetworkLoader(this, HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if (result.getResponseCode() == 0) {
			onAuthenticationResult(result.getStrResponseCon());
		} else {
			hideProgress();
			ExceptionToast.show(this, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

}
