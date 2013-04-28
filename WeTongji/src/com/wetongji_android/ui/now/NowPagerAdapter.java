package com.wetongji_android.ui.now;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NowPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;
	
	public NowPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments=new ArrayList<Fragment>();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	public int addPage(Fragment fragment, boolean isAppend) {
        if(isAppend){
        	fragments.add(fragment);
        	notifyDataSetChanged();
        	return getCount()-1;
        }
        else{
        	fragments.add(0, fragment);
        	notifyDataSetChanged();
        	return 0;
        }
    }

}
