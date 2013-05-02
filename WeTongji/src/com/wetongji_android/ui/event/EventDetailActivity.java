package com.wetongji_android.ui.event;



import java.sql.SQLException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.j256.ormlite.dao.Dao;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.date.DateParser;

public class EventDetailActivity extends android.app.Activity{
	
	private Activity mEvent;
	
	private CheckBox mCbLike;
	private TextView mTvLikeNum;
	
	private DbHelper mDbHelper;
	private Dao<Activity, Integer> mEventDao = null;
	private AQuery mAq;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		recieveActivity();
		setUpUI();
		
		
	}
	
	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_event);
		
		mAq = WTApplication.getInstance().getAq(this);
		mAq.id(R.id.img_event_detail_org_avatar).image(mEvent.getOrganizerAvatar(), false, true, 0,
				R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);
		
		Drawable drawable = getResources().getDrawable(R.drawable.image_place_holder);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		mAq.id(R.id.iv_event_detail_image).image(mEvent.getImage(), false, true, 0,
				R.drawable.image_place_holder, bitmap, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
		
		mCbLike = (CheckBox)findViewById(R.id.cb_event_like);
		mTvLikeNum = (TextView)findViewById(R.id.tv_event_like_number);
		mCbLike.setChecked(!mEvent.isCanLike());
		mTvLikeNum.setText(String.valueOf(mEvent.getLike()));
		
		TextView tvEventTitle = (TextView)findViewById(R.id.tv_event_detail_title);
		TextView tvEventTime = (TextView)findViewById(R.id.tv_event_detail_time);
		TextView tvEventLocation = (TextView)findViewById(R.id.tv_event_detail_location);
		TextView tvEventOrganization = (TextView)findViewById(R.id.text_event_detail_org_name);
		TextView tvEventContent = (TextView)findViewById(R.id.tv_event_detail_content);
		
		tvEventTitle.setText(mEvent.getTitle());
		tvEventLocation.setText(mEvent.getLocation());
		tvEventOrganization.setText(mEvent.getOrganizer());
		tvEventContent.setText(mEvent.getDescription());
		if(DateParser.isNow(mEvent.getBegin(), mEvent.getEnd())) {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time_now);
			tvEventTime.setTextColor(timeColor);
		}else {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time);
			tvEventTime.setTextColor(timeColor);
		}
		tvEventTime.setText(DateParser.getEventTime(this, mEvent.getBegin(), mEvent.getEnd()));
		
		Log.d("activity", mEvent.getTitle());

		
	}
	
	private void recieveActivity() {
		mDbHelper = WTApplication.getInstance().getDbHelper();
		try {
			mEventDao = mDbHelper.getActDao();
		} catch (SQLException e) {
			throw new RuntimeException("Error for get Dao");
		}
		
		Intent intent = this.getIntent();
		int eventId = intent.getIntExtra(EventsFragment.BUNDLE_KEY_ACTIVITY, 0);
		try {
			mEvent = mEventDao.queryForId(eventId);
		} catch (SQLException e) {
			throw new RuntimeException("Did not get Activity with " + eventId);
		}
	}
	
	
	
	
	
}
