package com.wetongji_android.ui.course;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Sections;
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
		

		tvTitle.setText(mCourse.getTitle());

		if (DateParser.isNow(mCourse.getBegin(), mCourse.getEnd())) {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time_now);
			tvTime.setTextColor(timeColor);
		} else {
			int timeColor = getResources().getColor(R.color.tv_eventlst_time);
			tvTime.setTextColor(timeColor);
		}
		Calendar start = Calendar.getInstance();
		start.setTime(mCourse.getBegin());
		Calendar end = Calendar.getInstance();
		end.setTime(mCourse.getEnd());
		StringBuilder sb = new StringBuilder();
		sb.append(start.get(Calendar.HOUR_OF_DAY)).append(":").append(start.get(Calendar.MINUTE))
			.append("-").append(end.get(Calendar.HOUR_OF_DAY)).append(":").append(end.get(Calendar.MINUTE))
			.append(" ").append(mCourse.getWeekDay());
		tvTime.setText(sb.toString());
		tvLocation.setText(mCourse.getLocation());
		tvTeacher.setText(mCourse.getTeacher());
		tvCourseNo.setText(mCourse.getNO());
		tvCredit.setText(String.valueOf(mCourse.getPoint()));
		tvHours.setText(String.valueOf(mCourse.getHours()));
		tvType.setText(mCourse.isRequired());

		LinearLayout total = (LinearLayout)this.findViewById(R.id.course_detail_total);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT);
		params.setMargins(8, 8, 8, 8);
		ArrayList<Sections> sections = mCourse.getSections();
		for(int i = 0; i < sections.size(); i++) {
			total.addView(createSection(sections.get(i), i + 1), params);
		}
	}

	private View createSection(Sections sections, int index) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View section = inflater.inflate(R.layout.course_detail_section, null);
		
		TextView tvLabel = (TextView)section.findViewById(R.id.course_detail_time_label);
		tvLabel.setText("Time " + index);
		TextView tvTime = (TextView)section.findViewById(R.id.course_detail_time);
		tvTime.setText(sections.getWeekDay());
		TextView tvType = (TextView)section.findViewById(R.id.course_detail_type);
		tvType.setText(sections.getWeekType());
		TextView tvLocation = (TextView)section.findViewById(R.id.course_detail_location);
		tvLocation.setText(sections.getLocation());
		
		return section;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
		}
		
		return super.onKeyDown(keyCode, event);
	}
}