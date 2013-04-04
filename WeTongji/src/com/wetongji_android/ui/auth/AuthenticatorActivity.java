package com.wetongji_android.ui.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiMethods;

public class AuthenticatorActivity extends FragmentActivity
implements LoaderCallbacks<String>{
	
	public static final String PARAM_CONFIRM_CREDENTIALS="confirmCredentials";
	public static final String PARAM_PASSWORD="password";
	public static final String PARAM_USERNAME="username";
	public static final String PARAM_AUTHTOKEN_TYPE="authTokenType";
	
	private static final String TAG=AuthenticatorActivity.class.getSimpleName();
	private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;
	private AccountManager mAm;
	private ProgressDialog mPd=null;
	private boolean mConfirmCredentials=false;
	private String mPassword;
	private EditText mEtPassword;
	private boolean mRequestNewAccount=false;
	private String mUsername;
	private EditText mEtUsername;
	
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
		// Show the Up button in the action bar.
		setupActionBar();
		
		mEtUsername=(EditText) findViewById(R.id.et_username);
		mEtPassword=(EditText) findViewById(R.id.et_password);
		if(!TextUtils.isEmpty(mUsername)){
			mEtUsername.setText(mUsername);
		}
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
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void handleLogin(View view){
		if(mRequestNewAccount){
			mUsername=mEtUsername.getText().toString();
		}
		mPassword=mEtPassword.getText().toString();
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
	public Loader<String> onCreateLoader(int id, Bundle args) {
		return new NetworkLoader(this, HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<String> arg0, String result) {
		onAuthenticationResult(result);
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) {
	}
	
}
