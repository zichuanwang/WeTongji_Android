package com.wetongji_android.ui.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class EventsListActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.content_frame);
		
		Fragment f;
		Bundle b = getIntent().getExtras();
		if(b.getBoolean(EventsFragment.BUNDLE_KEY_LIKE, false))
		{
			f = EventsFragment.newInstance(StartMode.LIKE, b);
		}else
		{
			f = EventsFragment.newInstance(StartMode.USERS, b);
		}
		
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.content_frame, f).commit();
	}
	
}
