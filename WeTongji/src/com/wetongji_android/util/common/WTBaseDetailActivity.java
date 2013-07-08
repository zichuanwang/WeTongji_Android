package com.wetongji_android.util.common;

import android.os.Bundle;
import android.view.ViewStub;
import android.view.Window;
import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;

public class WTBaseDetailActivity extends SherlockActivity {

	protected ViewStub mVsContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void setContentView(int layoutResId) {
		super.setContentView(R.layout.activity_detail_frame);
		mVsContent = (ViewStub) findViewById(R.id.stub);
		mVsContent.setLayoutResource(layoutResId);
		mVsContent.inflate();
	}
	
}
