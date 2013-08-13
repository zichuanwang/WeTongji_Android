package com.wetongji_android.util.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.friend.FriendInviteActivity;
import com.wetongji_android.ui.friend.FriendListActivity;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class WTBaseDetailActivity extends SherlockFragmentActivity
{

	protected ViewStub mVsContent;
	private LinearLayout mLayoutBack;
	private ImageButton mBtnShare;
	private LinearLayout mLayoutBottomAB;
	private LinearLayout mLayoutInvite;
	private LinearLayout mLayoutFriends;
	private LinearLayout mLayoutAttend;
	private LinearLayout mLayoutBottomBlank;
	private TextView mTextView;
	
	public static final String IMAGE_URL = "ImageUrl";
	public static final String IMAGE_WIDTH = "ImageWidth";
	public static final String IMAGE_HEIGHT = "ImageHeight";
	public static final String CHILD_ID = "ChildId";
	public static final String CHILD_TYPE = "ChildType";
	
	private int iChildId;
	private boolean bSchedule;
	private String shareContent;
	private String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		iChildId = 0;
		bSchedule = false;
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
				showShareDialog(shareContent);
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
		mTextView = (TextView)findViewById(R.id.activity_detail_bottom_text);
		if(type.equals("CourseDetailActivity"))
		{
			mTextView.setText("AUDIT");
		}else
		{
			mTextView.setText("ATTEND");
		}
	}
	
	//Display the bottom action bar
	public void showBottomActionBar()
	{
		mLayoutBottomAB = (LinearLayout)findViewById(R.id.bottom_action_bar);
		mLayoutBottomAB.setVisibility(View.VISIBLE);
		mLayoutBottomBlank = (LinearLayout)findViewById(R.id.ll_base_detail_blank);
		mLayoutBottomBlank.setVisibility(View.VISIBLE);
		
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
	
	protected void setiChildId(int iChildId) 
	{
		this.iChildId = iChildId;
	}

	protected void setType(String type) 
	{
		this.type = type;
	}

	protected void setbSchedule(boolean bSchedule) {
		this.bSchedule = bSchedule;
	}

	protected void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	class BottomABClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			if(v.getId() == R.id.btn_event_detail_invite)
			{
				if(WTApplication.getInstance().hasAccount)
				{
					Intent intent = new Intent(WTBaseDetailActivity.this, FriendInviteActivity.class);
					intent.putExtra(CHILD_ID, iChildId);
					intent.putExtra(CHILD_TYPE, type);
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
					Intent intent = new Intent(WTBaseDetailActivity.this, FriendListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE, type);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID, String.valueOf(iChildId));
					intent.putExtras(bundle);
					startActivity(intent);
				}else
				{
					Toast.makeText(WTBaseDetailActivity.this, getResources().getText(R.string.no_account_error), 
							Toast.LENGTH_SHORT).show();
				}
			}else if(v.getId() == R.id.btn_event_detail_attend)
			{
				if(WTApplication.getInstance().hasAccount)
				{
					ApiHelper apiHelper = ApiHelper.getInstance(WTBaseDetailActivity.this);
					if(type.equals("EventDetailActivity")){
						getSupportLoaderManager().restartLoader(WTApplication.SCHEDUL_LOADER, 
								apiHelper.setActivityScheduled(bSchedule, iChildId), new LoadCallback());
					}else{
						getSupportLoaderManager().restartLoader(WTApplication.SCHEDUL_LOADER, 
								apiHelper.setCourseScheduled(bSchedule, iChildId), new LoadCallback());
					}
				}else
				{
					Toast.makeText(WTBaseDetailActivity.this, getResources().getText(R.string.no_account_error), 
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	class LoadCallback implements LoaderCallbacks<HttpRequestResult>{
		@Override
		public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
			return new NetworkLoader(WTBaseDetailActivity.this, HttpMethod.Get, arg1);
		}

		@Override
		public void onLoadFinished(Loader<HttpRequestResult> arg0,
				HttpRequestResult result) {
			if(result.getResponseCode() == 0){
				
			}
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
			
		}
	}
}
