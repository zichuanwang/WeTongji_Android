package com.wetongji_android.ui.setting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.wetongji_android.R;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;

public class SettingActivity extends SherlockPreferenceActivity 
implements OnSharedPreferenceChangeListener, OnPreferenceClickListener{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		getListView().setBackgroundResource(R.drawable.bg_app_base);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharePreferences, String key) {
		if (key.equals("pref_user_test_server")) {
			
		}
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
		if (accounts.length != 0) {
			am.removeAccount(accounts[0], null, null);
		}
		Intent intent = new Intent();
		intent.setClass(this, AuthActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		return false;
	}
}
