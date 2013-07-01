package com.wetongji_android.ui.friend;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;

public class FriendListActivity extends SherlockFragmentActivity 
{
	private static final String TAG_FRIEND_FRAGMENT = "FRIEND_FRAGMENT";
		
	@Override
	protected void onCreate(Bundle arg0) 
	{
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_friend_list);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.img_wt_logo);
		
		FriendListFragment fragment = new FriendListFragment();
		getSupportFragmentManager().beginTransaction()
			.add(R.id.friend_list_container, fragment, TAG_FRIEND_FRAGMENT)
			.commit();
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
}
