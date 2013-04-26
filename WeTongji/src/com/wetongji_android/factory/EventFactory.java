package com.wetongji_android.factory;

import java.util.List;

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
		List<Activity> acts=actFactory.createObjects(jsonStr);
		List<Course> courses=courseFactory.createObjects(jsonStr);
		List<Exam> exams=examFactory.createObjects(jsonStr);
		
		list.addAll(acts);
		list.addAll(courses);
		list.addAll(exams);
		
		fragment.getLoaderManager().initLoader(WTApplication.DB_SAVER, null, this);
		
		return list;
	}
	
	
	
}
