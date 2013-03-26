package com.wetongji_android.ui.main;

import com.actionbarsherlock.view.MenuItem;
import com.androidquery.service.MarketService;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.ui.test.TestFragment;
import com.wetongji_android.ui.today.TodayFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MainActivity extends SlidingFragmentActivity 
{
	private Fragment mContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// 主视图
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			//mContent = new TodayFragment(android.R.color.darker_gray);
			mContent = new TestFragment();
		
		setContentView(R.layout.activity_main);
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, mContent)
			.commit();
		
		setSlidingMenu();
		
		//getSupportLoaderManager().initLoader(0, savedInstanceState, null);
	}



	/**
	 * check if there's a new version of the app.
	 * Update info comes from Google Play.
	 */
	private void checkAppUpdate() 
	{
		MarketService marketService=new MarketService(this);
		marketService.level(MarketService.REVISION).checkVersion();
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
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 初始化SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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

	@Override
	protected void onStart() {
		super.onStart();
		//startService(new Intent(this, WTUpdateService.class));
		checkAppUpdate();
	}

}
