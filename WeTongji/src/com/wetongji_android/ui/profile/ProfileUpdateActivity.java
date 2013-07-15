package com.wetongji_android.ui.profile;

import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.User;

public class ProfileUpdateActivity extends SherlockActivity {

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
	}
	
	private void setUpUI() {
		mEtPhone = (EditText) findViewById(R.id.et_profile_phone);
		mEtEmail = (EditText) findViewById(R.id.et_profile_email);
		mEtQQ = (EditText) findViewById(R.id.et_profile_qq);
		mEtWeibo = (EditText) findViewById(R.id.et_profile_weibo);
		mEtDorm = (EditText) findViewById(R.id.et_profile_dorm);
		
		//mEtPhone.setText(text)
	}
	
}
