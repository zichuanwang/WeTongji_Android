package com.wetongji_android.factory;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Event;
import com.wetongji_android.data.Exam;
import com.wetongji_android.util.common.WTApplication;

public class EventFactory extends BaseFactory<Event, Integer>{
	
	private ActivityFactory actFactory;
	private CourseFactory courseFactory;
	private ExamFactory examFactory;

	public EventFactory(Fragment fragment) {
		super(fragment, Event.class);
		actFactory=new ActivityFactory(fragment);
		courseFactory=new CourseFactory(fragment);
		examFactory=new ExamFactory(fragment);
	}

	@Override
	public List<Event> createObjects(String jsonStr) {
		list.clear();
		
		List<Activity> acts=actFactory.createObjectsWithoutWriteToDb(jsonStr);
		List<Course> courses=courseFactory.createObjects(jsonStr);
		List<Exam> exams=examFactory.createObjects(jsonStr);
		
		list.addAll(acts);
		list.addAll(courses);
		list.addAll(exams);
		
		Collections.sort(list);

		Bundle args=new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, true);
		fragment.getLoaderManager().initLoader(WTApplication.DB_LIST_SAVER, args, this).forceLoad();
		
		return list;
	}
	
	
	
}
