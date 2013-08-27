package com.wetongji_android.ui.today;

import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Banner;
import com.wetongji_android.data.Information;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.informations.InformationDetailActivity;
import com.wetongji_android.ui.informations.InformationsFragment;

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
		view.setTag(banner);
		view.setOnClickListener(onPageClickListener);
		
		ImageView ivBanner = (ImageView) view
				.findViewById(R.id.iv_banner_image);
		aq.id(ivBanner).image(banner.getImage(), true,
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
	
	private OnClickListener onPageClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent = null;
			Banner banner = (Banner) view.getTag();
			if (banner.getURL() == null) {
				if (banner.getContent() instanceof Activity) {
					Activity activity = (Activity) banner.getContent();
					intent = new Intent(context, EventDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY, activity);
					intent.putExtras(bundle);
				} else if (banner.getContent() instanceof Information) {
					Information info  = (Information) banner.getContent();
					intent = new Intent(context, InformationDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(InformationsFragment.BUNDLE_KEY_INFORMATION, info);
					intent.putExtras(bundle);
				}
			} else {
				String url = banner.getURL();
				if (!url.contains("http")) {
					StringBuilder sb = new StringBuilder("http://");
					url = sb.append(url).toString();
				}
				intent = new Intent(Intent.ACTION_VIEW);  
				intent.setData(Uri.parse(url));  
			}
			
			try {
				context.startActivity(intent); 
			} catch (ActivityNotFoundException e) {
			}
		}
		
	};
}
