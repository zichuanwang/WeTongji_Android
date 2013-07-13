package com.wetongji_android.util.common;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;

public class WTBaseDetailActivity extends SherlockActivity 
{

	protected ViewStub mVsContent;
	private LinearLayout mLayoutBack;
	private ImageButton mBtnShare;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void setContentView(int layoutResId) 
	{
		super.setContentView(R.layout.activity_detail_frame);
		mVsContent = (ViewStub) findViewById(R.id.stub);
		mVsContent.setLayoutResource(layoutResId);
		mVsContent.inflate();
		
		//Set up the back icon event
		mLayoutBack = (LinearLayout) findViewById(R.id.event_detail_back);
		mLayoutBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				finish();
			}
		});
		
		//Set up the share event
		mBtnShare = (ImageButton)findViewById(R.id.action_event_detail_share);
		mBtnShare.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
}
