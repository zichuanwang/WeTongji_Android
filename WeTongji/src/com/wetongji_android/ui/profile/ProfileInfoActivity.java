package com.wetongji_android.ui.profile;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.util.date.DateParser;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.content.Intent;

public class ProfileInfoActivity extends SherlockFragmentActivity {

	public static final int REQUEST_CODE_EDIT_PROFILE = 9201;
	
	private User mUser;
	
	private TextView tvBirthday;
	private TextView tvStudentNo;
	private TextView tvMajor;
	private TextView tvMotto;
	private TextView tvPhone;
	private TextView tvEmail;
	private TextView tvQQ;
	private TextView tvWeibo;
	private TextView tvDorm;
	
	
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
		//recieve user
		mUser = getIntent().getParcelableExtra(ProfileFragment.BUNDLE_USER);
		
		tvBirthday = (TextView) findViewById(R.id.text_profile_birthday);
		tvStudentNo = (TextView) findViewById(R.id.text_profile_student_no);
		tvMajor = (TextView) findViewById(R.id.text_profile_major);
		tvMotto = (TextView) findViewById(R.id.text_profile_motto);
		tvPhone = (TextView) findViewById(R.id.text_profile_phone);
		tvEmail = (TextView) findViewById(R.id.text_profile_email);
		tvQQ = (TextView) findViewById(R.id.text_profile_qq);
		tvWeibo = (TextView) findViewById(R.id.text_profile_weibo);
		tvDorm = (TextView) findViewById(R.id.text_profile_dorm);
		
		setUserData();
	}

	
	private void setUserData() {
		tvBirthday.setText(DateParser.parseDateFromString(mUser.getBirthday(), this));
		tvStudentNo.setText(mUser.getNO());
		tvMajor.setText(mUser.getMajor());
		tvMotto.setText(mUser.getWords());
		tvPhone.setText(mUser.getPhone());
		tvEmail.setText(mUser.getEmail());
		tvQQ.setText(mUser.getQQ());
		tvWeibo.setText(mUser.getSinaWeibo());
		tvDorm.setText(mUser.getRoom());		
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_profile_info, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home: {
			// send back motto to previous screen
			Intent intent = new Intent();
			intent.putExtra(ProfileFragment.BUNDLE_MOTTO, mUser.getWords());
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		case R.id.action_profile_edit: {
			Intent intent = new Intent(this, ProfileUpdateActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(ProfileFragment.BUNDLE_USER, mUser);
			intent.putExtras(bundle);
			startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		}
		}

		return super.onOptionsItemSelected(item);
	}

	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// send back motto to previous screen
			Intent intent = new Intent();
			intent.putExtra(ProfileFragment.BUNDLE_MOTTO, mUser.getWords());
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == RESULT_OK) {
			mUser = data.getParcelableExtra(ProfileFragment.BUNDLE_USER);
			setUserData();
		}
	}
	
	
}
