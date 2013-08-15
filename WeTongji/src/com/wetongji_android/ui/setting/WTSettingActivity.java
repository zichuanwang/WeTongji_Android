package com.wetongji_android.ui.setting;

import java.io.File;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.util.AQUtility;
import com.wetongji_android.R;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;

public class WTSettingActivity extends SherlockFragmentActivity {

	private RelativeLayout rlChangePwd;
	private RelativeLayout rlClearCache;
	private RelativeLayout rlAboutWe;
	private Button btnLogOut;
	private TextView tvCacheAmount;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_setting);

		initWidget();
	}

	private void initWidget() {
		rlChangePwd = (RelativeLayout) findViewById(R.id.ll_setting_change_pwd);
		rlChangePwd.setOnClickListener(clickListener);
		rlClearCache = (RelativeLayout) findViewById(R.id.ll_setting_clear_cache);
		rlClearCache.setOnClickListener(clickListener);
		rlAboutWe = (RelativeLayout) findViewById(R.id.ll_setting_about);
		rlAboutWe.setOnClickListener(clickListener);
		btnLogOut = (Button) findViewById(R.id.btn_setting_log_out);
		btnLogOut.setOnClickListener(clickListener);

		// set cache amount
		tvCacheAmount = (TextView) findViewById(R.id.text_setting_cache);
		File cacheDir = getExternalFilesDir("imgCache");
		int cacheKBs = (int) getDirSize(cacheDir) / 1024;
		tvCacheAmount.setText(getString(R.string.pref_clear_cache_summary,
				cacheKBs));
	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ll_setting_change_pwd) {
				Intent intent = new Intent(WTSettingActivity.this,
						WTChangePwdActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else if (v.getId() == R.id.ll_setting_clear_cache) {
				clearCache();
			} else if (v.getId() == R.id.ll_setting_about) {
				Intent intent = new Intent(WTSettingActivity.this,
						WTAboutActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else {
				AccountManager am = AccountManager.get(WTSettingActivity.this);
				Account[] accounts = am
						.getAccountsByType(WTApplication.ACCOUNT_TYPE);
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

	private void clearCache() {
		long triggerSize = 10; //starts cleaning when cache size is larger than 3M
        long targetSize = 0;
		AQUtility.cleanCacheAsync(this, triggerSize, targetSize);
		((TextView) findViewById(R.id.text_setting_cache)).setText(getString(
				R.string.pref_clear_cache_summary, 0));
	}

	// get directory size by byte
	private long getDirSize(File dir) {
		long size = 0;
		File files[] = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				size += getDirSize(files[i]);
			} else {
				size += files[i].length();
			}
		}
		return size;
	}
}
