package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

import com.wetongji_android.data.Exam;
import com.wetongji_android.util.common.WTApplication;

public class ExamFactory extends BaseFactory<Exam, Integer> {

	public ExamFactory(Fragment fragment) {
		super(fragment, Exam.class, WTApplication.EXAMS_SAVER);
	}

	@Override
	public List<Exam> createObjects(String jsonStr) {
		List<Exam> result=new ArrayList<Exam>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			result=createObjects(outer.getString("Exams"), true);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Exam> parseObjects(String jsonStr) {
		String array = "";
		try {
			JSONObject outer = new JSONObject(jsonStr);
			array = outer.getString("Exams");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return unserializeObjects(array);
	}
}
