package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;

public class ActivityFactory extends BaseFactory<Activity, Integer>{

	public ActivityFactory(Fragment fragment) {
		super(fragment, Activity.class, WTApplication.ACTIVITIES_SAVER);
	}

	public List<Activity> createObjects(String jsonStr, boolean bRefresh) {
		List<Activity> result=new ArrayList<Activity>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			result=super.createObjects(outer.getString("Activities"),bRefresh);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
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
