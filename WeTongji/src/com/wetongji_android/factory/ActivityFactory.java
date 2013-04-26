package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import com.wetongji_android.data.Activity;

public class ActivityFactory extends BaseFactory<Activity, Integer>{

	public ActivityFactory(Fragment fragment) {
		super(fragment, Activity.class);
	}

	@Override
	public List<Activity> createObjects(String jsonStr) {
		try {
			JSONObject outer=new JSONObject(jsonStr);
			int nextPager=outer.getInt("NextPager");
			if(nextPager!=2){
				return super.createObjects(outer.getString("Activities"),false);
			}
			else{
				return super.createObjects(outer.getString("Activities"), true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return new ArrayList<Activity>();
		}
	}
	
	
}
