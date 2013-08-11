package com.wetongji_android.util.common;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;

public class WTAboutActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		
		setUpUI();
	}
	
	private void setUpUI(){
		TextView tvVersionNumber = (TextView) findViewById(R.id.text_about_version);
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
	}
}
