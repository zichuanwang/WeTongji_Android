package com.wetongji_android.ui.search;

import com.wetongji_android.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchTipsAdapter extends BaseAdapter {
	private static final int COUNT = 6;
	
	private String mTipAll;
	private String mTipUser;
	private String mTipOrg;
	private String mTipActivity;
	private String mTipNews;
	private String mTipStars;
	
	private String mKeywords = "";
	
	private Fragment mFragment;
	
	public SearchTipsAdapter(Fragment fragment) {
		mFragment = fragment;
		mTipAll = mFragment.getString(R.string.search_tip_all);
		mTipUser = mFragment.getString(R.string.search_tip_users);
		mTipOrg = mFragment.getString(R.string.search_tip_org);
		mTipActivity = mFragment.getString(R.string.search_tip_activity);
		mTipNews = mFragment.getString(R.string.search_tip_news);
		mTipStars = mFragment.getString(R.string.search_tip_stars);
	}
	
	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(mFragment.getActivity());
		View view = inflater.inflate(R.layout.row_search_tips, parent, false);
		TextView tvTip = (TextView) view.findViewById(R.id.text_search_tip);
		StringBuilder sb = new StringBuilder();
		switch (position) {
		case 0:
			sb.append(mTipAll);
			break;
		case 1:
			sb.append(mTipUser);
			break;
		case 2:
			sb.append(mTipOrg);
			break;
		case 3:
			sb.append(mTipActivity);
			break;
		case 4:
			sb.append(mTipNews);
			break;
		case 5:
			sb.append(mTipStars);
			break;
		}
		sb.append(" ").append(mKeywords);
		tvTip.setText(sb.toString());
		
		LinearLayout llRow = (LinearLayout) view.findViewById(R.id.ll_search_tip);
		if (position % 2 != 0) {
			llRow.setBackgroundColor(mFragment.getActivity().
					getResources().getColor(R.color.layout_event_list_row1));
		} else {
			llRow.setBackgroundColor(mFragment.getActivity().
					getResources().getColor(R.color.layout_event_list_row2));
		}
		
		return view;
	}
	
	public void setKeywords(String keywords) {
		mKeywords = keywords;
		notifyDataSetChanged();
	}

	public String getmKeywords() {
		return mKeywords;
	}
	
}
