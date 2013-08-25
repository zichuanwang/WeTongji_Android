package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Event;
import com.wetongji_android.data.Exam;
import com.wetongji_android.data.Schedule;
import com.wetongji_android.util.common.WTApplication;


public class ScheduleFactory extends BaseFactory<Schedule, Integer>{
	private ActivityFactory actFactory;
	private CourseFactory courseFactory;
	private ExamFactory examFactory;
	private List<Event> resultList;

	public ScheduleFactory(Fragment fragment) {
		super(fragment, Schedule.class, WTApplication.EVENTS_SAVER);
		actFactory = new ActivityFactory(fragment);
		courseFactory = new CourseFactory(fragment);
		examFactory = new ExamFactory(fragment);
		resultList = new ArrayList<Event>();
	}

	public List<Event> createSchedule(int weekNum, String jsonStr) {
		
		parseSchedule(weekNum, jsonStr);
		
		Bundle args = new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, false);
		fragment.getLoaderManager().initLoader(WTApplication.EVENTS_SAVER, args, this).forceLoad();
		
		return resultList;
	}
	
	public List<Event> parseSchedule(int weekNum, String jsonStr) {
		List<Activity> acts =actFactory.parseObjects(jsonStr);
		List<Course> courses = courseFactory.parseObjects(jsonStr);
		List<Exam> exams = examFactory.parseObjects(jsonStr);
		
		resultList.clear();
		resultList.addAll(acts);
		resultList.addAll(courses);
		resultList.addAll(exams);
		Collections.sort(resultList);

		// store raw JSON string to db
		Schedule schedule = new Schedule();
		schedule.setJsonContent(jsonStr);
		schedule.setWeekNum(weekNum);
		list.clear();
		list.add(schedule);
		return resultList;
	}
}
