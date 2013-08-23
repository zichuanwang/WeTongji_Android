package com.wetongji_android.ui.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseFragment;
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
		}else if(b.getBoolean(WTBaseFragment.BUNDLE_KEY_ACCOUNT, false))
		{
			f = EventsFragment.newInstance(StartMode.ATTEND, b);
		} else if(b.getString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE).equals("TODAY")) {
			f = EventsFragment.newInstance(StartMode.TODAY, b);
		} else {
			f = EventsFragment.newInstance(StartMode.USERS, b);
		}
		
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.content_frame, f).commit();
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
