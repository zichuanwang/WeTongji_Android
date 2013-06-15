package com.wetongji_android.ui.people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.util.net.HttpUtil;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonDetailPicPagerAdapter extends PagerAdapter{

	private HashMap<String, String> mPics;
	private LayoutInflater mInflater;
	private AQuery mAq;
	
	public PersonDetailPicPagerAdapter(HashMap<String, String> pics, Context context) {
		mPics = pics;
		mInflater = LayoutInflater.from(context);
		mAq = new AQuery(context);
	}
	
	@Override
	public int getCount() {
		return mPics.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mInflater.inflate(R.layout.page_person_pic, null);
		
		String url = "";
		String desc = "";
		Set<String> keys = mPics.keySet();
		List<String> lstKeys = new ArrayList<String>();
		for (String key : keys) {
			lstKeys.add(key);
		}
		Collections.reverse(lstKeys);
		url = lstKeys.get(position);
		desc = mPics.get(url);
		
		ImageView ivPic = (ImageView) view.findViewById(R.id.img_person_detail_viewpager_pic);
		mAq.id(ivPic).image(url, true,
				true, 300, 0, null, AQuery.FADE_IN_NETWORK,
				AQuery.RATIO_PRESERVE);
		
		TextView tvDesc = (TextView) view.findViewById(R.id.tv_person_detail_pic_desc);
		tvDesc.setText(desc);
		
		container.addView(view);
		return view;
	}
	
	

}
