package com.wetongji_android.ui.now;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.util.common.WTBaseDetailActivity;

public class CourseDetailActivity extends WTBaseDetailActivity{

	public static final String BUNDLE_COURSE = "BUNDLE_COURSE";
	private Course mCourse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_course_detail);
		recieveData();
		setUpUI();
	}

	private void recieveData() {
		Intent intent = this.getIntent();
		mCourse = intent.getExtras().getParcelable(BUNDLE_COURSE);
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
		//TODO
		//tvTime.setText(mCourse.get)
		tvLocation.setText(mCourse.getLocation());
		tvTeacher.setText(mCourse.getTeacher());
		tvCourseNo.setText(mCourse.getNO());
		tvCredit.setText(String.valueOf(mCourse.getPoint()));
		tvHours.setText(String.valueOf(mCourse.getHours()));
		tvType.setText(mCourse.isRequired());
	}
	
}