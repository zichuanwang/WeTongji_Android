package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Sections;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.course.CourseUtil;

public class CourseFactory extends BaseFactory<Course, Integer> {

	private Gson gson = new Gson();
	
	public CourseFactory(Fragment fragment) {
		super(fragment, Course.class, WTApplication.COURSES_SAVER);
	}

	public CourseFactory() {
		super(null, Course.class, WTApplication.COURSES_SAVER);
	}

	/*@Override
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
				course.setCanLike(jsonDetails.getBoolean("CanLike"));
				course.setCanSchedule(jsonDetails.getBoolean("CanSchedule"));
				course.setIsAudit(jsonDetails.getBoolean("IsAudit"));
				course.setUNO(jsonCourse.getString("UNO"));
				
				list.add(course);
			}

			Bundle args = new Bundle();
			args.putBoolean(ARG_NEED_TO_REFRESH, true);
			fragment.getLoaderManager().initLoader(WTApplication.COURSES_SAVER, args, this).forceLoad();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}*/

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
				course.setLocation(jsonCourse.optString("Location"));
				course.setRequired(jsonCourse.getString("Required"));
				course.setWeekDay(jsonCourse.getString("WeekDay"));
				String strDay = jsonCourse.getString("Day");
				int startSection = jsonCourse.getInt("SectionStart");
				int endSection = jsonCourse.getInt("SectionEnd");
				course.setBegin(CourseUtil.parseCourseStartTime(strDay, startSection));
				course.setEnd(CourseUtil.parseCourseEndTime(strDay, endSection));
				
				JSONObject courseDetail = jsonCourse.getJSONObject("CourseDetails");
				course.setCanLike(courseDetail.getBoolean("CanLike"));
				course.setCanSchedule(courseDetail.getBoolean("CanSchedule"));
				course.setIsAudit(courseDetail.getBoolean("IsAudit"));
				course.setFriendsCount(courseDetail.getInt("FriendsCount"));
				course.setLike(courseDetail.getInt("Like"));
				
				JSONArray sect = courseDetail.getJSONArray("Sections");
				ArrayList<Sections> sections = new ArrayList<Sections>();
				for(int j = 0; j < sect.length(); j++) {
					sections.add(gson.fromJson(sect.getJSONObject(j).toString(), Sections.class));
				}
				course.setSections(sections);
				
				list.add(course);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Course> parseCoursesForUser(String jsonStr) {
		list.clear();
		
		try {
			JSONObject outer = new JSONObject(jsonStr);
			JSONArray array = outer.getJSONArray("Courses");
			for(int i = 0; i != array.length(); i++) {
				Course course = new Course();
				JSONObject json = array.getJSONObject(i);
				course.setIsAudit(json.getBoolean("IsAudit"));
				course.setCanLike(json.getBoolean("CanLike"));
				course.setCanSchedule(json.getBoolean("CanSchedule"));
				course.setFriendsCount(json.getInt("FriendsCount"));
				course.setHours(json.getInt("Hours"));
				course.setLike(json.getInt("Like"));
				course.setTitle(json.getString("Name"));
				course.setNO(json.getString("NO"));
				course.setPoint((float)json.getDouble("Point"));
				course.setRequired(json.getString("Required"));
				course.setTeacher(json.getString("Teacher"));
				course.setTitle(json.getString("Name"));
				course.setUNO(json.getString("UNO"));
				JSONArray sect = json.getJSONArray("Sections");
				ArrayList<Sections> sections = new ArrayList<Sections>();
				for(int j = 0; j < sect.length(); j++) {
					sections.add(gson.fromJson(sect.getJSONObject(j).toString(), Sections.class));
				}
				course.setSections(sections);
				course.setLocation(sect.getJSONObject(0).getString("Location"));
				course.setWeekDay(sect.getJSONObject(0).getString("WeekDay"));
				int startSection = sect.getJSONObject(0).getInt("SectionStart");
				int endSection = sect.getJSONObject(0).getInt("SectionEnd");
				
				String strDay = "2013-05-13T00:00:00+00:00";
				course.setBegin(CourseUtil.parseCourseStartTime(strDay, startSection));
				course.setEnd(CourseUtil.parseCourseStartTime(strDay, endSection));
				list.add(course);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
