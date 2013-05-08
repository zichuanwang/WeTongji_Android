package com.wetongji_android.ui.today;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class TodayBannerPagerAdapter extends PagerAdapter 
{
	public TodayBannerPagerAdapter(List<View> views) 
	{
		super();
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) 
	{
		// TODO Auto-generated method stub
		return (arg0 == arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
