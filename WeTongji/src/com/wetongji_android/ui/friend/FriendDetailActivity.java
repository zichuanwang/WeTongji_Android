package com.wetongji_android.ui.friend;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
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
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_detail);
	}

	private void initWidget()
	{
		tvFriendWords = (TextView)findViewById(R.id.text_profile_words);
		tvFriendDepartment = (TextView)findViewById(R.id.text_profile_gender);
		ibFriend = (Button)findViewById(R.id.btn_profile_action);
		rlFriendList = (RelativeLayout)findViewById(R.id.ll_friend_detail_list);
		tvFriendNum = (TextView)findViewById(R.id.tv_detail_friend_num);
		ibFriendAdd = (ImageButton)findViewById(R.id.btn_detail_friend_add);
		rlPartEvents = (RelativeLayout)findViewById(R.id.ll_friend_detail_part_events);
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
}
