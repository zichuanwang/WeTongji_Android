package com.wetongji_android.factory;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.Course;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.course.CourseUtil;

public class CourseFactory extends BaseFactory<Course, Integer> {

	public CourseFactory(Fragment fragment) {
		super(fragment, Course.class, WTApplication.COURSES_SAVER);
	}

	@Override
	public List<Course> createObjects(String jsonStr) {
		list.clear();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			JSONArray array=outer.getJSONArray("CourseInstances");
			for(int i=0;i!=array.length();i++){
				Course course=new Course();
				JSONObject jsonCourse=array.getJSONObject(i);
				course.setNO(jsonCourse.getString("NO"));
				course.setHours(jsonCourse.getInt("Hours"));
				course.setPoint((float) jsonCourse.getDouble("Point"));
				course.setTitle(jsonCourse.getString("Name"));
				course.setTeacher(jsonCourse.getString("Teacher"));
				course.setLocation(jsonCourse.getString("Location"));
				course.setRequired(jsonCourse.getString("Required"));
				String strDay=jsonCourse.getString("Day");
				int startSection=jsonCourse.getInt("SectionStart");
				int endSection=jsonCourse.getInt("SectionEnd");
				course.setBegin(CourseUtil.parseCourseStartTime(strDay, startSection));
				course.setEnd(CourseUtil.parseCourseEndTime(strDay, endSection));
				list.add(course);
			}

			Bundle args=new Bundle();
			args.putBoolean(ARG_NEED_TO_REFRESH, true);
			fragment.getLoaderManager().initLoader(WTApplication.COURSES_SAVER, args, this).forceLoad();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
