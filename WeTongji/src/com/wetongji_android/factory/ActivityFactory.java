package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;

public class ActivityFactory extends BaseFactory<Activity, Integer>{
	
	private int nextPage;
	
	public ActivityFactory(Fragment fragment) {
		super(fragment, Activity.class, WTApplication.ACTIVITIES_SAVER);
	}

	public List<Activity> createObjects(String jsonStr, boolean bRefresh) {
		Log.v("need to refresh", "" + bRefresh);
		List<Activity> result=new ArrayList<Activity>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			nextPage = outer.getInt("NextPager");
			result = bRefresh ? super.createObjects(outer.getString("Activities"),bRefresh)
					: super.unserializeObjects(outer.getString("Activities"));
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

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
}
