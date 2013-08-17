package com.wetongji_android.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.util.net.HttpUtil;

public class CourseDetailActivity extends WTBaseDetailActivity
		implements LoaderCallbacks<HttpRequestResult> {

	public static final String BUNDLE_COURSE = "BUNDLE_COURSE";
	private Course mCourse;
	
	private TextView mFriendNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_course_detail);
		
		recieveData();
		setUpUI();
		showBottomActionBar();
	}

	private void recieveData() {
		Intent intent = this.getIntent();
		mCourse = (Course)(intent.getExtras().getParcelable(BUNDLE_COURSE));
		setiChildId(mCourse.getNO());
		setModelType("Course");
		setShareContent(mCourse.getTitle());
		
		//Get friends number with the same course
		if(WTApplication.getInstance().hasAccount){
			ApiHelper apiHelper = ApiHelper.getInstance(this);
			getSupportLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_FRIENDS, 
					apiHelper.getFriendsWithSameCourse(mCourse.getNO()), this);
		}
	}
	
	private void setUpUI() {
		TextView tvTitle = (TextView) findViewById(R.id.text_course_detail_title);
		TextView tvTime = (TextView) findViewById(R.id.text_course_detail_time);
		TextView tvLocation = (TextView) findViewById(R.id.text_course_detail_location);
		TextView tvTeacher = (TextView) findViewById(R.id.text_teacher_name);
		TextView tvCourseNo = (TextView) findViewById(R.id.text_course_number_value);
		TextView tvCredit = (TextView) findViewById(R.id.text_course_credit_value);
		TextView tvHours = (TextView) findViewById(R.id.text_course_hours_value);
		TextView tvType = (TextView) findViewById(R.id.text_course_type_name);
		mFriendNumber = (TextView)findViewById(R.id.tv_event_detail_friends);
		
		tvTitle.setText(mCourse.getTitle());
		
		if (DateParser.isNow(mCourse.getBegin(), mCourse.getEnd())) {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time_now);
			tvTime.setTextColor(timeColor);
		} else {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time);
			tvTime.setTextColor(timeColor);
		}
		tvTime.setText(DateParser.getEventTime(this, mCourse.getBegin(),
				mCourse.getEnd()));
		tvLocation.setText(mCourse.getLocation());
		tvTeacher.setText(mCourse.getTeacher());
		tvCourseNo.setText(mCourse.getNO());
		tvCredit.setText(String.valueOf(mCourse.getPoint()));
		tvHours.setText(String.valueOf(mCourse.getHours()));
		tvType.setText(mCourse.isRequired());
		mFriendNumber.setText("0");
	}

	@Override
	protected void updateObjectInDB() {
		
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if(result.getResponseCode() == 0){
			mFriendNumber.setText(String.valueOf(HttpUtil.getFriendsCountWithResponse(result.getStrResponseCon())));
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		
	}
}