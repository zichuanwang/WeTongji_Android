package com.wetongji_android.ui.setting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class WTChangePwdActivity extends SherlockFragmentActivity implements
		LoaderCallbacks<HttpRequestResult>, OnCheckedChangeListener {
	
	private EditText edtOld;
	private EditText edtNew;
	private EditText edtConfirm;
	
	private ToggleButton btnConfirm;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_change_pwd);
		
		initWidget();
		setupActionBar();
	}
	
	private void initWidget(){
		edtOld = (EditText)findViewById(R.id.edt_old_password);
		edtNew = (EditText)findViewById(R.id.edt_new_password);
		edtConfirm = (EditText)findViewById(R.id.edt_confirm_password);
	}
	
	private void setupActionBar()
	{
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		View v = getLayoutInflater().inflate(R.layout.actionbar_change_pwd, null);
		ab.setCustomView(v);
		btnConfirm = (ToggleButton)v.findViewById(R.id.btn_on_confirm);
		btnConfirm.setOnCheckedChangeListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if(result.getResponseCode() == 0){
			try {
				WTApplication application = WTApplication.getInstance();
				JSONObject json = new JSONObject(result.getStrResponseCon());
				application.session = json.getString("Session");
				AccountManager am = AccountManager.get(this);
				Account[] ac = am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
				am.setAuthToken(ac[0], WTApplication.AUTHTOKEN_TYPE, application.session);
				Toast.makeText(this, getResources().getString(R.string.update_password_success), 
						Toast.LENGTH_SHORT).show();
				btnConfirm.setChecked(false);
				ApiHelper.getInstance(this).setSession(application.session);
				finish();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else {
			ExceptionToast.show(this, result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if(isParameterValid()){
			ApiHelper apiHelper = ApiHelper.getInstance(this);
			getSupportLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, 
					apiHelper.userUpdatePassword(edtOld.getText().toString(), edtConfirm.getText().toString()), this);
		}
	}
	
	private boolean isParameterValid(){
		boolean result = true;
		
		if(edtOld.getText().toString().equals("")){
			result = false;
			Toast.makeText(this, getResources().getString(R.string.password_error2), Toast.LENGTH_SHORT).show();
		}else if(edtNew.getText().toString().equals("")){
			result = false;
			Toast.makeText(this, getResources().getString(R.string.password_error3), Toast.LENGTH_SHORT).show();
		}else if(edtConfirm.getText().toString().equals("")){
			result = false;
			Toast.makeText(this, getResources().getString(R.string.password_error4), Toast.LENGTH_SHORT).show();
		}else if(!edtNew.getText().toString().equals(edtConfirm.getText().toString())){
			result = false;
			Toast.makeText(this, getResources().getString(R.string.password_error1), Toast.LENGTH_SHORT).show();
		}else if(!isSyntaxWrong(edtNew.getText().toString()) || !isSyntaxWrong(edtConfirm.getText().toString())){
			result = false;
			Toast.makeText(this, getResources().getString(R.string.password_syntax_wrong), Toast.LENGTH_SHORT).show();
		}
		
		return result;
	}
	
	private boolean isSyntaxWrong(String password){
		boolean result = false;
		
		String strRegex = "^\\w{6,}$";
    	Pattern p = Pattern.compile(strRegex);
    	
    	Matcher m = p.matcher(password);
    	
    	if(m.matches())
    		result = true;
    	
		return result;
	}
}
