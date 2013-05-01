package com.wetongji_android.ui.event;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Activity;

public class EventDetailActivity extends android.app.Activity{
	
	private Activity mActivity;
	
	private CheckBox mCbLike;
	private TextView mTvLikeNum;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		recieveActivity();
		setUpUI();
		
		
	}
	
	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_event);
		
		mCbLike = (CheckBox)findViewById(R.id.cb_event_like);
		mTvLikeNum = (TextView)findViewById(R.id.tv_event_like_number);
		mCbLike.setChecked(!mActivity.isCanLike());
		mTvLikeNum.setText(String.valueOf(mActivity.getLike()));
		
		
		
	}
	
	private void recieveActivity() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mActivity = (Activity)bundle.getSerializable(EventsFragment.BUNDLE_KEY_ACTIVITY);
	}
	
	
	
}
