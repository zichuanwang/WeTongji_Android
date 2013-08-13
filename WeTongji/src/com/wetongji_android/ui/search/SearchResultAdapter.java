package com.wetongji_android.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.foound.widget.AmazingAdapter;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.SearchResult;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
		// Information
		TextView tvInfoType;
		TextView tvInfoTitle;
		TextView tvInfoDescription;
		// Account
		TextView tvAccountName;
		TextView tvAccountDesc;
		ImageView ivAccountThumb;
		// User
		TextView tvFriendName;
		TextView tvFriendDepart;
		ImageView imgFriendAvatar;
		// Course
		TextView tvCourseTitle;
		TextView tvCourseTeacher;
		// Activity
		TextView tvEventTitle;
		TextView tvEventTime;
		TextView tvEventLocation;
		ImageView ivEventThumb;
		// People
		TextView tvPeopleName;
		TextView tvPeopleVol;
		TextView tvPeopleWords;
		ImageView ivPeopleAvatar;
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
		for (int i = 0; i < mData.size(); i++) {
			count += mData.get(i).second.size();
		}

		return count;
	}

	@Override
	public Object getItem(int position) {
		int pos = 0;

		for (int i = 0; i < mData.size(); i++) {
			if (position >= pos && position < pos + mData.get(i).second.size()) {
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
		TextView tvSectionHeader = (TextView) view
				.findViewById(R.id.information_list_header);
		String header = mData.get(getSectionForPosition(position)).first;
		if (header.equals("Account"))
		tvSectionHeader.setText(header);
	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) {

		SearchResult result = (SearchResult) getItem(position);
		ViewHolder holder = new ViewHolder();
		
		switch (result.getType()) {
			case 1:
				convertView = mInflater.inflate(R.layout.information_list_item,
						parent, false);
				holder.tvInfoTitle = (TextView) convertView
						.findViewById(R.id.information_list_item_title);
				holder.tvInfoDescription = (TextView) convertView
						.findViewById(R.id.information_list_item_description);
				holder.tvInfoType = (TextView) convertView
						.findViewById(R.id.information_list_item_type);
				setInfoWidgets(holder, result);
				break;
			case 2:
				convertView = mInflater.inflate(R.layout.row_search_result,
						parent, false);
				holder.tvAccountName = (TextView) convertView
						.findViewById(R.id.search_result_name);
				holder.tvAccountDesc = (TextView) convertView
						.findViewById(R.id.search_result_sub_name);
				holder.ivAccountThumb = (ImageView) convertView
						.findViewById(R.id.search_result_pic);
				setAccountWidgets(holder, result);
				break;
			case 3:
				convertView = mInflater.inflate(R.layout.row_friend_search, parent,
						false);
				holder.tvFriendName = (TextView) convertView
						.findViewById(R.id.friend_name);
				holder.tvFriendDepart = (TextView) convertView
						.findViewById(R.id.friend_department);
				holder.imgFriendAvatar = (ImageView) convertView
						.findViewById(R.id.friend_avatar);
				setUserWidgets(holder, result);
				break;
			case 4:
				convertView = mInflater.inflate(R.layout.row_my_courses_search,
						parent, false);
				holder.tvCourseTitle = (TextView) convertView
						.findViewById(R.id.text_mycourses_name);
				holder.tvCourseTeacher = (TextView) convertView
						.findViewById(R.id.text_mycourses_teacher);
				setCourseWidgets(holder, result);
				break;
			case 5:
				convertView = mInflater.inflate(R.layout.row_event_search, parent,
						false);
				holder.tvEventTitle = (TextView) convertView
						.findViewById(R.id.tv_event_title);
				holder.tvEventTime = (TextView) convertView
						.findViewById(R.id.tv_event_time);
				holder.tvEventLocation = (TextView) convertView
						.findViewById(R.id.tv_event_location);
				holder.ivEventThumb = (ImageView) convertView
						.findViewById(R.id.img_event_thumbnails);
				setEventWidgets(holder, result);
				break;
			case 6:
				convertView = mInflater.inflate(R.layout.row_people,
						parent, false);
				holder.tvPeopleName = (TextView) convertView
						.findViewById(R.id.tv_people_name);
				holder.tvPeopleVol = (TextView) convertView
						.findViewById(R.id.tv_people_vol_num);
				holder.tvPeopleWords = (TextView) convertView
						.findViewById(R.id.tv_people_words);
				holder.ivPeopleAvatar = (ImageView) convertView
						.findViewById(R.id.img_people_avatar);
				setPeopleWidgets(holder, result);
				break;
		}
		
		int section = getSectionForPosition(position);
		int pos = getPositionForSection(section);

		if ((position - pos) % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.listview_selector_1);
		} else {
			convertView.setBackgroundResource(R.drawable.listview_selector_2);
		}

		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		configureHeader(header, position);
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0)
			section = 0;
		if (section >= mData.size())
			section = mData.size() - 1;

		int c = 0;

		for (int i = 0; i < mData.size(); i++) {
			if (section == i) {
				return c;
			}

			c += mData.get(i).second.size();
		}

		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		int c = 0;

		for (int i = 0; i < mData.size(); i++) {
			if (position >= c && position < c + mData.get(i).second.size()) {
				return i;
			}

			c += mData.get(i).second.size();
		}

		return -1;
	}

	@Override
	public String[] getSections() {
		String[] sections = new String[mData.size()];

		for (int i = 0; i < mData.size(); i++) {
			sections[i] = mData.get(i).first;
		}

		return sections;
	}

	public void setSearchResult(List<Pair<String, List<SearchResult>>> search) {
		this.mData.clear();
		this.mData = search;
		notifyDataSetChanged();
	}
	
	private void setInfoWidgets(ViewHolder holder, SearchResult result) {
		Information info = (Information) result.getContent();
		holder.tvInfoTitle.setText(info.getTitle());
		holder.tvInfoDescription.setText(info.getSummary());
		holder.tvInfoType.setText(info.getCategory());
	}
	
	private void setAccountWidgets(ViewHolder holder, SearchResult result) {
		Account account = (Account) result.getContent();
		holder.tvAccountName.setText(account.getDisplay());
		holder.tvAccountDesc.setText(account.getDescription());
		String strUrl = account.getImage();
		mListAq.id(holder.ivAccountThumb).image(strUrl, true, true,
	        			300, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1f);
	}
	
	private void setUserWidgets(ViewHolder holder, SearchResult result) {
		User user = (User) result.getContent();
		holder.tvFriendName.setText(user.getName());
		holder.tvFriendDepart.setText(user.getDepartment());
		int gendarResourceId = user.getGender().equals("ÄÐ") ?
				R.drawable.ic_profile_gender_male :
				R.drawable.ic_profile_gender_female;
		Drawable gendar = mContext.getResources().getDrawable(gendarResourceId);
		holder.tvFriendDepart.setCompoundDrawablesWithIntrinsicBounds(
				gendar, null, null, null);

		String strUrl = user.getAvatar();
		mListAq.id(holder.imgFriendAvatar).image(strUrl, true, true,
    			300, R.drawable.event_list_thumbnail_place_holder,
    			null, AQuery.FADE_IN_NETWORK, 1f);
	}
	
	private void setCourseWidgets(ViewHolder holder, SearchResult result) {
		Course course = (Course) result.getContent();
		holder.tvCourseTitle.setText(course.getTitle());
		holder.tvCourseTeacher.setText(course.getTeacher());
	}
	
	private void setEventWidgets(ViewHolder holder, SearchResult result) {
		Activity activity = (Activity) result.getContent();
		holder.tvEventTitle.setText(activity.getTitle());
		holder.tvEventTime.setText(DateParser.getEventTime(mContext,
				activity.getBegin(), activity.getEnd()));
		holder.tvEventLocation.setText(activity.getLocation());
		String strUrl = activity.getImage();
		mListAq.id(holder.ivEventThumb).image(strUrl, true, true,
				300, R.drawable.event_list_thumbnail_place_holder,
    			null, AQuery.FADE_IN_NETWORK, 1.33f);
	}
	
	private void setPeopleWidgets(ViewHolder holder, SearchResult result) {
		Person person = (Person) result.getContent();
		holder.tvPeopleName.setText(person.getName());
		holder.tvPeopleVol.setText(String.valueOf(person.getNO()));
		holder.tvPeopleWords.setText(person.getWords());
		String strUrl = person.getAvatar();
		mListAq.id(holder.ivPeopleAvatar).image(strUrl, true, true,
    			300, R.drawable.event_list_thumbnail_place_holder,
    			null, AQuery.FADE_IN_NETWORK, 1f);
	}
}
