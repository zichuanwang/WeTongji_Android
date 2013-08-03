package com.wetongji_android.util.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.informations.InformationsFragment;
import com.wetongji_android.ui.people.PeopleListFragment;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class WTLikeListActivity extends SherlockFragmentActivity 
{
	private Fragment fragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
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
		}else if(type.equals("Info")){
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
		}else{
			
		}
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.content_frame, fragment).commit();
	}
}
