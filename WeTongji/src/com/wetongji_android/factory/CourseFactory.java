package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

import com.wetongji_android.data.Course;

public class CourseFactory extends BaseFactory<Course, Integer> {

	public CourseFactory(Fragment fragment) {
		super(fragment, Course.class);
	}

	@Override
	public List<Course> createObjects(String jsonStr) {
		try {
			JSONObject outer=new JSONObject(jsonStr);
			return super.createObjects(outer.getString("CourseInstances"),false);
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<Course>();
		}
	}
	
}
