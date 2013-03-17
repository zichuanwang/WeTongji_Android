package com.wetongji_android.ui;

import com.wetongji_android.Constants;
import com.wetongji_android.R;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.util.auth.RSAEncrypter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
	
	public static final String PARAM_CONFIRM_CREDENTIALS="confirmCredentials";
	public static final String PARAM_PASSWORD="password";
	public static final String PARAM_USERNAME="username";
	public static final String PARAM_AUTHTOKEN_TYPE="authTokenType";
	
	private static final String TAG=AuthenticatorActivity.class.getSimpleName();
	private AccountManager mAm;
	private UserLoginTask mAuthTask=null;
	private ProgressDialog mPd=null;
	private boolean mConfirmCredentials=false;
	private final Handler handler=new Handler();
	private String mPassword;
	private EditText mEtPassword;
	private boolean mRequestNewAccount=false;
	private String mUsername;
	private EditText mEtUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate("+savedInstanceState+")");
		super.onCreate(savedInstanceState);
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

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		super.onCreateDialog(id, args);
		final ProgressDialog dialog=new ProgressDialog(this);
		dialog.setMessage(getText(R.string.title_activity_authenticator));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.i(TAG, "user cancelling authentication");
				if(mAuthTask!=null){
					mAuthTask.cancel(true);
				}
			}
		});
		mPd=dialog;
		return dialog;
	}
	
	public void handleLogin(View view){
		if(mRequestNewAccount){
			mUsername=mEtUsername.getText().toString();
		}
		mPassword=mEtPassword.getText().toString();
		if(!TextUtils.isEmpty(mUsername)&&!TextUtils.isEmpty(mPassword)){
			showProgress();
			mAuthTask=new UserLoginTask();
			mAuthTask.execute();
		}
	}
	
	private void finishConfirmCredentials(boolean result){
		Log.i(TAG, "finishConfirmCredentials");
		final Account account=new Account(mUsername, Constants.ACCOUNT_TYPE);
		mAm.setPassword(account, mPassword);
		final Intent intent=new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void finishLogin(String authToken){
		Log.i(TAG, "finishLogin");
		final Account account=new Account(mUsername, Constants.ACCOUNT_TYPE);
		if(mRequestNewAccount){
			mAm.addAccountExplicitly(account, mPassword, null);
			//ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, false);
		}
		else{
			mAm.setPassword(account, mPassword);
		}
		final Intent intent=new Intent();
		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void onAuthenticationResult(String authToken){
		boolean success=((authToken!=null)&&(authToken.length()>0));
		Log.i(TAG, "onAuthenticationResult("+success+")");
		mAuthTask=null;
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
		mAuthTask=null;
		hideProgress();
	}
	
	private void showProgress(){
		showDialog(0);
	}
	
	private void hideProgress(){
		if(mPd!=null){
			mPd.dismiss();
			mPd=null;
		}
	}

	public class UserLoginTask extends AsyncTask<Void, Void, String>{
		WTClient client;
		
		@Override
		protected String doInBackground(Void... params) {
			client=WTClient.getInstance();
			try {
				String encryptedPass=RSAEncrypter.encrypt(mPassword, AuthenticatorActivity.this);
				client.login(mUsername, encryptedPass);
				return client.getSession();
			} catch (Exception e) {
				Log.e(TAG, "UserLoginTask.doInBackground: failed to authenticate");
				Log.i(TAG, e.toString());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			onAuthenticationCancel();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			onAuthenticationResult(result);
		}
		
	}
	
}
