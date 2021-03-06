package com.wetongji_android.ui.friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.ui.EndlessListAdapter;
import com.wetongji_android.util.common.WTApplication;

import java.util.HashMap;

public class FriendListAdapter extends EndlessListAdapter<User> {
	private static final float LIST_THUMBNAILS_TARGET_WIDTH_FACTOR = 3;
	private static int LIST_THUMBNAILS_TARGET_WIDTH = 300;
	
	private LayoutInflater mInflater;
	private Context mContext;
	private AQuery mListAq;
	private AQuery mShouldDelayAq;
	private Fragment mFragment;
	
	private static HashMap<Integer, Boolean> isSelected;
	
	public final class ViewHolder
	{
		TextView tvFriendName;
		TextView tvFriendDepart;
		ImageView imgFriendAvatar;
		RelativeLayout rlFriendItem;
		CheckBox cbFriendInvite;
	}
	
	@SuppressLint("UseSparseArrays")
	public FriendListAdapter(Fragment fragment, AbsListView listView)
	{
		super(fragment.getActivity(), listView, R.layout.amazing_lst_view_loading_view);
		
		mContext = fragment.getActivity();
		mInflater = LayoutInflater.from(mContext);
		mFragment = fragment;
		mListAq = WTApplication.getInstance().getAq(mFragment.getActivity());

		WTApplication app = WTApplication.getInstance();
		app.setActivity(fragment.getActivity());
		DisplayMetrics dm = app.getDisplayMetrics();
		if(dm.widthPixels <= 1080) {
			LIST_THUMBNAILS_TARGET_WIDTH = (int)(dm.widthPixels / LIST_THUMBNAILS_TARGET_WIDTH_FACTOR);
		}
		
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}
	
	private void initData()
	{
		for(int i = 0; i < getData().size(); i++)
		{
			isSelected.put(i, false);
		}
	}

	
	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		
		if(convertView ==  null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_friend, parent, false);
			holder.tvFriendName = (TextView)convertView.findViewById(R.id.friend_name);
			holder.tvFriendDepart = (TextView)convertView.findViewById(R.id.friend_department);
			holder.imgFriendAvatar = (ImageView)convertView.findViewById(R.id.friend_avatar);
			holder.rlFriendItem = (RelativeLayout)convertView.findViewById(R.id.friend_item);
			holder.cbFriendInvite = (CheckBox)convertView.findViewById(R.id.cb_friend_invite);
			
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		//Set background color
		if(position % 2 == 0)
		{
			holder.rlFriendItem.setBackgroundResource(R.drawable.listview_selector_1);
		}else
		{
			holder.rlFriendItem.setBackgroundResource(R.drawable.listview_selector_2);
		}
		
		//Set text view
		User user = getItem(position);
		holder.tvFriendName.setText(user.getName());
		holder.tvFriendDepart.setText(user.getDepartment());
		int gendarRid = user.getGender().equals("男") ? R.drawable.ic_profile_gender_male
				: R.drawable.ic_profile_gender_female;
		Drawable gendarDrawable = mContext.getResources().getDrawable(gendarRid);
		holder.tvFriendDepart.setCompoundDrawablesWithIntrinsicBounds(
				gendarDrawable, null, null, null);
		
		//set the checkbox visibility
		if(mContext instanceof FriendInviteActivity)
		{
			holder.cbFriendInvite.setVisibility(View.VISIBLE);
            if (isSelected.size() == 0) {
                holder.cbFriendInvite.setChecked(false);
            }
		}else
		{
			holder.cbFriendInvite.setVisibility(View.GONE);
		}
		
		//Set avatar
		String strUrl = user.getAvatar();
		mShouldDelayAq = mListAq.recycle(convertView);
		if(!strUrl.equals(WTApplication.MISSING_IMAGE_URL))
		{
			if(mShouldDelayAq.shouldDelay(position, convertView, parent, strUrl))
			{
                Drawable colorDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));
				mShouldDelayAq.id(holder.imgFriendAvatar).image(colorDrawable);
			}else
			{
				mShouldDelayAq.id(holder.imgFriendAvatar).image(strUrl, true, true,
	        			LIST_THUMBNAILS_TARGET_WIDTH, R.drawable.default_avatar,
                        null, AQuery.FADE_IN_NETWORK, 1.0f);
			}
		}else
		{
			mShouldDelayAq.id(holder.imgFriendAvatar).image(R.drawable.default_avatar);
		}
		
		return convertView;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected()
	{
		return isSelected;
	}
	
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected)
	{
		FriendListAdapter.isSelected = isSelected;
	}
}
