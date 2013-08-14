package com.wetongji_android.ui.setting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;

public class WTSettingActivity extends SherlockFragmentActivity {
	
	private RelativeLayout rlChangePwd;
	private RelativeLayout rlClearCache;
	private RelativeLayout rlAboutWe;
	private Button btnLogOut;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_setting);
		
		initWidget();
	}
	
	private void initWidget(){
		rlChangePwd = (RelativeLayout)findViewById(R.id.ll_setting_change_pwd);
		rlChangePwd.setOnClickListener(clickListener);
		rlClearCache = (RelativeLayout)findViewById(R.id.ll_setting_clear_cache);
		rlClearCache.setOnClickListener(clickListener);
		rlAboutWe = (RelativeLayout)findViewById(R.id.ll_setting_about);
		rlAboutWe.setOnClickListener(clickListener);
		btnLogOut = (Button)findViewById(R.id.btn_setting_log_out);
		btnLogOut.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.ll_setting_change_pwd){
				Intent intent = new Intent(WTSettingActivity.this, WTChangePwdActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}else if(v.getId() == R.id.ll_setting_clear_cache){
				
			}else if(v.getId() == R.id.ll_setting_about){
				Intent intent = new Intent(WTSettingActivity.this, WTAboutActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}else{
				AccountManager am = AccountManager.get(WTSettingActivity.this);
				Account[] accounts = am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
				if (accounts.length != 0) {
					am.removeAccount(accounts[0], null, null);
				}
				Intent intent = new Intent();
				intent.setClass(WTSettingActivity.this, AuthActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
