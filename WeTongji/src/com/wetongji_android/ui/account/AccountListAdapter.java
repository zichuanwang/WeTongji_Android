package com.wetongji_android.ui.account;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
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
import com.wetongji_android.data.Account;
import com.wetongji_android.ui.EndlessListAdapter;
import com.wetongji_android.util.common.WTApplication;

public class AccountListAdapter extends EndlessListAdapter<Account> {	
	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private Fragment mFragment;
	
	private BitmapDrawable mBmDefaultThumbnails;
	
	static class ViewHolder {
		TextView tvEventTitle;
		TextView tvEventDescription;
		ImageView ivEventThumb;	
		LinearLayout llEventRow;
	}
	
	public AccountListAdapter(Fragment fragment, AbsListView listView)
	{
		super(fragment.getActivity(), listView, R.layout.amazing_lst_view_loading_view);
		mFragment = fragment;
		mContext = mFragment.getActivity();
		mInflater = LayoutInflater.from(mContext);
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
		
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.row_account, parent, false);
			holder.tvEventTitle=
					(TextView)convertView.findViewById(R.id.tv_account_title);
			holder.tvEventDescription = 
					(TextView)convertView.findViewById(R.id.tv_account_description);
			holder.ivEventThumb=
					(ImageView)convertView.findViewById(R.id.img_account_thumbnails);
			holder.llEventRow = 
					(LinearLayout)convertView.findViewById(R.id.layout_account_row);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		//Set background color
		if(position % 2 != 0) {
			holder.llEventRow.setBackgroundResource(R.drawable.listview_selector_2);
		}else {
			holder.llEventRow.setBackgroundResource(R.drawable.listview_selector_1);
		}
		
		Account event = getItem(position);
		
		holder.tvEventTitle.setText(event.getTitle());
		
		holder.tvEventDescription.setText(event.getDescription());
		
		// Set thumbnails
		String strUrl = event.getImage();

		mShouldDelayAq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL)){
			
	        if(mShouldDelayAq.shouldDelay(position, convertView, parent, strUrl)) {
	        	mShouldDelayAq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
	        }
	        else {
	        	mShouldDelayAq.id(holder.ivEventThumb).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.event_list_thumbnail_place_holder,
	        			null, AQuery.FADE_IN_NETWORK, 1.33f);
	        }
		}
		else{
			mShouldDelayAq.id(holder.ivEventThumb).image(mBmDefaultThumbnails);
		}
		
		return convertView;
	}
}
