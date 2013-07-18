package com.wetongji_android.ui.setting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.wetongji_android.R;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;

public class DevSettingActivity extends SherlockPreferenceActivity 
implements OnSharedPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.dev_settings);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharePreferences, String key) {
		
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals("pref_is_login_setting")) {
			logout();
		}
		return true;
	}

	private void logout() {
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		am.removeAccount(accounts[0], null, null);
		Intent intent = new Intent();
		intent.setClass(this, AuthActivity.class);
		startActivity(intent);
		finish();
	}
	
	

	
}
