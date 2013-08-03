package com.wetongji_android.ui.people;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseFragment.StartMode;

public class PeopleListActivity extends SherlockFragmentActivity{

	private static final String TAG_ORG_FRAGMENT = "TAG_ORG_FRAGMENT";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_people_list);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.img_wt_logo);
		
		PeopleListFragment fragment = PeopleListFragment.newInstance(StartMode.BASIC, null);
		getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, fragment, TAG_ORG_FRAGMENT)
			.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
}
