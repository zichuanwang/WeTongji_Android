package com.wetongji_android.ui.profile;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.R.layout;
import com.wetongji_android.R.menu;
import com.wetongji_android.data.User;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ProfileInfoActivity extends SherlockFragmentActivity {

	private User mUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_info);
		
		setUpActionBar();
		setUpUI();
	}
	
	private void setUpActionBar() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
	}

	
	private void setUpUI() {
		mUser = getIntent().getParcelableExtra(ProfileFragment.BUNDLE_USER);
		Toast.makeText(this, "Hello " + mUser.getName(), Toast.LENGTH_LONG).show();
	}
}
