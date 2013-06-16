package com.wetongji_android.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.slidingmenu.lib.SlidingMenu;
import com.wetongji_android.R;
import com.wetongji_android.ui.notification.NotificationFragment;
import com.wetongji_android.ui.now.NowFragment;
import com.wetongji_android.ui.profile.ProfileFragment;
import com.wetongji_android.ui.today.TodayFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.version.UpdateBaseActivity;

public class MainActivity extends UpdateBaseActivity {
	private Fragment mContent;
	public static final String PARAM_PREVIEW_WITHOUT_lOGIN = "previewWithoutLogin";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new TodayFragment();

		// set the above view
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		setSlidingMenu();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	/**
	 * set attributes of the sliding menu
	 */
	private void setSlidingMenu() {
		// set slidingmenu properties
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		// set left menu
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

		// set right menu
		sm.setSecondaryMenu(R.layout.right_menu);
		sm.setSecondaryShadowDrawable(R.drawable.shadow);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.right_menu, new NotificationFragment()).commit();

		// setTitle();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	public void switchContent(Fragment fragment) {
		if((fragment instanceof NowFragment) || (fragment instanceof ProfileFragment))
		{
			if(WTApplication.getInstance().hasAccount)
			{
				mContent = fragment;
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}else
			{
				Toast.makeText(this, getResources().getText(R.string.no_account_error), Toast.LENGTH_SHORT).show();
			}
		}else
		{
			mContent = fragment;
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isMenuShowing()) {
				finish();
			} else {
				getSlidingMenu().showMenu(true);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void showRightMenu()
	{
		if(getSlidingMenu().isSecondaryMenuShowing())
		{
			getSlidingMenu().showContent();
		}else
		{
			showSecondaryMenu();
		}
	}
}
