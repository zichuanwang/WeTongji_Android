package com.wetongji_android.ui.friend;

import android.os.Bundle;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class FriendListActivity extends SherlockFragmentActivity 
{
	private static final String TAG_FRIEND_FRAGMENT = "FRIEND_FRAGMENT";
	public static final String TAG_COURSE = "COURSE_DETAIL";
	
	private FriendListFragment fragment;
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		setContentView(R.layout.activity_friend_list);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.img_wt_logo);
		
		Bundle bundle = getIntent().getExtras();
		String type = bundle.getString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE);
		
		if(type.equals("ProfileFragment"))
		{
			fragment = FriendListFragment.newInstance(StartMode.BASIC, bundle);
		}else if(type.equals("FriendDetailActivity"))
		{
			fragment = FriendListFragment.newInstance(StartMode.FRIENDS, bundle);
		}else if(type.equals("EventDetailActivity"))
		{
			bundle.putBoolean(TAG_COURSE, true);
			fragment = FriendListFragment.newInstance(StartMode.ATTEND, bundle);
		}else if(type.equals("User"))
		{
			fragment = FriendListFragment.newInstance(StartMode.LIKE, bundle);
		}
		
		getSupportFragmentManager().beginTransaction()
			.add(R.id.friend_list_container, fragment, TAG_FRIEND_FRAGMENT)
			.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if (item.getItemId() == android.R.id.home) 
		{
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		return super.onOptionsItemSelected(item);
	}
}
