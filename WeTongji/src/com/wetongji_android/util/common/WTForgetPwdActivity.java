package com.wetongji_android.util.common;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class WTForgetPwdActivity extends SherlockFragmentActivity implements
		LoaderCallbacks<HttpRequestResult>, OnCheckedChangeListener {

	private EditText etStudentID;
	private EditText etStudentName;
	
	private ToggleButton btnSubmit;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_forget_pwd);
		
		setupActionBar();
		initWidget();
	}

	private void setupActionBar() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		View v = getLayoutInflater().inflate(R.layout.actionbar_forget_pwd, null);
		ab.setCustomView(v);
		btnSubmit = (ToggleButton)v.findViewById(R.id.btn_submit);
		btnSubmit.setOnCheckedChangeListener(this);
	}
	
	private void initWidget() {
		etStudentID = (EditText)findViewById(R.id.tv_student_id);
		etStudentName = (EditText)findViewById(R.id.tv_student_name);
	}
	
	private boolean checkValidation(String name, String password)
	{
		boolean result = true;
		
		if(TextUtils.isEmpty(name))
		{
			result = false;
			ExceptionToast.show(this, 16);
		}else if(TextUtils.isEmpty(password))
		{
			result = false;
			ExceptionToast.show(this, 17);
		}
		
		return result;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		break;
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
		if(result.getResponseCode() == 0) {
			Toast.makeText(this, getResources().getString(R.string.reset_pwd_success), Toast.LENGTH_SHORT).show();
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		} else {
			Toast.makeText(this, getResources().getString(R.string.text_error_check_network), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		String id = etStudentID.getText().toString();
		String name = etStudentName.getText().toString();
		
		if(checkValidation(id, name)) {
			ApiHelper apiHelper = ApiHelper.getInstance(this);
			getSupportLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_FORGET_PWD, 
					apiHelper.forgetPassword(id, name), this);
		} else {
			btnSubmit.setChecked(false);
		}
	}

}
