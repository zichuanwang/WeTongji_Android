package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji_android.data.Activity;

public class ActivityFactory implements ICreateWTObjects<Activity> {

	@Deprecated
	@Override
	public Activity createObject(String jsonStr) {
		Activity activity=new Activity();
		Gson gson=new Gson();
		activity=gson.fromJson(jsonStr, Activity.class);
		return activity;
	}

	@Override
	public List<Activity> createObjects(String jsonStr) {
		List<Activity> list=new ArrayList<Activity>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			JSONArray array=outer.getJSONArray("Activities");
			for(int i=0;i!=array.length();i++){
				Activity activity=createObject(array.getJSONObject(i).toString());
				list.add(activity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
