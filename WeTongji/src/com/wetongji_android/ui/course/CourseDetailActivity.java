package com.wetongji_android.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.date.DateParser;

public class CourseDetailActivity extends WTBaseDetailActivity{

	public static final String BUNDLE_COURSE = "BUNDLE_COURSE";
	private Course mCourse;
	
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
		setiChildId(mCourse.getId());
		setType(this.getClass().getSimpleName());
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
	}

	@Override
	protected void setiChildId(int iChildId) {
		super.setiChildId(iChildId);
	}

	@Override
	protected void setType(String type) {
		super.setType(type);
	}
}