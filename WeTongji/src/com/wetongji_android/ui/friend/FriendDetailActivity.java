package com.wetongji_android.ui.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.util.net.HttpRequestResult;

public class FriendDetailActivity extends SherlockFragmentActivity implements
		LoaderCallbacks<HttpRequestResult> 
{
	private TextView tvFriendWords;
	private TextView tvFriendDepartment;
	private Button ibFriend;
	private RelativeLayout rlFriendList;
	private TextView tvFriendNum;
	private ImageButton ibFriendAdd;
	private RelativeLayout rlPartEvents;
	private TextView tvEventsNum;
	private TextView tvMajor;
	private TextView tvGrade;
	private TextView tvEmail;
	
	private User mUser;
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_detail);
		
		receiveData();
		
		initWidget();
	}

	private void receiveData()
	{
		Intent intent = getIntent();
		mUser = intent.getExtras().getParcelable(FriendListFragment.BUNDLE_KEY_USER);
	}
	
	private void initWidget()
	{
		tvFriendWords = (TextView)findViewById(R.id.text_profile_words);
		tvFriendWords.setText(mUser.getWords());
		tvFriendDepartment = (TextView)findViewById(R.id.text_profile_gender);
		tvFriendDepartment.setText(mUser.getDepartment());
		ibFriend = (Button)findViewById(R.id.btn_profile_action);
		if(mUser.isIsFriend())
		{
			ibFriend.setText("Friend");
		}else
		{
			ibFriend.setText("UnFriend");
		}
		rlFriendList = (RelativeLayout)findViewById(R.id.ll_friend_detail_list);
		rlFriendList.setOnClickListener(new ClickListener());
		tvFriendNum = (TextView)findViewById(R.id.tv_detail_friend_num);
		ibFriendAdd = (ImageButton)findViewById(R.id.btn_detail_friend_add);
		ibFriendAdd.setOnClickListener(new ClickListener());
		rlPartEvents = (RelativeLayout)findViewById(R.id.ll_friend_detail_part_events);
		rlPartEvents.setOnClickListener(new ClickListener());
		tvEventsNum = (TextView)findViewById(R.id.tv_detail_friend_part_events_num);
		tvMajor = (TextView)findViewById(R.id.tv_friend_detail_major);
		tvMajor.setText(mUser.getMajor());
		tvGrade = (TextView)findViewById(R.id.tv_friend_detail_grade);
		tvGrade.setText(mUser.getYear());
		tvEmail = (TextView)findViewById(R.id.tv_friend_detail_email);
		tvEmail.setText(mUser.getEmail());
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			if(v.getId() == R.id.ll_friend_detail_list)
			{
				
			}else
			{
				
			}
		}
	}
}
