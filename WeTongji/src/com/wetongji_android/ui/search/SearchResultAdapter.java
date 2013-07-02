package com.wetongji_android.ui.search;

import java.util.List;

import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.User;

import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchResultAdapter extends AmazingAdapter {

	private Fragment mFragment;
	private LayoutInflater mInflater;
	private List<Pair<Integer, List<Object>>> mResults;
	
	public static class ViewHolder
	{
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_description;
		RelativeLayout rl_item;
	}
	
	public SearchResultAdapter(Fragment fragment) {
		mFragment = fragment;
		mInflater = LayoutInflater.from(fragment.getActivity());
	}
	
	@Override
	public int getCount() {
		int count = 0;
		for (int i = 0; i < mResults.size(); i++) {
			count += mResults.get(i).second.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object item = null;
		int pos = 0;
		for (int i = 0; i < mResults.size(); i++) {
			if (position < mResults.get(i).second.size() + pos) {
				item = mResults.get(i).second.get(position - pos);
			} else {
				pos += mResults.get(i).second.size();
			}
		}
		return item;
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
			view.findViewById(R.id.layout_information_header).setVisibility(View.VISIBLE);
			configureHeader(view, position);
		} else {
			view.findViewById(R.id.layout_information_header).setVisibility(View.GONE);
		}
		
	}

	private void configureHeader(View view, int position) {
		TextView tvSectionHeader = 
				(TextView) view.findViewById(R.id.information_list_header);
		Object item = getItem(position);
		int stringResId = 0;
		if (item instanceof User) {
			stringResId = R.string.type_users;
		} else if (item instanceof Account) {
			stringResId = R.string.type_org;
		} else if (item instanceof Activity) {
			stringResId = R.string.type_activities;
		} else if (item instanceof Information) {
			stringResId = R.string.type_information;
		} else if (item instanceof Person) {
			stringResId = R.string.type_stars;
		}
		
		tvSectionHeader.setText(mFragment.getString(stringResId));
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
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			SearchResult result = convertItemToResult(position);
			holder.tv_title.setText(result.title);
			holder.tv_description.setText(result.desc);
			//TODO add image
		}
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SearchResult convertItemToResult(int position) {
		SearchResult result = new SearchResult();
		Object item = getItem(position);
		if (item instanceof User) {
			User user = (User) item;
			result.title = user.getName();
			result.desc = user.getDepartment();
			result.pic = user.getAvatar();
			result.gender = user.getGender().equals("ÄÐ") ? 1 : 2;
		} else if (item instanceof Account) {
			Account org = (Account) item;
			result.title = org.getName();
			result.desc = org.getDescription();
			result.pic = org.getImage();
		} else if (item instanceof Activity) {
			Activity activity = (Activity) item;
			result.title = activity.getTitle();
			result.desc = activity.getOrganizer();
			result.pic = activity.getImage();
		} else if (item instanceof Information) {
			Information info = (Information) item;
			result.title = info.getTitle();
			result.desc = info.getOrganizer();
			result.pic = "";
		} else if (item instanceof Person) {
			Person person = (Person) item;
			result.title = person.getName();
			result.desc = person.getJobTitle();
			result.pic = person.getAvatar();
		}
		
		return result;
	}
	
	public class SearchResult {
		public String title;
		public String desc;
		public String pic;
		public int gender = 0;
	}

}
