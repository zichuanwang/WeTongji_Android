package com.wetongji_android.ui.informations;

import java.util.List;

import com.androidquery.AQuery;
import com.wetongji_android.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class InfoDetailPicPagerAdapter extends PagerAdapter {

	private List<String> mPics;
	private AQuery mAq;
	private Context mContext;
	private List<View> mViews;

	public InfoDetailPicPagerAdapter(List<String> pics,
			Context context, List<View> views) {
		mPics = pics;
		mContext = context;
		mAq = new AQuery(mContext);
		mViews = views;
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
		View view = mViews.get(position);

		String url = mPics.get(position);
		
		ImageView ivPic = (ImageView) view
				.findViewById(R.id.img_person_detail_viewpager_pic);
		mAq.id(ivPic).image(url, true, true, 300, 0, null,
				AQuery.FADE_IN_NETWORK, 0f);

		container.addView(view);
		return view;
	}

}
