package com.wetongji_android.ui.today;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.util.adapter.GridViewAdapter;
import com.wetongji_android.util.adapter.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

public class TodayFragment extends Fragment 
{
	private static int[] pics = {
		R.drawable.img_today_1,
		R.drawable.img_today_2,
		R.drawable.img_today_3,
	};
	
	private List<View> views = new ArrayList<View>();
	private View mView;
	private ViewPager mViewPager;
	private GridView mGridView;
	
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
		{
			return null;
		}else
		{
			mView = inflater.inflate(R.layout.today_frame, container, false);
			
			for(int i = 0; i < pics.length; i++)
			{
				ImageView image = new ImageView(getActivity());
				image = (ImageView)inflater.inflate(R.layout.view_page_intro, null);
				image.setBackgroundResource(pics[i]);
				views.add(image);
			}
		}
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		//set viewpager adapter
		mViewPager = (ViewPager)mView.findViewById(R.id.vp_today);
		mViewPager.setAdapter(new ViewPagerAdapter(views));
		
		UnderlinePageIndicator indicator = (UnderlinePageIndicator)getActivity().findViewById(R.id.vp_indicator_today);
		indicator.setViewPager(mViewPager);
		indicator.setFades(false);
		
		mGridView = (GridView)mView.findViewById(R.id.gridview_today);
		mGridView.setAdapter(new GridViewAdapter(getActivity()));
	}

	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
}