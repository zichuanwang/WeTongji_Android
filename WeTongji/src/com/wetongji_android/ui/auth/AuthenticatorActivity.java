package com.wetongji_android.ui.auth;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiMethods;
import com.wetongji_android.util.net.HttpRequestResult;

public class AuthenticatorActivity extends SherlockFragmentActivity
implements LoaderCallbacks<HttpRequestResult>, OnClickListener, OnCheckedChangeListener {
	
	public static final String PARAM_CONFIRM_CREDENTIALS="confirmCredentials";
	public static final String PARAM_PASSWORD="password";
	public static final String PARAM_USERNAME="username";
	public static final String PARAM_AUTHTOKEN_TYPE="authTokenType";
	public static final String PARAM_INITIAL_LOGIN="initialLogin";
	
	private static final String TAG=AuthenticatorActivity.class.getSimpleName();
	private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;
	private AccountManager mAm;
	private ProgressDialog mPd=null;
	private boolean mConfirmCredentials=false;
	private String mPassword;
	private boolean mRequestNewAccount=false;
	private String mUsername;
	private ToggleButton btnOnLogin;
	
	public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate("+savedInstanceState+")");
		super.onCreate(savedInstanceState);
		mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
		
		mAm=AccountManager.get(this);
		Log.i(TAG, "loading data from intent");
		final Intent intent=getIntent();
		mUsername=intent.getStringExtra(PARAM_USERNAME);
		mRequestNewAccount=mUsername==null;
		mConfirmCredentials=intent.getBooleanExtra(PARAM_CONFIRM_CREDENTIALS, false);
		Log.i(TAG, "   request new:"+mRequestNewAccount);
		setContentView(R.layout.activity_authenticator);
		
		setupActionBar(!intent.getBooleanExtra(PARAM_INITIAL_LOGIN, false));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar(boolean isOnAuth) {
		ActionBar ab=getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		View v=getLayoutInflater().inflate(R.layout.actionbar_authenticator, null);
		ab.setCustomView(v);
		btnOnLogin=(ToggleButton) v.findViewById(R.id.btn_on_login);
		btnOnLogin.setOnCheckedChangeListener(this);
		btnOnLogin.setChecked(isOnAuth);
		if(!isOnAuth){
			onCheckedChanged(btnOnLogin, false);
		}
		Button btnNotNow=(Button) v.findViewById(R.id.btn_not_now);
		btnNotNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_not_now:
			Intent intent=new Intent(this, MainActivity.class);
			intent.putExtra(MainActivity.PARAM_PREVIEW_WITHOUT_lOGIN, true);
			startActivity(intent);
			finish();
			break;
		}
	}
	

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i(TAG, "isChecked"+isChecked);
		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
		if(btnOnLogin.isChecked()){
			transaction.replace(R.id.auth_content_container, LoginFragment.newInstance(mUsername));
		}
		else{
			transaction.replace(R.id.auth_content_container, IntroFragment.newInstance());
		}
		transaction.commit();
	}
	
	
	public void handleLogin(String username,String password){
		if(mRequestNewAccount){
			mUsername=username;
		}
		mPassword=password;
		if(!TextUtils.isEmpty(mUsername)&&!TextUtils.isEmpty(mPassword)){
			showProgress();
			Bundle args=ApiMethods.getUserLogOn(mUsername, mPassword, this);
			getSupportLoaderManager().initLoader(WTApplication.NETWORK_LOADER, args, this);
		}
	}
	
	private void finishConfirmCredentials(boolean result){
		Log.i(TAG, "finishConfirmCredentials");
		final Account account=new Account(mUsername, WTApplication.ACCOUNT_TYPE);
		mAm.setPassword(account, mPassword);
		final Intent intent=new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void finishLogin(String authToken){
		Log.i(TAG, "finishLogin");
		final Account account=new Account(mUsername, WTApplication.ACCOUNT_TYPE);
		if(mRequestNewAccount){
			mAm.addAccountExplicitly(account, mPassword, null);
		}
		else{
			mAm.setPassword(account, mPassword);
		}
		mAm.setAuthToken(account, WTApplication.AUTHTOKEN_TYPE, authToken);
		final Intent intent=new Intent();
		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, WTApplication.ACCOUNT_TYPE);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void onAuthenticationResult(String authToken){
		boolean success=((authToken!=null)&&(authToken.length()>0));
		Log.i(TAG, "onAuthenticationResult("+success+")");
		Log.i(TAG, "auth token="+authToken);
		hideProgress();
		if(success){
			if(!mConfirmCredentials){
				finishLogin(authToken);
			}
			else{
				finishConfirmCredentials(success);
			}
		}
		else{
			Log.e(TAG, "onAuthenticationResult: failed to authenticate");
		}
	}
	
	public void onAuthenticationCancel(){
		Log.i(TAG, "onAuthenticationCancel");
		hideProgress();
	}
	
	private void showProgress(){
		if(mPd==null){
			mPd=new ProgressDialog(this);
			mPd.setIndeterminate(true);
			mPd.setTitle(R.string.title_activity_authenticator);
		}
		mPd.show();
	}
	
	private void hideProgress(){
		if(mPd!=null){
			mPd.dismiss();
			mPd=null;
		}
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int id, Bundle args) {
		return new NetworkLoader(this, HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0, HttpRequestResult result) {
		if(result.getResponseCode()==0){
			try {
				JSONObject json=new JSONObject(result.getStrResponseCon());
				onAuthenticationResult(json.getString("Session"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else{
			hideProgress();
			ExceptionToast.show(this, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

}
