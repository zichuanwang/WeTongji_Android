package com.wetongji_android.ui.today;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.ui.informations.InformationDetailActivity;
import com.wetongji_android.ui.informations.InformationsFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.HttpUtil;

public class TodayGridNewsAdapter extends TodayGridBaseAdapter<Information> {

	public TodayGridNewsAdapter(Context context, List<Information> items) {
		super(context, items);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Information info = items.get(position);
		if (info != null) {
			holder.ivGridTitleIndicator
					.setImageResource(R.drawable.indicator_today_grid_red);
			holder.tvGridTitle.setTextColor(context.getResources().getColor(
					R.color.tv_today_red));
			holder.tvGridContent.setText(info.getTitle());
			int paddingLeft = holder.tvGridTitle.getPaddingLeft();
			String strCategory = info.getCategory();
			if (strCategory.equals("校园新闻")) {
				holder.tvGridTitle.setText(R.string.text_news_category_1);
			} else if (strCategory.equals("校务信息")) {
				holder.tvGridTitle.setText(R.string.text_news_category_2);
			} else if (strCategory.equals("社团通告")) {
				holder.tvGridTitle.setText(R.string.text_news_category_3);
			} else if (strCategory.equals("周边推荐")) {
				holder.tvGridTitle.setText(R.string.text_news_category_4);
			}
			if (info.getImages() == null
					|| info.getImages().isEmpty()
					|| info.getImages().get(0)
							.equals(WTApplication.MISSING_IMAGE_URL)) {
				holder.ivGridImage.setVisibility(View.GONE);

				holder.ivGridImageMask.setVisibility(View.GONE);

				holder.tvGridTitle
						.setBackgroundResource(R.drawable.bg_today_grid_title_no_image);
				holder.tvGridTitle.setShadowLayer(
						0,
						0,
						1,
						context.getResources().getColor(
								R.color.tv_today_grid_title_shadow));

				holder.tvGridContent.setTextColor(context.getResources()
						.getColor(R.color.tv_today_content_black));
				holder.tvGridContent.setShadowLayer(2, 0, 1, context
						.getResources().getColor(R.color.transparent));
			} else {
				holder.ivGridImage.setVisibility(View.VISIBLE);
				AQuery aq = gridAq.recycle(convertView);
				aq.id(holder.ivGridImage).image(
						HttpUtil.replaceURL(info.getImages().get(0)), true,
						true, 300, 0, null, AQuery.FADE_IN_NETWORK, 1f);

				holder.ivGridImageMask.setVisibility(View.VISIBLE);

				holder.tvGridTitle
						.setBackgroundResource(R.drawable.bg_today_grid_title_default);
				holder.tvGridTitle.setShadowLayer(2, 0, 1, context
						.getResources().getColor(R.color.transparent));

				holder.tvGridContent.setTextColor(context.getResources()
						.getColor(R.color.tv_today_content_white));
				holder.tvGridContent.setShadowLayer(
						2,
						0,
						1,
						context.getResources().getColor(
								R.color.tv_today_content_shadow));
			}
			holder.tvGridTitle.setPadding(paddingLeft, 0, 0, 0);
		}
		return convertView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) 
	{		
		Intent intent = new Intent(context, InformationDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(InformationsFragment.BUNDLE_KEY_INFORMATION, items.get(position));
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
