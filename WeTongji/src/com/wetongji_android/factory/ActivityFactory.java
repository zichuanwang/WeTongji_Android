package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Pair;

import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;

public class ActivityFactory extends BaseFactory<Activity, Integer>{

	public ActivityFactory(Fragment fragment) {
		super(fragment, Activity.class, WTApplication.ACTIVITIES_SAVER);
	}

	public Pair<Integer, List<Activity>> createObjects(String jsonStr, int currentPage) {
		List<Activity> result=new ArrayList<Activity>();
		int nextPager=0;
		try {
			JSONObject outer=new JSONObject(jsonStr);
			nextPager=outer.getInt("NextPager");
			if(currentPage!=1){
				result=super.createObjects(outer.getString("Activities"),false);
			}
			else{
				result=super.createObjects(outer.getString("Activities"),true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Pair<Integer, List<Activity>>(nextPager, result);
	}
	
	public List<Activity> createObjects(String jsonSrt){
		JSONObject outer;
		try {
			outer = new JSONObject(jsonSrt);
			return super.createObjects(outer.getString("Activities"), true);
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<Activity>();
		}
	}
	
}
