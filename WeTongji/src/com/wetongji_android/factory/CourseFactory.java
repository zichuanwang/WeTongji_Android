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

	public CourseFactory() {
		super(null, Course.class, WTApplication.COURSES_SAVER);
	}

	@Override
	public List<Course> createObjects(String jsonStr) {
		list.clear();
		try {
			JSONObject outer = new JSONObject(jsonStr);
			JSONArray array = outer.getJSONArray("CourseInstances");
			for (int i = 0; i != array.length(); i++) {
				Course course = new Course();
				JSONObject jsonCourse = array.getJSONObject(i);
				course.setNO(jsonCourse.getString("NO"));
				course.setHours(jsonCourse.getInt("Hours"));
				course.setPoint((float) jsonCourse.getDouble("Point"));
				course.setTitle(jsonCourse.getString("Name"));
				course.setTeacher(jsonCourse.getString("Teacher"));
				course.setLocation(jsonCourse.getString("Location"));
				course.setRequired(jsonCourse.getString("Required"));
				String strDay = jsonCourse.getString("Day");
				int startSection = jsonCourse.getInt("SectionStart");
				int endSection = jsonCourse.getInt("SectionEnd");
				course.setBegin(CourseUtil.parseCourseStartTime(strDay, startSection));
				course.setEnd(CourseUtil.parseCourseEndTime(strDay, endSection));

				JSONObject jsonDetails = jsonCourse.getJSONObject("CourseDetails");
				JSONObject jsonSection1 = jsonDetails.getJSONArray("Sections").getJSONObject(0);
				course.setS1_Location(jsonSection1.getString("Location"));
				course.setS1_TimeType(jsonSection1.getString("WeekType"));
				course.setS1_Begin(jsonSection1.getString("SectionStart"));
				course.setS1_End(jsonSection1.getString("SectionEnd"));
				course.setS1_WeekDay(jsonSection1.getString("WeekDay"));
				if (jsonDetails.getJSONArray("Sections").length() >= 2) {
					JSONObject jsonSection2 = jsonDetails.getJSONArray("Sections").getJSONObject(1);
					course.setS2_Location(jsonSection2.getString("Location"));
					course.setS2_TimeType(jsonSection2.getString("WeekType"));
					course.setS2_Begin(jsonSection2.getString("SectionStart"));
					course.setS2_End(jsonSection2.getString("SectionEnd"));
					course.setS2_WeekDay(jsonSection2.getString("WeekDay"));
				}
				course.setLike(jsonDetails.getInt("Like"));
				course.setFriendsCount(jsonDetails.getInt("FriendsCount"));
				course.setCanLike(jsonDetails.getBoolean(("CanLike")));
				list.add(course);
			}

			Bundle args = new Bundle();
			args.putBoolean(ARG_NEED_TO_REFRESH, true);
			fragment.getLoaderManager().initLoader(WTApplication.COURSES_SAVER, args, this).forceLoad();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Course> parseObjects(String jsonStr) {
		list.clear();
		try {
			JSONObject outer = new JSONObject(jsonStr);
			JSONArray array = outer.getJSONArray("CourseInstances");
			for (int i = 0; i != array.length(); i++) {
				Course course = new Course();
				JSONObject jsonCourse = array.getJSONObject(i);
				course.setNO(jsonCourse.getString("NO"));
				course.setHours(jsonCourse.getInt("Hours"));
				course.setPoint((float) jsonCourse.getDouble("Point"));
				course.setTitle(jsonCourse.getString("Name"));
				course.setTeacher(jsonCourse.getString("Teacher"));
				course.setLocation(jsonCourse.getString("Location"));
				course.setRequired(jsonCourse.getString("Required"));
				String strDay = jsonCourse.getString("Day");
				int startSection = jsonCourse.getInt("SectionStart");
				int endSection = jsonCourse.getInt("SectionEnd");
				course.setBegin(CourseUtil.parseCourseStartTime(strDay, startSection));
				course.setEnd(CourseUtil.parseCourseEndTime(strDay, endSection));

				JSONObject jsonDetails = jsonCourse.getJSONObject("CourseDetails");
				JSONObject jsonSection1 = jsonDetails.getJSONArray("Sections").getJSONObject(0);
				course.setS1_Location(jsonSection1.getString("Location"));
				course.setS1_TimeType(jsonSection1.getString("WeekType"));
				course.setS1_Begin(jsonSection1.getString("SectionStart"));
				course.setS1_End(jsonSection1.getString("SectionEnd"));
				course.setS1_WeekDay(jsonSection1.getString("WeekDay"));
				if (jsonDetails.getJSONArray("Sections").length() >= 2) {
					JSONObject jsonSection2 = jsonDetails.getJSONArray("Sections").getJSONObject(1);
					course.setS2_Location(jsonSection2.getString("Location"));
					course.setS2_TimeType(jsonSection2.getString("WeekType"));
					course.setS2_Begin(jsonSection2.getString("SectionStart"));
					course.setS2_End(jsonSection2.getString("SectionEnd"));
					course.setS2_WeekDay(jsonSection2.getString("WeekDay"));
				}
				course.setLike(jsonDetails.getInt("Like"));
				course.setFriendsCount(jsonDetails.getInt("FriendsCount"));
				course.setCanLike(jsonDetails.getBoolean(("CanLike")));

				list.add(course);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
