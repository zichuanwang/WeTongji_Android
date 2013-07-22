package com.wetongji_android.ui.profile;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.util.date.DateParser;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

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
		//recieve user
		mUser = getIntent().getParcelableExtra(ProfileFragment.BUNDLE_USER);
		
		TextView tvBirthday = (TextView) findViewById(R.id.text_profile_birthday);
		TextView tvStudentNo = (TextView) findViewById(R.id.text_profile_student_no);
		TextView tvMajor = (TextView) findViewById(R.id.text_profile_major);
		TextView tvMotto = (TextView) findViewById(R.id.text_profile_motto);
		TextView tvPhone = (TextView) findViewById(R.id.text_profile_phone);
		TextView tvEmail = (TextView) findViewById(R.id.text_profile_email);
		TextView tvQQ = (TextView) findViewById(R.id.text_profile_qq);
		TextView tvWeibo = (TextView) findViewById(R.id.text_profile_weibo);
		TextView tvDorm = (TextView) findViewById(R.id.text_profile_dorm);
		
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
			finish();
			break;
		}
		case R.id.action_profile_edit: {
			Intent intent = new Intent(this, ProfileUpdateActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(ProfileFragment.BUNDLE_USER, mUser);
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
			break;
		}
		}

		return super.onOptionsItemSelected(item);
	}
	
	
}
