package com.wetongji_android.ui.event;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class EventDetailActivity extends FragmentActivity implements LoaderCallbacks<HttpRequestResult>{
	private static final String BUNDLE_KEY_IS_LIKE = "bundle_key_is_like";
	
	private Activity mEvent;
	
	private CheckBox mCbLike;
	private boolean isRestCheckBox = false;
	private TextView mTvLikeNum;
	
	private AQuery mAq;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		recieveActivity();
		setUpUI();
		
		
	}
	
	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_event_detail);
		
		mAq = WTApplication.getInstance().getAq(this);
		mAq.id(R.id.img_event_detail_org_avatar).image(mEvent.getOrganizerAvatar(), false, true, 0,
				R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);
		
		Drawable drawable = getResources().getDrawable(R.drawable.image_place_holder);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        if(!mEvent.getImage().equals(WTApplication.MISSING_IMAGE_URL)) {
        	mAq.id(R.id.iv_event_detail_image).image(mEvent.getImage(), false, true, 0,
        			R.drawable.image_place_holder, bitmap, AQuery.FADE_IN, 0.41f);
        }else {
        	mAq.id(R.id.iv_event_detail_image).visibility(View.GONE);
        }
		
        setLikeCheckbox();
		
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
		
	}
	
	private void recieveActivity() {
		Intent intent = this.getIntent();
		mEvent = intent.getParcelableExtra(EventsFragment.BUNDLE_KEY_ACTIVITY);
	}
	
	
	private void setLikeCheckbox() {
		mCbLike = (CheckBox)findViewById(R.id.cb_event_like);
		mTvLikeNum = (TextView)findViewById(R.id.tv_event_like_number);
		mCbLike.setChecked(!mEvent.isCanLike());
		mTvLikeNum.setText(String.valueOf(mEvent.getLike()));
		
		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isRestCheckBox) {
					return;
				}
				likeEvent(isChecked);
				int delta = isChecked ? 1 : -1;
				mTvLikeNum.setText(String.valueOf(mEvent.getLike() + delta));
			}
		});
	}
	
	private void likeEvent(boolean isLike) {
		ApiHelper apiHelper = ApiHelper.getInstance(EventDetailActivity.this);
		int id = mEvent.getId();
		Bundle bundle = isLike ? apiHelper.likeActivity(id) : apiHelper.unlikeActivity(id);
		getSupportLoaderManager().restartLoader(WTApplication.EVENT_Like_LOADER,
				bundle, this);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) {
		if (arg1.getResponseCode() == 0) {
			Toast.makeText(this, getResources().getString(R.string.text_u_like_this_activity),
					Toast.LENGTH_SHORT).show();
			updateEventInDB();
		} else {
			ExceptionToast.show(this, arg1.getResponseCode());
			mTvLikeNum.setText(String.valueOf(mEvent.getLike()));
			isRestCheckBox = true;
			mCbLike.setChecked(!mCbLike.isChecked());
			isRestCheckBox = false;
		}
	}
	
	private void  updateEventInDB() {
		ActivityFactory factory = new ActivityFactory(null);
		ArrayList<Activity> lstTask = new ArrayList<Activity>();
		Activity newActivity = mEvent;
		newActivity.setLike(newActivity.getLike() + (mCbLike.isChecked() ? 1 : -1));
		newActivity.setCanLike(!mCbLike.isChecked());
		lstTask.add(newActivity);
		factory.saveObjects(this, lstTask);
	}
	
	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
}
