package com.wetongji_android.ui.course;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.factory.CourseFactory;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.date.DateParser;

public class CourseDetailActivity extends WTBaseDetailActivity {

	public static final String BUNDLE_COURSE = "BUNDLE_COURSE";
	private Course mCourse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recieveData();
		setContentView(R.layout.activity_course_detail);
		
		setUpUI();
		showBottomActionBar();
	}

	private void recieveData() {
		Intent intent = this.getIntent();
		mCourse = (Course) (intent.getExtras().getParcelable(BUNDLE_COURSE));
		setiChildId(mCourse.getNO());
		setModelType("Course");
		setShareContent(mCourse.getTitle() + "——" + mCourse.getTeacher());
		setLike(mCourse.getLike());
		setiFriendsCount(mCourse.getFriendsCount());
		setCanLike(mCourse.isCanLike());
		setbSchedule(mCourse.isCanSchedule());
		setbAudit(mCourse.isIsAudit());
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
		TextView tvTime1 = (TextView) findViewById(R.id.text_course_class_time_1_value);
		TextView tvTimeType1 = (TextView) findViewById(R.id.text_course_time_type_1_value);
		TextView tvLocation1 = (TextView) findViewById(R.id.text_course_location_1_value);
		TextView tvTime2 = (TextView) findViewById(R.id.text_course_class_time_2_value);
		TextView tvTimeType2 = (TextView) findViewById(R.id.text_course_time_type_2_value);
		TextView tvLocation2 = (TextView) findViewById(R.id.text_course_location_2_value);

		tvTitle.setText(mCourse.getTitle());

		if (DateParser.isNow(mCourse.getBegin(), mCourse.getEnd())) {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time_now);
			tvTime.setTextColor(timeColor);
		} else {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time);
			tvTime.setTextColor(timeColor);
		}
		tvTime.setText(DateParser.getEventTime(this, mCourse.getBegin(), mCourse.getEnd()));
		tvLocation.setText(mCourse.getLocation());
		tvTeacher.setText(mCourse.getTeacher());
		tvCourseNo.setText(mCourse.getNO());
		tvCredit.setText(String.valueOf(mCourse.getPoint()));
		tvHours.setText(String.valueOf(mCourse.getHours()));
		tvType.setText(mCourse.isRequired());

		tvTime1.setText(mCourse.getS1_WeekDay() + " 第" + mCourse.getS1_Begin() + "~" + mCourse.getS1_End()
				+ "节");
		tvTimeType1.setText(mCourse.getS1_TimeType());
		tvLocation1.setText(mCourse.getS1_Location());

		if (mCourse.getS2_Begin() == null) {
			this.findViewById(R.id.course_detail_section2).setVisibility(View.GONE);
		} else {
			tvTime2.setText(mCourse.getS2_WeekDay() + " 第" + mCourse.getS2_Begin() + "~"
					+ mCourse.getS2_End() + "节");
			tvTimeType2.setText(mCourse.getS2_TimeType());
			tvLocation2.setText(mCourse.getS2_Location());
		}
	}

	@Override
	protected void updateObjectInDB() {
		List<Course> data = new ArrayList<Course>(1);
		mCourse.setLike(getLike());
		mCourse.setCanLike(!isCanLike());
		data.add(mCourse);
		CourseFactory factory = new CourseFactory(null);
		factory.saveObjects(this, data, false);
	}

	@Override
	protected void updateDB() {
		List<Course> data = new ArrayList<Course>(1);
		mCourse.setCanSchedule(isbSchedule());
		data.add(mCourse);
		CourseFactory factory = new CourseFactory(null);
		factory.saveObjects(this, data, false);
	}
}