package com.wetongji_android.ui.today;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Person;
import com.wetongji_android.ui.account.AccountDetailActivity;
import com.wetongji_android.ui.people.PeopleListActivity;
import com.wetongji_android.ui.people.PeopleListFragment;
import com.wetongji_android.ui.people.PersonDetailActivity;
import com.wetongji_android.util.common.WTApplication;

public class TodayGridFeatureAdapter extends TodayGridBaseAdapter<Object> {

	public TodayGridFeatureAdapter(Context context, List<Object> items) {
		super(context, items);
	}

	@Override
	public long getItemId(int position) {
		Object item = items.get(position);
		if (item instanceof Person) {
			return ((Person) item).getId();
		} else if (item instanceof Account) {
			return ((Account) item).getId();
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		Object item = items.get(position);
		holder.ivGridTitleIndicator
				.setImageResource(R.drawable.indicator_today_grid_cyan);
		holder.tvGridTitle.setTextColor(context.getResources().getColor(
				R.color.tv_today_cyan));
		int paddingLeft = holder.tvGridTitle.getPaddingLeft();
		if (item instanceof Person) {
			getPersonView((Person) item, convertView);
		} else if (item instanceof Account) {
			getAccountView((Account) item, convertView);
		}
		holder.tvGridTitle.setPadding(paddingLeft, 0, 0, 0);
		return convertView;
	}

	private void getPersonView(Person person, View convertView) {
		if (person != null) {
			
			holder.rlSpinner.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					context.startActivity(new Intent(context, 
							PeopleListActivity.class));
				}
			});
			
			holder.tvGridTitle.setText(R.string.text_people);
			holder.tvGridContent.setText(person.getName());
			if (person.getImages() == null
					|| person.getImages().isEmpty()
					|| person.getImages().equals(
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
				String strImage = person.getImages().keySet().iterator().next();
				aq.id(holder.ivGridImage).image(strImage,
						true, true, 300, 0, null, AQuery.FADE_IN_NETWORK, 0f);

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
		}
	}

	private void getAccountView(Account account, View convertView) {
		if (account != null) {
			holder.tvGridTitle.setText(R.string.text_most_popular_account);
			holder.tvGridContent.setText(account.getDisplay());
			holder.ivGridImage.setVisibility(View.GONE);
			holder.ivGridImageMask.setVisibility(View.GONE);
			holder.tvGridTitle
					.setBackgroundResource(R.drawable.bg_today_grid_title_no_image);
			holder.tvGridTitle.setShadowLayer(2, 0, 1, context.getResources()
					.getColor(R.color.tv_today_grid_title_shadow));
			holder.tvGridContent.setTextColor(context.getResources().getColor(
					R.color.tv_today_content_black));
			holder.tvGridContent.setShadowLayer(2, 0, 1, context.getResources()
					.getColor(R.color.transparent));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Object item = items.get(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch(position) {
		case 0:
			bundle.putParcelable(PeopleListFragment.BUNDLE_KEY_PERSON,  
					(Person) item);
			bundle.putBoolean(PeopleListFragment.BUNDLE_KEY_IS_CURRENT, true);
			intent.setClass(context, PersonDetailActivity.class);
			break;
		case 1:
			bundle.putParcelable(AccountDetailActivity.BUNDLE_KEY_ACCOUNT, 
					(Account) item);
			intent.setClass(context, AccountDetailActivity.class);
			break;
		}
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	
}
