package com.wetongji_android.ui.today;

import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TodayBannerPagerAdapter extends PagerAdapter 
{
	private int imageIds[]=new int[]{R.drawable.img_today_1, R.drawable.img_today_2,
			R.drawable.img_today_3};
	private LayoutInflater inflater;
	
	public TodayBannerPagerAdapter(List<Banner> banners, Context context) 
	{
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 3;
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
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		View view=inflater.inflate(R.layout.page_today_banner, null);
		ImageView ivBanner=(ImageView) view.findViewById(R.id.iv_banner_image);
		ivBanner.setImageResource(imageIds[position]);
		ImageView ivBannerMask=(ImageView) view.findViewById(R.id.iv_banner_mask);
		if(position==0){
			ivBannerMask.setImageResource(R.drawable.img_banner_mask_current);
		}

		Log.v("bannerPagerAdapter", "instantiate item at "+position);
		ivBannerMask.setTag("position"+position);
		container.addView(view);
		return view;
	}
}
