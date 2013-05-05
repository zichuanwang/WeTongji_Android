package com.wetongji_android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTApplication;

public class IntroActivity extends SherlockFragmentActivity implements OnCheckedChangeListener, OnClickListener{

	private ToggleButton btnOnLogin;
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_authenticator);
		getSupportFragmentManager().beginTransaction()
			.add(R.id.auth_content_container, IntroFragment.newInstance())
			.commit();
		setupActionBar();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		ActionBar ab=getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		View v=getLayoutInflater().inflate(R.layout.actionbar_authenticator, null);
		ab.setCustomView(v);
		btnOnLogin=(ToggleButton) v.findViewById(R.id.btn_on_login);
		btnOnLogin.setOnCheckedChangeListener(this);
		Button btnNotNow=(Button) v.findViewById(R.id.btn_not_now);
		btnNotNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent intent=new Intent(this, AuthActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up_in, R.anim.push_out);
		finish();
	}
	
}
