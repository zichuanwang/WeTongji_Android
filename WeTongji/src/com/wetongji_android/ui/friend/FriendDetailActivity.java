package com.wetongji_android.ui.friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.image.ImageUtil;
import com.wetongji_android.util.net.HttpRequestResult;

public class FriendDetailActivity extends WTBaseDetailActivity implements
		LoaderCallbacks<HttpRequestResult> 
{
	private TextView tvFriendWords;
	private TextView tvFriendDepartment;
	private Button ibFriend;
	private RelativeLayout rlFriendList;
	private TextView tvFriendNum;
	private ImageButton ibFriendAdd;
	private RelativeLayout rlPartEvents;
	private TextView tvEventsNum;
	private TextView tvMajor;
	private TextView tvGrade;
	private TextView tvEmail;
	
	private User mUser;
	private AQuery mAq;
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.activity_friend_detail);
		
		receiveData();
		
		initWidget();
	}

	private void receiveData()
	{
		Intent intent = getIntent();
		mUser = intent.getExtras().getParcelable(FriendListFragment.BUNDLE_KEY_USER);
	}
	
	private void initWidget()
	{
		mAq = WTApplication.getInstance().getAq(this);
		tvFriendWords = (TextView)findViewById(R.id.text_profile_words);
		tvFriendWords.setText(mUser.getWords());
		tvFriendDepartment = (TextView)findViewById(R.id.text_profile_gender);
		tvFriendDepartment.setText(mUser.getDepartment());
		ibFriend = (Button)findViewById(R.id.btn_profile_action);
		if(mUser.isIsFriend())
		{
			ibFriend.setText("Friend");
		}else
		{
			ibFriend.setText("UnFriend");
		}
		//Set Avatar
		mAq.id(R.id.img_profile_avatar).image(mUser.getAvatar(), true, true, 0, 0, new BitmapAjaxCallback() 
		{
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) 
			{
				// TODO Auto-generated method stub
				//super.callback(url, iv, bm, status);
				iv.setImageBitmap(bm);
				setHeadBluredBg(bm);
			}	
		});
		rlFriendList = (RelativeLayout)findViewById(R.id.ll_friend_detail_list);
		rlFriendList.setOnClickListener(new ClickListener());
		tvFriendNum = (TextView)findViewById(R.id.tv_detail_friend_num);
		StringBuilder sb = new StringBuilder();
		sb.append(mUser.getFriendCount()).append(" Friends");
		tvFriendNum.setText(sb.toString());
		ibFriendAdd = (ImageButton)findViewById(R.id.btn_detail_friend_add);
		ibFriendAdd.setOnClickListener(new ClickListener());
		rlPartEvents = (RelativeLayout)findViewById(R.id.ll_friend_detail_part_events);
		rlPartEvents.setOnClickListener(new ClickListener());
		tvEventsNum = (TextView)findViewById(R.id.tv_detail_friend_part_events_num);
		sb.delete(0, sb.length());
		sb.append(mUser.getLikeCount().getActivity()).append(" Events");
		tvEventsNum.setText(sb.toString());
		tvMajor = (TextView)findViewById(R.id.tv_friend_detail_major);
		tvMajor.setText(mUser.getMajor());
		tvGrade = (TextView)findViewById(R.id.tv_friend_detail_grade);
		tvGrade.setText(mUser.getYear());
		tvEmail = (TextView)findViewById(R.id.tv_friend_detail_email);
		tvEmail.setText(mUser.getEmail());
	}
	
	@SuppressWarnings("deprecation")
	private void setHeadBluredBg(Bitmap resource)
	{
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.layout_profile_header);
		int tH =  (496 * 200 / 1080);
		Bitmap bm = Bitmap.createBitmap(resource, 0, (100 - tH / 2), 200, tH);
		Bitmap bg = ImageUtil.fastblur(bm, 10);
		rl.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	class ClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			if(v.getId() == R.id.ll_friend_detail_list)
			{
				
			}else
			{
				
			}
		}
	}
}
