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
import com.wetongji_android.data.Activity;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.util.common.WTApplication;

public class TodayGridEventAdapter extends TodayGridBaseAdapter<Activity> {

	public TodayGridEventAdapter(Context context, List<Activity> items) {
		super(context, items);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Activity activity = items.get(position);
		if (activity != null) {
			holder.ivGridTitleIndicator
					.setImageResource(R.drawable.indicator_today_grid_green);
			holder.tvGridTitle.setTextColor(context.getResources().getColor(
					R.color.tv_today_green));
			switch (activity.getChannel_Id()) {
			case 1:
				holder.tvGridTitle.setText(R.string.text_event_channel_id_1);
				break;
			case 2:
				holder.tvGridTitle.setText(R.string.text_event_channel_id_2);
				break;
			case 3:
				holder.tvGridTitle.setText(R.string.text_event_channel_id_3);
				break;
			case 4:
				holder.tvGridTitle.setText(R.string.text_event_channel_id_4);
				break;
			}
			holder.tvGridContent.setText(activity.getTitle());
			int paddingLeft = holder.tvGridTitle.getPaddingLeft();
			if (activity.getImage() == null
					|| activity.getImage().equals(
							WTApplication.MISSING_IMAGE_URL)) {
				holder.ivGridImage.setVisibility(View.GONE);

				holder.ivGridImageMask.setVisibility(View.GONE);

				holder.tvGridTitle
						.setBackgroundResource(R.drawable.bg_today_grid_title_no_image);
				holder.tvGridTitle.setShadowLayer(
						2,
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
						activity.getImage(), true, true,
						300, 0, null, AQuery.FADE_IN_NETWORK, 1f);

				holder.ivGridImageMask.setVisibility(View.VISIBLE);

				holder.tvGridTitle
						.setBackgroundResource(R.drawable.bg_today_grid_title_default);
				holder.tvGridTitle.setShadowLayer(0, 0, 1, context
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {
		Intent intent = new Intent(context, EventDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY,
				items.get(position));
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

}
