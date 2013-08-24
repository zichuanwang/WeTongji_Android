package com.wetongji_android.util.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.ui.account.AccountListFragment;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.informations.InformationsFragment;
import com.wetongji_android.ui.people.PeopleListFragment;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class WTLikeListActivity extends SherlockFragmentActivity 
{
	private Fragment fragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.content_frame);
		
		Bundle bundle = getIntent().getExtras();
		String type = bundle.getString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE);
		
		if(type.equals("Activity")){
			if(bundle.getBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, false)){
				fragment = EventsFragment.newInstance(StartMode.LIKE, bundle);
			}else{
				fragment = EventsFragment.newInstance(StartMode.USERS, bundle);
			}
		}else if(type.equals("Information")){
			if(bundle.getBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, false)){
				fragment = InformationsFragment.newInstance(StartMode.LIKE, bundle);
			}else{
				fragment = InformationsFragment.newInstance(StartMode.USERS, bundle);
			}
		}else if(type.equals("People")){
			if(bundle.getBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, false)){
				fragment = PeopleListFragment.newInstance(StartMode.LIKE, bundle);
			}else{
				fragment = PeopleListFragment.newInstance(StartMode.USERS, bundle);
			}
		}else if(type.equals("Account")){
			if(bundle.getBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, false)){
				fragment = AccountListFragment.newInstance(StartMode.LIKE, bundle);
			}else{
				fragment = AccountListFragment.newInstance(StartMode.USERS, bundle);
			}
		}
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.content_frame, fragment).commit();
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
