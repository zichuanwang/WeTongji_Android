package com.wetongji_android.ui.today;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Banner;
import com.wetongji_android.util.net.HttpUtil;

public class TodayBannerPagerAdapter extends PagerAdapter {
	private List<Banner> banners;
	private LayoutInflater inflater;
	private AQuery aq;
	private Context context;

	public TodayBannerPagerAdapter(List<Banner> banners, Context context) {
		inflater = LayoutInflater.from(context);
		this.banners = banners;
		aq = new AQuery(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return banners.size();
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
		View view = inflater.inflate(R.layout.page_today_banner, null);
		Banner banner = banners.get(position);

		ImageView ivBanner = (ImageView) view
				.findViewById(R.id.iv_banner_image);
		aq.id(ivBanner).image(HttpUtil.replaceURL(banner.getImage()), true,
				true, 300, 0, null, AQuery.FADE_IN_NETWORK,
				AQuery.RATIO_PRESERVE);
		ImageView ivBannerMask = (ImageView) view
				.findViewById(R.id.iv_banner_mask);

		TextView tvBannerTitle = (TextView) view
				.findViewById(R.id.tv_banner_title);
		tvBannerTitle.setText(banner.getTitle());
		TextView tvBannerPublisher = (TextView) view
				.findViewById(R.id.tv_banner_publisher);
		tvBannerPublisher.setText(String.format(context.getResources()
				.getString(R.string.text_banner_publisher_mask), banner
				.getPublisher()));

		if (position == 0) {
			ivBannerMask.setImageResource(R.drawable.img_banner_mask_current);
		}
		ivBannerMask.setTag("position" + position);
		container.addView(view);

		return view;
	}
}
