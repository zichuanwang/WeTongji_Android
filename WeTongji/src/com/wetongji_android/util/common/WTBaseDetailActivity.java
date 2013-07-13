package com.wetongji_android.util.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;
import com.wetongji_android.ui.friend.FriendInviteActivity;

public class WTBaseDetailActivity extends SherlockActivity 
{

	protected ViewStub mVsContent;
	private LinearLayout mLayoutBack;
	private ImageButton mBtnShare;
	private LinearLayout mLayoutBottomAB;
	private LinearLayout mLayoutInvite;
	private LinearLayout mLayoutFriends;
	private LinearLayout mLayoutAttend;
	
	public static final String IMAGE_URL = "ImageUrl";
	public static final String IMAGE_WIDTH = "ImageWidth";
	public static final String IMAGE_HEIGHT = "ImageHeight";
	
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
		
		setTopActionBar();
	}
	
	private void setTopActionBar()
	{
		//Set up the back icon event
		mLayoutBack = (LinearLayout) findViewById(R.id.detail_back);
		mLayoutBack.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				finish();
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_right_out);
			}
		});
		
		//Set up the share event
		mBtnShare = (ImageButton)findViewById(R.id.action_detail_share);
		mBtnShare.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				showShareDialog("Coming From WeTongji...");
			}
		});
	}
	
	//Set up the bottom action bar event handling
	private void setBottomActionBar()
	{
		mLayoutInvite = (LinearLayout)findViewById(R.id.btn_event_detail_invite);
		mLayoutInvite.setOnClickListener(new BottomABClickListener());
		mLayoutFriends = (LinearLayout)findViewById(R.id.btn_event_detail_friends);
		mLayoutFriends.setOnClickListener(new BottomABClickListener());
		mLayoutAttend = (LinearLayout)findViewById(R.id.btn_event_detail_attend);
		mLayoutAttend.setOnClickListener(new BottomABClickListener());
	}
	
	//Display the bottom action bar
	public void showBottomActionBar()
	{
		mLayoutBottomAB = (LinearLayout)findViewById(R.id.bottom_action_bar);
		mLayoutBottomAB.setVisibility(View.VISIBLE);
		
		setBottomActionBar();
	}
	
	private void showShareDialog(String content) 
	{
		String sourceDesc = getResources().getString(R.string.share_from_we);
		String share = getResources().getString(R.string.test_share);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_TEXT, content + sourceDesc);
		intent.setType("text/*");
		intent.setType("image/*");
		startActivity(Intent.createChooser(intent, share));
	}
	
	class BottomABClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			if(v.getId() == R.id.btn_event_detail_invite)
			{
				if(WTApplication.getInstance().hasAccount)
				{
					Intent intent = new Intent(WTBaseDetailActivity.this, FriendInviteActivity.class);
					startActivity(intent);
				}else
				{
					Toast.makeText(WTBaseDetailActivity.this, getResources().getText(R.string.no_account_error), 
							Toast.LENGTH_SHORT).show();
				}
			}else if(v.getId() == R.id.btn_event_detail_friends)
			{
				if(WTApplication.getInstance().hasAccount)
				{
					Toast.makeText(WTBaseDetailActivity.this, getResources().getText(R.string.no_account_error), 
							Toast.LENGTH_SHORT).show();
				}else
				{
					
				}
			}else if(v.getId() == R.id.btn_event_detail_attend)
			{
				if(WTApplication.getInstance().hasAccount)
				{
					Toast.makeText(WTBaseDetailActivity.this, getResources().getText(R.string.no_account_error), 
							Toast.LENGTH_SHORT).show();
				}else
				{
					
				}
			}
		}
	}
}
