package com.wetongji_android.ui.setting;

import java.io.File;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.util.AQUtility;
import com.wetongji_android.R;
import com.wetongji_android.service.IChangeInterval;
import com.wetongji_android.service.WeNotificationService;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;

public class WTSettingActivity extends SherlockFragmentActivity {
    private static final String PREFERENCES_FILE_NAME = "USER_CONFIG";
    private static final String PREFERENCE_INTERVAL = "notification_interval";

	private RelativeLayout rlChangePwd;
	private RelativeLayout rlClearCache;
	private RelativeLayout rlAboutWe;
    private RelativeLayout rlChangeInterval;
	private LinearLayout llChangePwdArea;
	private Button btnLogOut;
	private TextView tvCacheAmount;
    private TextView tvIntervalValue;
	private ProgressDialog progressDialog;

    private SharedPreferences sp;

    private IChangeInterval notifyService;
    private long interval = 2 * 60000;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            notifyService = (IChangeInterval)iBinder;
            notifyService.setInterval(interval);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            notifyService = null;
        }
    };

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_setting);
		
		initWidget();

        if (interval != 0) {
            bindService(new Intent("com.wetongji_android.service.WeNotificationService"), serviceConnection,
                    BIND_AUTO_CREATE);
        }

	}

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
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
		llChangePwdArea = (LinearLayout) findViewById(R.id.ll_setting_change_pwd_area);
        rlChangeInterval = (RelativeLayout) findViewById(R.id.ll_setting_change_interval_area);
        rlChangeInterval.setOnClickListener(clickListener);

		// check if the use is logined
		if (!WTApplication.getInstance().hasAccount) {
			llChangePwdArea.setVisibility(View.GONE);
			btnLogOut.setText(R.string.text_login);
			btnLogOut.setBackgroundResource(R.drawable.btn_default_holo_light);
		}
		
		// set cache amount
		tvCacheAmount = (TextView) findViewById(R.id.text_setting_cache);
		File cacheDir = getExternalFilesDir("imgCache");
		int cacheKBs = (int) getDirSize(cacheDir) / 1024;
		tvCacheAmount.setText(getString(R.string.pref_clear_cache_summary,
				cacheKBs));

        // set interval value
        tvIntervalValue = (TextView) findViewById(R.id.text_setting_interval);
        sp = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);
        int index = sp.getInt(PREFERENCE_INTERVAL, 0);
        setInterval(index);
        tvIntervalValue.setText(getResources().getStringArray(R.array.notification_interval_values)[index]);
	}

    private void setInterval(int type) {
        switch (type) {
            case 0: {
                interval = 0;
                break;
            }
            case 1: {
                interval = 45000;
                break;
            }
            case 2: {
                interval = 2 * 60000;
                break;
            }
            case 3: {
                interval = 5 * 60000;
                break;
            }
            case 4: {
                interval = 10 * 60000;
                break;
            }
        }
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
			} else if(v.getId() == R.id.ll_setting_change_interval) {
                //TODO
            } else {
				doLogout();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
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
	
	private void doLogout() {
		AccountManager am = AccountManager.get(WTSettingActivity.this);
		Account[] accounts = am
				.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if (accounts.length != 0) {
			am.removeAccount(accounts[0], null, null);
		}
		WTApplication.getInstance().hasAccount = false;
		new ClearDataTask().execute();
		
	}
	
	// clear database task
	private class ClearDataTask extends AsyncTask<Void, Void, Void> {
		DbHelper dbHelper;
		
		public ClearDataTask() {
			super();
			dbHelper = WTApplication.getInstance().getDbHelper();
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(WTSettingActivity.this);
			progressDialog.setMessage(getString(R.string.msg_progress_logout));
			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			dbHelper.clearCache();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			Intent intent = new Intent();
			intent.setClass(WTSettingActivity.this, AuthActivity.class);
			startActivity(intent);
			finish();
		}
		
	}

    private void openIntervalDialog() {
        final Dialog dialog = new Dialog(this, R.style.WTDialog);
        dialog.setTitle(R.string.pref_notification_interval);
        dialog.setContentView(R.layout.dialog_events_sort);
        dialog.setCanceledOnTouchOutside(true);
        RadioGroup rgInterval = (RadioGroup) dialog
                .findViewById(R.id.rg_dialog_notify_interval);
        int checkedId = sp.getInt(PREFERENCE_INTERVAL, 0);
        rgInterval.check(checkedId);
        rgInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(PREFERENCE_INTERVAL, checkedId);
                editor.apply();

                // set the Service
                setInterval(checkedId);
                if (interval == 0) {
                    stopService(new Intent(WTSettingActivity.this, WeNotificationService.class));
                } else {
                    bindService(new Intent("com.wetongji_android.service.WeNotificationService"), serviceConnection,
                            BIND_AUTO_CREATE);
                }
            }
        });

        dialog.show();
    }
}
