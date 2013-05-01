package com.wetongji_android.ui.main;

import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.slidingmenu.lib.SlidingMenu;
import com.wetongji_android.R;
import com.wetongji_android.data.Event;
import com.wetongji_android.ui.auth.AuthenticatorActivity;
import com.wetongji_android.ui.today.TodayFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.version.UpdateBaseActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainActivity extends UpdateBaseActivity 
implements OnWTListClickedListener
{
	private Fragment mContent;
	public static final String PARAM_PREVIEW_WITHOUT_lOGIN="previewWithoutLogin";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// 主视图
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new TodayFragment();
		
		if(!getIntent().getBooleanExtra(PARAM_PREVIEW_WITHOUT_lOGIN, false)){
			checkAccount();
		}
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, mContent)
			.commit();
		
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
		// 设置侧边栏
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MainMenuFragment())
		.commit();
		
		//setTitle();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// 定制SlidingMenu样式
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
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
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	private void checkAccount(){
		AccountManager am=AccountManager.get(this);
		Account[] accounts=am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if(accounts.length==0){
			Intent intent=new Intent(this, AuthenticatorActivity.class);
			intent.putExtra(AuthenticatorActivity.PARAM_SHOW_INTRO, true);
			startActivity(intent);
		}
		else{
			checkUpdate();
		}
	}
	
	@Override
	public void onEventClicked(Event event) {
		// TODO Auto-generated method stub
		
	}

}
