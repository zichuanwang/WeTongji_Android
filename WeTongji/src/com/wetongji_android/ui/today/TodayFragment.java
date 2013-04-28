package com.wetongji_android.ui.today;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.util.adapter.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TodayFragment extends Fragment 
{
	private static int[] pics = {
		R.drawable.img_today_1,
		R.drawable.img_today_2,
		R.drawable.img_today_3,
	};
	
	private List<View> views = new ArrayList<View>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		if(container == null)
			return null;
		
		for(int i = 0; i < pics.length; i++)
		{
			ImageView image = new ImageView(getActivity());
			image = (ImageView)inflater.inflate(R.layout.view_page_intro, null);
			image.setBackgroundResource(pics[i]);
			views.add(image);
		}
		
		return inflater.inflate(R.layout.today_frame, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		ViewPager vp = (ViewPager)getActivity().findViewById(R.id.vp_today);
		vp.setAdapter(new ViewPagerAdapter(views));
		
		UnderlinePageIndicator indicator = (UnderlinePageIndicator)getActivity().findViewById(R.id.vp_indicator_today);
		indicator.setViewPager(vp);
		indicator.setFades(false);
	}

	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
}
