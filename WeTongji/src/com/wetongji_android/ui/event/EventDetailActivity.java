package com.wetongji_android.ui.event;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.ui.account.AccountDetailActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTFullScreenActivity;
import com.wetongji_android.util.date.DateParser;

public class EventDetailActivity extends WTBaseDetailActivity {

	private Activity mEvent;
	private AQuery mAq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recieveActivity();

		setContentView(R.layout.activity_event_detail);

		setUpUI();
		showBottomActionBar();
	}

	private void setUpUI() {
		setTitle(getResources().getString(R.string.profile_section_events));
		setPicture();

		setTextViews();
	}

	private void recieveActivity() {
		Intent intent = this.getIntent();
		mEvent = intent.getParcelableExtra(EventsFragment.BUNDLE_KEY_ACTIVITY);
		setiChildId(String.valueOf(mEvent.getId()));
		setModelType("Activity");
		setbSchedule(mEvent.isCanSchedule());
		setShareContent(mEvent.getTitle());
		setLike(mEvent.getLike());
		setCanLike(mEvent.isCanLike());
		setiFriendsCount(mEvent.getFriendsCount());
		setSchedule(mEvent.getSchedule());
		setImagePath(mEvent.getImage());
	}

	private void setPicture() {
		mAq = WTApplication.getInstance().getAq(this);
		mAq.id(R.id.img_event_detail_org_avatar).image(
				mEvent.getOrganizerAvatar(), false, true, 0,
				R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);
		ImageView ivOrganizer = (ImageView) findViewById(R.id.img_event_detail_org_avatar);
		ivOrganizer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EventDetailActivity.this,
						AccountDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(BUNDLE_KEY_ACCOUNT,
						mEvent.getAccountDetails());
				intent.putExtras(bundle);
				startActivity(intent);
				EventDetailActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}
		});

		Drawable drawable = getResources().getDrawable(
				R.drawable.image_place_holder);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (!mEvent.getImage().equals(WTApplication.MISSING_IMAGE_URL)
				|| TextUtils.isEmpty(mEvent.getImage())) {
			mAq.id(R.id.iv_event_detail_image).image(mEvent.getImage(), false,
					true, 0, AQuery.GONE, bitmap,
					AQuery.FADE_IN, 0.41f);
		} else {
			mAq.id(R.id.iv_event_detail_image).visibility(View.GONE);
		}

		ImageView detailImage = (ImageView) findViewById(R.id.iv_event_detail_image);
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

	private class OnPicClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(EventDetailActivity.this,
					WTFullScreenActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(IMAGE_URL, mEvent.getImage());

			Bitmap bitmapTemp = mAq.getCachedImage(mEvent.getImage());
			if (bitmapTemp != null) {
				bundle.putInt(IMAGE_WIDTH, bitmapTemp.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmapTemp.getHeight());
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.image_place_holder);
				Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
				bundle.putInt(IMAGE_WIDTH, bitmap.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmap.getHeight());
			}

			intent.putExtras(bundle);
			startActivity(intent);
			EventDetailActivity.this.overridePendingTransition(
					R.anim.slide_right_in, R.anim.slide_left_out);
		}
	}

	@Override
	protected void updateObjectInDB() {
		ArrayList<Activity> data = new ArrayList<Activity>(1);
		mEvent.setLike(getLike());
		mEvent.setCanLike(!isCanLike());
		data.add(mEvent);
		ActivityFactory factory = new ActivityFactory(null);
		factory.saveObjects(this, data, false);
	}
	
	@Override
	protected void updateDB() {
		ArrayList<Activity> data = new ArrayList<Activity>(1);
		mEvent.setSchedule(getSchedule());
		mEvent.setCanSchedule(isbSchedule());
		data.add(mEvent);
		ActivityFactory factory = new ActivityFactory(null);
		factory.saveObjects(this, data, false);
	}
}
