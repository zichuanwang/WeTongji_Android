package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

import com.wetongji_android.data.Exam;

public class ExamFactory extends BaseFactory<Exam, Integer> {

	public ExamFactory(Fragment fragment) {
		super(fragment, Exam.class);
	}

	@Override
	public List<Exam> createObjects(String jsonStr) {
		try {
			JSONObject outer=new JSONObject(jsonStr);
			return super.createObjects(outer.getString("Exams"), false);
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<Exam>();
		}
	}
	
}
