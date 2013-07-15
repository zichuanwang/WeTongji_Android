package com.wetongji_android.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.User;

public class ProfileUpdateActivity extends SherlockActivity implements OnClickListener {

	private EditText mEtPhone;
	private EditText mEtEmail;
	private EditText mEtQQ;
	private EditText mEtWeibo;
	private EditText mEtDorm;
	
	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile_update);
		setUpUI();
		setUpActionBar();
	}
	
	private void setUpUI() {
		mUser = getIntent().getParcelableExtra(ProfileFragment.BUNDLE_USER);
		mEtPhone = (EditText) findViewById(R.id.et_profile_phone);
		mEtEmail = (EditText) findViewById(R.id.et_profile_email);
		mEtQQ = (EditText) findViewById(R.id.et_profile_qq);
		mEtWeibo = (EditText) findViewById(R.id.et_profile_weibo);
		mEtDorm = (EditText) findViewById(R.id.et_profile_dorm);
		
		if (mUser != null) {
			mEtPhone.setText(mUser.getPhone());
			mEtEmail.setText(mUser.getEmail());
			mEtQQ.setText(mUser.getQQ());
			mEtWeibo.setText(mUser.getSinaWeibo());
			mEtDorm.setText(mUser.getRoom());
		}
	}
	
	private void setUpActionBar() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		
		View v = getLayoutInflater().inflate(R.layout.actionbar_update_profile, null);
		ab.setCustomView(v);
		Button btnCancel = (Button) v.findViewById(R.id.btn_ac_cancel_update);
		Button btnSave = (Button) v.findViewById(R.id.btn_ac_save_profile);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btn_ac_cancel_update) {
			finish();
		} else if (view.getId() == R.id.btn_ac_save_profile) {
			updateProfile();
		}
	}

	private void updateProfile() {
		// TODO Auto-generated method stub
		
	}
	
}
