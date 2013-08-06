package com.wetongji_android.ui.people;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;
import com.wetongji_android.ui.EndlessListAdapter;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.people.PeopleLoader;

public class PeopleListAdapter extends EndlessListAdapter<Person> implements LoaderCallbacks<List<Person>>{

	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private Fragment mFragment;
	
	private BitmapDrawable mBmDefaultThumbnails;
	
	static class ViewHolder {
		TextView tvPeopleName;
		TextView tvPeopleVol;
		TextView tvPeopleWords;
		ImageView ivPeopleAvatar;	
		LinearLayout llPeopleRow;
	}
	
	public PeopleListAdapter(Fragment fragment, AbsListView listView) {
		super(fragment.getActivity(), listView, R.layout.amazing_lst_view_loading_view);
		
		mInflater = LayoutInflater.from(fragment.getActivity());
		mContext = fragment.getActivity();
		mListAq = WTApplication.getInstance().getAq(fragment.getActivity());
		mFragment = fragment;
		mBmDefaultThumbnails = (BitmapDrawable) mContext.getResources()
				.getDrawable(R.drawable.event_list_thumbnail_place_holder);
		
		WTApplication app = WTApplication.getInstance();
		app.setActivity(fragment.getActivity());
		DisplayMetrics dm = app.getDisplayMetrics();
		if(dm.widthPixels <= 1080) {
			LIST_THUMBNAILS_TARGET_WIDTH = (int)(dm.widthPixels / LIST_THUMBNAILS_TARGET_WIDTH_FACTOR);
		}
	}

	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
			
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_people, parent, false);
			holder.tvPeopleName =
					(TextView)convertView.findViewById(R.id.tv_people_name);
			holder.tvPeopleVol =
					(TextView)convertView.findViewById(R.id.tv_people_vol_num);
			holder.tvPeopleWords = 
					(TextView)convertView.findViewById(R.id.tv_people_words);
			holder.ivPeopleAvatar =
					(ImageView)convertView.findViewById(R.id.img_people_avatar);
			holder.llPeopleRow = 
					(LinearLayout)convertView.findViewById(R.id.layout_people_row);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		//Set background color
		if(position % 2 != 0) {
			holder.llPeopleRow.setBackgroundResource(R.drawable.listview_selector_1);
		}else {
			holder.llPeopleRow.setBackgroundResource(R.drawable.listview_selector_2);
		}
		
		Person person = getItem(position);
		holder.tvPeopleName.setText(person.getName());
		holder.tvPeopleWords.setText(person.getWords());
		
		if (position == 0) {
			holder.tvPeopleVol.setText(
					mContext.getString(R.string.people_current_star));
			holder.tvPeopleVol.setTextColor(
					mContext.getResources().getColor(R.color.tv_people_current_vol));
		} else {
			String vol = mContext.getResources().getString(R.string.people_vol);
			holder.tvPeopleVol.setText(String.format(vol, person.getNO()));
			holder.tvPeopleVol.setTextColor(
					mContext.getResources().getColor(R.color.tv_eventlst_location));
		}
		
		// Set avatar
		String strUrl = person.getAvatar();
		mShouldDelayAq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL)){
			
	        if(mShouldDelayAq.shouldDelay(position, convertView, parent, strUrl)) {
	        	mShouldDelayAq.id(holder.ivPeopleAvatar).image(mBmDefaultThumbnails);
	        }
	        else {
	        	mShouldDelayAq.id(holder.ivPeopleAvatar).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1.0f);
	        }
		}
		else{
			mShouldDelayAq.id(holder.ivPeopleAvatar).image(mBmDefaultThumbnails);
		}
		
		return convertView;
	}

	@Override
	public Loader<List<Person>> onCreateLoader(int arg0, Bundle arg1) {
		return new PeopleLoader(mContext);
	}

	@Override
	public void onLoadFinished(Loader<List<Person>> arg0, List<Person> arg1) {
		if (arg1 != null && arg1.size() != 0) {
			Collections.reverse(arg1);
			getData().addAll(arg1);
			setIsLoadingData(false);
			notifyDataSetChanged();
		} else {
			((PeopleListFragment)mFragment).refreshData();
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Person>> arg0) {
	}
	
	public void loadDataFromDB() {
		mFragment.getLoaderManager().initLoader(WTApplication.PERSON_LOADER, null, this);
		setIsLoadingData(true);
	}
}
