package com.wetongji_android.ui.event;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTFullScreenActivity;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.net.ApiHelper;

public class EventDetailActivity extends WTBaseDetailActivity  
{

	private Activity mEvent;

	private CheckBox mCbLike;
	private boolean isRestCheckBox = false;
	private TextView mTvLikeNum;

	private AQuery mAq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_event_detail);
		
		recieveActivity();
		setUpUI();
		showBottomActionBar();
	}

	private void setUpUI() 
	{
		setPicture();
		
		setLikeCheckbox();

		setTextViews();
	}

	private void recieveActivity() {
		Intent intent = this.getIntent();
		mEvent = intent.getParcelableExtra(EventsFragment.BUNDLE_KEY_ACTIVITY);
		setiChildId(mEvent.getId());
	}

	private void setPicture() {
		mAq = WTApplication.getInstance().getAq(this);
		mAq.id(R.id.img_event_detail_org_avatar).image(
				mEvent.getOrganizerAvatar(), false, true, 0,
				R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);

		Drawable drawable = getResources().getDrawable(
				R.drawable.image_place_holder);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (!mEvent.getImage().equals(WTApplication.MISSING_IMAGE_URL)) {
			mAq.id(R.id.iv_event_detail_image).image(mEvent.getImage(), false, true, 0,
					R.drawable.image_place_holder, bitmap, AQuery.FADE_IN,
					0.41f);
		} else {
			mAq.id(R.id.iv_event_detail_image).visibility(View.GONE);
		}

		ImageView detailImage = (ImageView)findViewById(R.id.iv_event_detail_image);
		detailImage.setOnClickListener(new OnPicClickListener());
	}
	
	private void setTextViews() {
		TextView tvEventTitle = (TextView) findViewById(R.id.tv_event_detail_title);
		TextView tvEventTime = (TextView) findViewById(R.id.tv_event_detail_time);
		TextView tvEventLocation = (TextView) findViewById(R.id.tv_event_detail_location);
		TextView tvEventOrganization = (TextView) findViewById(R.id.text_event_detail_org_name);
		TextView tvEventContent = (TextView) findViewById(R.id.tv_event_detail_content);

		tvEventTitle.setText(mEvent.getTitle());
		tvEventLocation.setText(mEvent.getLocation());
		tvEventOrganization.setText(mEvent.getOrganizer());
		tvEventContent.setText(mEvent.getDescription());
		if (DateParser.isNow(mEvent.getBegin(), mEvent.getEnd())) {
			int timeColor = getResources().getColor(
					R.color.tv_eventlst_time_now);
			tvEventTime.setTextColor(timeColor);
		} else {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time);
			tvEventTime.setTextColor(timeColor);
		}
		tvEventTime.setText(DateParser.getEventTime(this, mEvent.getBegin(),
				mEvent.getEnd()));
	}
	
	private void setLikeCheckbox() {
		mCbLike = (CheckBox) findViewById(R.id.cb_like);
		mTvLikeNum = (TextView) findViewById(R.id.tv_like_number);
		mCbLike.setChecked(!mEvent.isCanLike());
		mTvLikeNum.setText(String.valueOf(mEvent.getLike()));

		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button,
					boolean isChecked) {
				if (WTApplication.getInstance().hasAccount) {
					if (isRestCheckBox) {
						return;
					}
					likeEvent(isChecked);
					int delta = isChecked ? 1 : -1;
					mTvLikeNum.setText(String.valueOf(mEvent.getLike() + delta));
				} else {
					mCbLike.setChecked(false);
					Toast.makeText(
							EventDetailActivity.this,
							getResources().getString(
									R.string.need_account_login),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void setiChildId(int iChildId) 
	{
		// TODO Auto-generated method stub
		super.setiChildId(iChildId);
	}

	private void likeEvent(boolean isLike) {
		ApiHelper apiHelper = ApiHelper.getInstance(EventDetailActivity.this);
		int id = mEvent.getId();
		Bundle bundle = isLike ? apiHelper.likeActivity(id) : apiHelper
				.unlikeActivity(id);
	}

	private void updateEventInDB() {
		ActivityFactory factory = new ActivityFactory(null);
		ArrayList<Activity> lstTask = new ArrayList<Activity>();
		Activity newActivity = mEvent;
		newActivity.setLike(newActivity.getLike()
				+ (mCbLike.isChecked() ? 1 : -1));
		newActivity.setCanLike(!mCbLike.isChecked());
		lstTask.add(newActivity);
		//factory.saveObjects(this, lstTask);
	}
	
	private class OnPicClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			Intent intent = new Intent(EventDetailActivity.this, WTFullScreenActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(IMAGE_URL, mEvent.getImage());
			
			Bitmap bitmapTemp = mAq.getCachedImage(mEvent.getImage());
			if(bitmapTemp != null)
			{
				bundle.putInt(IMAGE_WIDTH, bitmapTemp.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmapTemp.getHeight());
			}else
			{
				Drawable drawable = getResources().getDrawable(R.drawable.image_place_holder);
				Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
				bundle.putInt(IMAGE_WIDTH, bitmap.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmap.getHeight());
			}
			
			intent.putExtras(bundle);
			startActivity(intent);
			EventDetailActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		}	
	}
}
