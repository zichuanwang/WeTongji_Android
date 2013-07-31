package com.wetongji_android.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.SearchResult;
import com.wetongji_android.util.common.WTApplication;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchResultAdapter extends AmazingAdapter {

	private Fragment mFragment;
	private Context mContext;
	private LayoutInflater mInflater;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private BitmapDrawable mBmDefaultThumbnails;
	
	private List<Pair<String, List<SearchResult>>> mData;
	
	public static class ViewHolder {
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_description;
		RelativeLayout rl_item;
	}
	
	public SearchResultAdapter(Fragment fragment) {
		mFragment = fragment;
		mContext = fragment.getActivity();
		mInflater = LayoutInflater.from(fragment.getActivity());
		mListAq = WTApplication.getInstance().getAq(fragment.getActivity());
		mBmDefaultThumbnails = (BitmapDrawable) fragment.getActivity()
				.getResources()
				.getDrawable(R.drawable.event_list_thumbnail_place_holder);
		mData = new ArrayList<Pair<String, List<SearchResult>>>();
	}
	
	@Override
	public int getCount() {
		int count = 0;
		for(int i = 0; i < mData.size(); i++)
		{
			count += mData.get(i).second.size();
		}
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		int pos = 0;
		
		for(int i = 0; i < mData.size(); i++)
		{
			if(position >= pos && position < pos + mData.get(i).second.size())
			{
				return mData.get(i).second.get(position - pos);
			}
			
			pos += mData.get(i).second.size();
		}
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {
		
	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) {
		if (displaySectionHeader) {
			view.findViewById(R.id.layout_information_header).setVisibility(
					View.VISIBLE);
			configureHeader(view, position);
		} else {
			view.findViewById(R.id.layout_information_header).setVisibility(
					View.GONE);
		}
		
	}

	private void configureHeader(View view, int position) {
		TextView tvSectionHeader = 
				(TextView) view.findViewById(R.id.information_list_header);
		String header = mData.get(getSectionForPosition(position)).first;
		tvSectionHeader.setText(header);
		Log.d("data", header);
		//tvSectionHeader.setText(getSections()[getSectionForPosition(position)]);
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.row_search_result, parent, false);
			holder.iv_pic = (ImageView) convertView
					.findViewById(R.id.search_result_pic);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.search_result_name);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.search_result_sub_name);
			holder.rl_item = (RelativeLayout)convertView.findViewById(R.id.search_result_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Drawable gender = null;
		SearchResult sr = (SearchResult)getItem(position);
		int section = getSectionForPosition(position);
		int pos = getPositionForSection(section);
		
		if((position - pos) % 2 == 0)
		{
			holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.information_list_row1));
		}else
		{
			holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.information_list_row2));
		}
		holder.tv_title.setText(sr.getTitle());
		holder.tv_description.setText(sr.getDesc());
		
		gender = mFragment.getActivity().getResources()
				.getDrawable(R.drawable.ic_profile_gender_female);
		holder.tv_description.setCompoundDrawables(gender, null, null, null);
		//add image
		mShouldDelayAq = mListAq.recycle(convertView);
		if (!sr.getAvatar().equals(WTApplication.MISSING_IMAGE_URL)) {
			if (mShouldDelayAq.shouldDelay(position, convertView, parent,
					sr.getAvatar())) {
				mShouldDelayAq.id(holder.iv_pic)
				.image(mBmDefaultThumbnails);
			} else {
				mShouldDelayAq.id(holder.iv_pic).image(sr.getAvatar(), true,
						true, 360,
						R.drawable.event_list_thumbnail_place_holder, null,
						AQuery.FADE_IN_NETWORK, 1.0f);
			}
		} else {
			mShouldDelayAq.id(holder.iv_pic).image(mBmDefaultThumbnails);
		}
		
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		configureHeader(header, position);
	}

	@Override
	public int getPositionForSection(int section) {
		if(section < 0) section = 0;
		if(section >= mData.size()) section = mData.size() - 1;
		
		int c = 0;
		
		for(int i = 0; i < mData.size(); i++)
		{
			if(section == i)
			{
				return c;
			}
			
			c += mData.get(i).second.size();
		}
		
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		int c= 0;
		
		for(int i = 0; i < mData.size(); i++)
		{
			if(position >= c && position < c + mData.get(i).second.size())
			{
				return i;
			}
			
			c += mData.get(i).second.size();
		}
		
		return -1;
	}

	@Override
	public String[] getSections() {
		String[] sections = new String[mData.size()];
		
		for(int i = 0; i < mData.size(); i++)
		{
			sections[i] = mData.get(i).first;
		}
		
		return sections;
	}
	public void setSearchResult(List<Pair<String, List<SearchResult>>> search)
	{
		this.mData.clear();
		this.mData = search;
		notifyDataSetChanged();
	}
}
