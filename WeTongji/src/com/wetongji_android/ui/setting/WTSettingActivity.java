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
    public static final String PREFERENCES_FILE_NAME = "USER_CONFIG";
    public static final String PREFERENCE_INTERVAL = "notification_interval";

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

    private Interval interval = Interval.NEVER;
    private int oldSetting;

    public enum Interval {
        NEVER(0, R.id.interval_never, 0), INTERVAL_45SEC(1, R.id.interval_45sec, 45000),
        INTERVAL_2MIN(2, R.id.interval_2min, 2 * 60000), INTERVAL_5MIN(3, R.id.interval_5min, 5 * 60000),
        INTERVAL_10MIN(4, R.id.interval_10min, 10 *60000);
        public int id;
        public int type;
        public long value;
        private Interval(int type, int id, long value) {
            this.type = type;
            this.id = id;
            this.value = value;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            notifyService = (IChangeInterval)iBinder;
            notifyService.setInterval(interval.value);
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

        if (interval.type != 0) {
            bindService(new Intent("com.wetongji_android.service.WeNotificationService"), serviceConnection,
                    BIND_AUTO_CREATE);
        }

	}

    @Override
    protected void onDestroy() {
        if (interval.type > 0) {
            unbindService(serviceConnection);
        }
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
        rlChangeInterval = (RelativeLayout) findViewById(R.id.ll_setting_change_interval);
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
        int type = sp.getInt(PREFERENCE_INTERVAL, 0);
        setInterval(type);
        tvIntervalValue.setText(getResources().getStringArray(R.array.notification_interval_values)[type]);
        oldSetting = interval.type;
	}

    private void setInterval(int type) {
        switch (type) {
            case 0: {
                interval = Interval.NEVER;
                break;
            }
            case 1: {
                interval = Interval.INTERVAL_45SEC;
                break;
            }
            case 2: {
                interval = Interval.INTERVAL_2MIN;
                break;
            }
            case 3: {
                interval = Interval.INTERVAL_5MIN;
                break;
            }
            case 4: {
                interval = Interval.INTERVAL_10MIN;
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
                openIntervalDialog();
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
        dialog.setContentView(R.layout.dialog_notification_interval);
        dialog.setCanceledOnTouchOutside(true);
        RadioGroup rgInterval = (RadioGroup) dialog.findViewById(R.id.rg_dialog_notify_interval);

        rgInterval.check(interval.id);
        rgInterval.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                SharedPreferences.Editor editor = sp.edit();
                oldSetting = interval.type;
                switch (checkedId) {
                    case R.id.interval_never:
                        interval = Interval.NEVER;
                        break;
                    case R.id.interval_45sec:
                        interval = Interval.INTERVAL_45SEC;
                        break;
                    case R.id.interval_2min:
                        interval = Interval.INTERVAL_2MIN;
                        break;
                    case R.id.interval_5min:
                        interval = Interval.INTERVAL_5MIN;
                        break;
                    case R.id.interval_10min:
                        interval = Interval.INTERVAL_10MIN;
                        break;
                }
                editor.putInt(PREFERENCE_INTERVAL, interval.type);
                editor.apply();

                // set the Service
                setInterval(checkedId);
                if (interval.type == 0) {
                    stopService(new Intent(WTSettingActivity.this, WeNotificationService.class));
                } else if (interval.type > 0 && oldSetting > 0) {
                    bindService(new Intent("com.wetongji_android.service.WeNotificationService"), serviceConnection,
                            BIND_AUTO_CREATE);
                } else if (interval.type > 0 && oldSetting == 0) {
                    startService(new Intent(WTSettingActivity.this, WeNotificationService.class));
                }

                tvIntervalValue.setText(getResources().getStringArray(R.array.notification_interval_values)[interval.type]);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
