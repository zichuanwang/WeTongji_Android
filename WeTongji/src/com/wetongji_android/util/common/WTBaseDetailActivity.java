package com.wetongji_android.util.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public abstract class WTBaseDetailActivity extends SherlockFragmentActivity
{
	public static final String IMAGE_URL = "ImageUrl";
	public static final String IMAGE_WIDTH = "ImageWidth";
	public static final String IMAGE_HEIGHT = "ImageHeight";
	public static final String CHILD_ID = "ChildId";
	public static final String CHILD_TYPE = "ChildType";
	public static final String KEY_OBJECT_ID = "key_object_id";
	public static final String KEY_CAN_LIKE = "key_object_can_like";
	public static final String KEY_LIKE_NUMBER = "key_object_like_num";
	public static final String BUNDLE_KEY_ACCOUNT = "BUNDLE_KEY_ACCOUNT";
	
	protected ViewStub mVsContent;
	private LinearLayout mLayoutBack;
	private ImageButton mBtnShare;
	private LinearLayout mLayoutBottomAB;
	private LinearLayout mLayoutInvite;
	private LinearLayout mLayoutFriends;
	private LinearLayout mLayoutAttend;
	private LinearLayout mLayoutBottomBlank;
	private TextView mTextView;
	private CheckBox mCbLike;
	private TextView mTvLikeNumber;
	private TextView mTvInvite;
	private ImageView mIvSchedule;
	private TextView mTvFriendsNumber;
	
	private String iChildId;
	private boolean bSchedule;
	private String shareContent;
	private boolean canLike;
	private int like;
	private String modelType;
	private int iFriendsCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
		
		// set up the like CheckBox
		mTvLikeNumber = (TextView) findViewById(R.id.tv_like_number);
		mTvLikeNumber.setText(String.valueOf(like));
		
		mCbLike = (CheckBox) findViewById(R.id.cb_like);
		mCbLike.setChecked(!canLike);
		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if (WTApplication.getInstance().hasAccount) {
					setContentLiked(checked);
					like += (checked ? 1 : -1);
					canLike = !canLike;
					mTvLikeNumber.setText(String.valueOf(like));
				} else {
					mCbLike.setChecked(!checked);
					Toast.makeText(WTBaseDetailActivity.this,
							getResources().getString(
									R.string.need_account_login),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	//Set up the bottom action bar event handling
	@SuppressWarnings("deprecation")
	private void setBottomActionBar()
	{
		//Set the invite tab
		mLayoutInvite = (LinearLayout)findViewById(R.id.btn_event_detail_invite);
		mTvInvite = (TextView)findViewById(R.id.tv_event_detail_friends_invite);
		if(bSchedule){
			mLayoutInvite.setBackgroundColor(getResources().getColor(R.color.bg_now_tab));
			mLayoutInvite.setClickable(false);
			mTvInvite.setTextColor(getResources().getColor(R.color.tv_friend_can_not_invite));
		}else{
			mLayoutInvite.setOnClickListener(new BottomABClickListener());
		}
		
		//Set the friends tab
		mLayoutFriends = (LinearLayout)findViewById(R.id.btn_event_detail_friends);
		mLayoutFriends.setOnClickListener(new BottomABClickListener());
		mTvFriendsNumber = (TextView)findViewById(R.id.tv_event_detail_friends);
		if(WTApplication.getInstance().hasAccount) {
			mTvFriendsNumber.setText(String.valueOf(iFriendsCount));
		} else {
			mTvFriendsNumber.setText("0");
		}
		
		//Set the schedule tab
		mLayoutAttend = (LinearLayout)findViewById(R.id.btn_event_detail_attend);
		mLayoutAttend.setOnClickListener(new BottomABClickListener());
		mTextView = (TextView)findViewById(R.id.activity_detail_bottom_text);
		if(modelType.equals("Course") && bSchedule)
		{
			mTextView.setText("AUDIT");
		}else if(modelType.equals("Activity") && !bSchedule){
			mTextView.setText("ATTENDED");
			mTextView.setTextColor(getResources().getColor(R.color.tv_event_detail_location));
		}else if(modelType.equals("Course") && !bSchedule){
			mTextView.setText("REGISTERED");
		}
		mIvSchedule = (ImageView)findViewById(R.id.tv_event_detail_schedule);
		if(!bSchedule){
			mIvSchedule.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_event_detail_attended));
		}else{
			mIvSchedule.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_event_detail_attend));
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
	
	//Update the bottom action bar when the schedule is clicked
	@SuppressWarnings("deprecation")
	private void updateBottomActionBar(){
		if(bSchedule){
			if(modelType.equals("Course")){
				mTextView.setText("REGISTERED");
			}else{
				mTextView.setText("ATTENDED");
			}
			mTextView.setTextColor(getResources().getColor(R.color.tv_event_detail_location));
			mIvSchedule.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_event_detail_attended));
			mLayoutInvite.setBackgroundColor(getResources().getColor(R.color.bg_event_detail_bottom_actionbar));
			mLayoutInvite.setClickable(true);
			mLayoutInvite.setOnClickListener(new BottomABClickListener());
			mTvInvite.setTextColor(getResources().getColor(android.R.color.white));
		}else{
			mTextView.setText("ATTEND");
			mTextView.setTextColor(getResources().getColor(android.R.color.white));
			mIvSchedule.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_event_detail_attend));
			mLayoutInvite.setBackgroundColor(getResources().getColor(R.color.bg_now_tab));
			mLayoutInvite.setClickable(false);
			mTvInvite.setTextColor(getResources().getColor(R.color.tv_friend_can_not_invite));
		}
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
	
	protected void setiChildId(String iChildId) 
	{
		this.iChildId = iChildId;
	}

	protected void setbSchedule(boolean bSchedule) {
		this.bSchedule = bSchedule;
	}

	protected void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}
	
	protected boolean isCanLike() {
		return canLike;
	}

	protected void setCanLike(boolean canLike) {
		this.canLike = canLike;
	}

	protected int getLike() {
		return like;
	}

	protected void setLike(int like) {
		this.like = like;
	}
	
	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public void setiFriendsCount(int iFriendsCount) {
		this.iFriendsCount = iFriendsCount;
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
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID, iChildId);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE, modelType);
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE, modelType);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID, String.valueOf(iChildId));
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
					if(modelType.equals("Activity")){
						getSupportLoaderManager().restartLoader(WTApplication.SCHEDUL_LOADER, 
								apiHelper.setActivityScheduled(bSchedule, String.valueOf(iChildId)), new LoadCallback());
					}else{
						getSupportLoaderManager().restartLoader(WTApplication.SCHEDUL_LOADER, 
								apiHelper.setCourseScheduled(bSchedule, String.valueOf(iChildId)), new LoadCallback());
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
				updateBottomActionBar();
			}
		}

		@Override
		public void onLoaderReset(Loader<HttpRequestResult> arg0) {
			
		}
	}
	
	protected void setContentLiked(boolean like) {
		ApiHelper apiHelper = ApiHelper.getInstance(this);
		getSupportLoaderManager().initLoader(WTApplication.NETWORK_LOADER_LIKE, 
				apiHelper.setObjectLikedWithModelType(like, iChildId, modelType), new LoadCallback() {
			@Override
			public void onLoadFinished(Loader<HttpRequestResult> arg0,
					HttpRequestResult result) {
				if (result.getResponseCode() == 0){
					Toast.makeText(WTBaseDetailActivity.this,
							R.string.toast_like_success,
							Toast.LENGTH_SHORT).show();
					updateObjectInDB();
				} else {
					Toast.makeText(WTBaseDetailActivity.this,
							R.string.toast_like_failed,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void finish() {
		Intent intent = getIntent();
		intent.putExtra(KEY_OBJECT_ID, iChildId);
		intent.putExtra(KEY_CAN_LIKE, canLike);
		intent.putExtra(KEY_LIKE_NUMBER, like);
		setResult(RESULT_OK, intent);
		
		super.finish();
	}
	
	@Override
	public void setTitle(CharSequence title) {
		TextView tvTitle = (TextView) findViewById(R.id.text_actionbar_titile);
		tvTitle.setText(title);
	}

	abstract protected void updateObjectInDB();
}
