package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.Exam;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.exam.ExamUtil;

public class ExamFactory extends BaseFactory<Exam, Integer> {

	public ExamFactory(Fragment fragment) {
		super(fragment, Exam.class, WTApplication.EVENTS_SAVER);
	}

	@Override
	public List<Exam> createObjects(String jsonStr) {
		List<Exam> result=new ArrayList<Exam>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			JSONArray array=outer.getJSONArray("Exams");
			for(int i=0;i!=array.length();i++){
				Exam exam=new Exam();
				JSONObject jsonExam=array.getJSONObject(i);
				exam.setNO(jsonExam.getString("NO"));
				exam.setHours(jsonExam.getInt("Hours"));
				exam.setPoint((float) jsonExam.getDouble("Point"));
				exam.setTitle(jsonExam.getString("Name"));
				exam.setTeacher(jsonExam.getString("Teacher"));
				exam.setLocation(jsonExam.getString("Location"));
				exam.setRequired(jsonExam.getBoolean("Required"));
				exam.setBegin(ExamUtil.parseExamDate(jsonExam.getString("Begin")));
				exam.setEnd(ExamUtil.parseExamDate(jsonExam.getString("End")));
				result.add(exam);
			}

			Bundle args=new Bundle();
			args.putBoolean(ARG_NEED_TO_REFRESH, true);
			fragment.getLoaderManager().initLoader(WTApplication.EVENTS_SAVER, args, this).forceLoad();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
