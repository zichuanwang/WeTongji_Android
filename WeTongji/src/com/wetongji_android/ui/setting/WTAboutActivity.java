package com.wetongji_android.ui.setting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;

public class WTAboutActivity extends SherlockFragmentActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		
		setUpUI();
	}
	
	private void setUpUI(){
		// set ActionBar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		TextView tvVersionNumber = (TextView) findViewById(R.id.text_about_version);
		RelativeLayout rlRate = (RelativeLayout) findViewById(R.id.ll_about_rate);
		RelativeLayout rlShare = (RelativeLayout) findViewById(R.id.ll_about_share);
		RelativeLayout rlFeedback = (RelativeLayout) findViewById(R.id.ll_about_feedback);
		RelativeLayout rlWebsite = (RelativeLayout) findViewById(R.id.ll_about_visit);
		TextView tvTermsOfUse = (TextView) findViewById(R.id.text_about_terms);
		
		// set version information
		PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
		} catch (NameNotFoundException e) {
		}
        String versionName = packInfo.versionName;
		String version = getString(R.string.wetongji_version_number) + " "
				+ versionName;
		tvVersionNumber.setText(version);
		
		// set OnClickListener
		rlRate.setOnClickListener(this);
		rlShare.setOnClickListener(this);
		rlFeedback.setOnClickListener(this);
		rlWebsite.setOnClickListener(this);
		tvTermsOfUse.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_about_rate:
			final Uri uri = Uri.parse("market://details?id="
					+ getApplicationContext().getPackageName());
			final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
			if (getPackageManager().queryIntentActivities(rateAppIntent, 0)
					.size() > 0) {
				startActivity(rateAppIntent);
			} else {
				Toast.makeText(this, R.string.toast_no_market, Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.ll_about_share:
			showShareDialog();
			break;
		case R.id.ll_about_feedback:
			Uri emailUri = Uri.parse("mailto:wetongji2012@gmail.com");
	        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);                                   
	        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WeTongji Android Feedbacks");
	        try {
	        	startActivity(emailIntent);
	        } catch (ActivityNotFoundException e) {
	        	Toast.makeText(this, R.string.toast_no_email_app, Toast.LENGTH_LONG).show();
	        }
			break;
		case R.id.ll_about_visit:
			String url = "http://we.tongji.edu.cn";
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
			break;
		case R.id.text_about_terms:
			Intent termsIntent = new Intent(this, WTTermsOfUseActivity.class);
			startActivity(termsIntent);
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showShareDialog() {
		String sourceDesc = getResources().getString(R.string.share_wetongji);
		String share = getResources().getString(R.string.test_share);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_TEXT, sourceDesc);
		intent.setType("text/*");
		intent.setType("image/*");
		startActivity(Intent.createChooser(intent, share));
	}
}
