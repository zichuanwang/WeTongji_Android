package com.wetongji_android.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.SearchResults;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchResultAdapter extends AmazingAdapter {

	private Fragment mFragment;
	private LayoutInflater mInflater;
	private SearchResults mResults;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private BitmapDrawable mBmDefaultThumbnails;
	
	public static class ViewHolder {
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_description;
		RelativeLayout rl_item;
	}
	
	public class Result {
		public String title;
		public String desc;
		public String pic;
		public int gender = 0;
	}
	
	public SearchResultAdapter(Fragment fragment) {
		mFragment = fragment;
		mInflater = LayoutInflater.from(fragment.getActivity());
		mListAq = WTApplication.getInstance().getAq(fragment.getActivity());
		mBmDefaultThumbnails = (BitmapDrawable) fragment.getActivity()
				.getResources()
				.getDrawable(R.drawable.event_list_thumbnail_place_holder);
		addResult(new SearchResults());
	}
	
	@Override
	public int getCount() {
		int count = 0;
		count += mResults.getAccounts().size();
		count += mResults.getUsers().size();
		count += mResults.getActivities().size();
		count += mResults.getInformation().size();
		count += mResults.getPerson().size();
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		int pos = 0;
		if (position < mResults.getUsers().size() + pos) {
			return mResults.getUsers().get(position);
		} else {
			pos += mResults.getUsers().size();
		}
		if (position < mResults.getAccounts().size() + pos) {
			return mResults.getAccounts().get(position - pos);
		} else {
			pos += mResults.getAccounts().size();
		}
		if (position < mResults.getActivities().size() + pos) {
			return mResults.getActivities().get(position - pos);
		} else {
			pos += mResults.getActivities().size();
		}
		if (position < mResults.getInformation().size() + pos) {
			return mResults.getInformation().get(position - pos);
		} else {
			pos += mResults.getInformation().size();
		}
		if (position < mResults.getPerson().size() + pos) {
			return mResults.getPerson().get(position - pos);
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
		
		tvSectionHeader.setText(stringResId);
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
			Result result = convertItemToResult(position);
			holder.tv_title.setText(result.title);
			holder.tv_description.setText(result.desc);
			Drawable gender = null;
			if (result.gender == 1) {
				gender = mFragment.getActivity().getResources()
						.getDrawable(R.drawable.ic_profile_gender_male);
			} else if (result.gender == 2) {
				gender = mFragment.getActivity().getResources()
						.getDrawable(R.drawable.ic_profile_gender_female);
			}
			holder.tv_description.setCompoundDrawables(gender, null, null, null);
			//add image
			mShouldDelayAq = mListAq.recycle(convertView);
			if (!result.pic.equals(WTApplication.MISSING_IMAGE_URL)) {
				if (mShouldDelayAq.shouldDelay(position, convertView, parent,
						result.pic)) {
					mShouldDelayAq.id(holder.iv_pic)
							.image(mBmDefaultThumbnails);
				} else {
					mShouldDelayAq.id(holder.iv_pic).image(result.pic, true,
							true, 360,
							R.drawable.event_list_thumbnail_place_holder, null,
							AQuery.FADE_IN_NETWORK, 1.0f);
				}
			} else {
				mShouldDelayAq.id(holder.iv_pic).image(mBmDefaultThumbnails);
			}
		}
		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		configureHeader(header, position);
	}

	@Override
	public int getPositionForSection(int section) {
		switch (section) {
		case 0:
			return 0;
		case 1:
			return getSecondSection();
		case 2:
			return getThirdSection();
		case 3:
			return getFourthSection();
		case 4:
			return getFifthSection();
		}
		
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		Object item = getItem(position);
		int section = 0;
		if (item instanceof User) {
			section = 0;
		} else if (item instanceof Account) {
			section = 1;
		} else if (item instanceof Activity) {
			section = 2;
		} else if (item instanceof Information) {
			section = 3;
		} else if (item instanceof Person) {
			section = 4;
		}
		return section;
	}

	@Override
	public String[] getSections() {
		Log.v(TAG, "getSections");
		String user = mFragment.getString(R.string.type_users);
		String accounts = mFragment.getString(R.string.type_org);
		String activity = mFragment.getString(R.string.type_activities);
		String information = mFragment.getString(R.string.type_information);
		String star = mFragment.getString(R.string.type_stars);
		
		List<String> sections = new ArrayList<String>();
		if (mResults.getPerson().size() != 0) {
			sections.add(star);
		}
		if (mResults.getInformation().size() != 0) {
			sections.add(information);
		}
		if (mResults.getActivities().size() != 0) {
			sections.add(activity);
		}
		if (mResults.getAccounts().size() != 0) {
			sections.add(accounts);
		}
		if (mResults.getUsers().size() != 0) {
			sections.add(user);
		}
		
		Log.v("size", "" + sections.size());
		return (String[]) sections.toArray();
	}
	
	private int getSecondSection() {
		int pos = 0;
		if (mResults.getUsers().size() != 0) {
			return mResults.getUsers().size();
		} else if (mResults.getAccounts().size() != 0){
			return mResults.getAccounts().size() + pos;
		} else if (mResults.getActivities().size() != 0) {
			return mResults.getActivities().size() + pos;
		} else if (mResults.getInformation().size() != 0) {
			return mResults.getInformation().size() + pos;
		}
		return pos;
	}
	
	private int getThirdSection() {
		int pos = 0;
		if (mResults.getUsers().size() != 0) {
			pos += mResults.getUsers().size();
		} else if (mResults.getAccounts().size() != 0){
			return mResults.getAccounts().size() + pos;
		} else if (mResults.getActivities().size() != 0) {
			return mResults.getActivities().size() + pos;
		} else if (mResults.getInformation().size() != 0) {
			return mResults.getInformation().size() + pos;
		}
		return pos;
	}
	
	private int getFourthSection() {
		int pos = 0;
		if (mResults.getUsers().size() != 0) {
			pos += mResults.getUsers().size();
		} else if (mResults.getAccounts().size() != 0){
			pos +=  mResults.getAccounts().size() + pos;
		} else if (mResults.getActivities().size() != 0) {
			return mResults.getActivities().size() + pos;
		} else if (mResults.getInformation().size() != 0) {
			return mResults.getInformation().size() + pos;
		}
		return pos;
	}
	
	private int getFifthSection() {
		int pos = 0;
		if (mResults.getUsers().size() != 0) {
			pos += mResults.getUsers().size();
		} else if (mResults.getAccounts().size() != 0){
			pos +=  mResults.getAccounts().size() + pos;
		} else if (mResults.getActivities().size() != 0) {
			pos += mResults.getActivities().size() + pos;
		} else if (mResults.getInformation().size() != 0) {
			return mResults.getInformation().size() + pos;
		}
		return pos;
	}
	
	private Result convertItemToResult(int position) {
		Result result = new Result();
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

	public void addResult(SearchResults result) {
		mResults = result;
		if (mResults.getAccounts() == null) {
			mResults.setAccounts(new ArrayList<Account>(0));
		}
		if (mResults.getUsers() == null) {
			mResults.setUsers(new ArrayList<User>(0));
		}
		if (mResults.getPerson() == null) {
			mResults.setPerson(new ArrayList<Person>(0));
		}
		if (mResults.getActivities() == null) {
			mResults.setActivities(new ArrayList<Activity>(0));
		}
		if (mResults.getInformation() == null) {
			mResults.setInformation(new ArrayList<Information>(0));
		}
		
		notifyDataSetChanged();
	}
	
}
