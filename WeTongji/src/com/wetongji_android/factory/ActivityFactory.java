package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;

public class ActivityFactory extends BaseFactory<Activity, Integer>{
	
	private int nextPage;
	private Gson gson = new GsonBuilder().
			setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	
	public ActivityFactory(Fragment fragment) {
		super(fragment, Activity.class, WTApplication.ACTIVITIES_SAVER);
	}

	private Activity createObject(String jsonStr){
		return gson.fromJson(jsonStr, Activity.class);
	}
	
	public List<Activity> createObjects(String jsonStr, boolean bRefresh) {
		List<Activity> result=new ArrayList<Activity>();
		Log.v("refresh", "" + bRefresh);
		try {
			JSONObject outer=new JSONObject(jsonStr);
			if(outer.has("NextPager")){
				nextPage = outer.getInt("NextPager");
			}
			
			if(outer.has("Like")){
				JSONArray activities = outer.getJSONArray("Like");
				for(int i = 0; i < activities.length(); i++){
					JSONObject likeObject = activities.getJSONObject(i);
					result.add(createObject(likeObject.getJSONObject("ModelDetails").toString()));
				}
			}else{
				result = bRefresh ? super.createObjects(outer.getString("Activities"),bRefresh)
						: super.unserializeObjects(outer.getString("Activities"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<Activity> createObjects(String jsonStr){
		JSONObject outer;
		try {
			outer = new JSONObject(jsonStr);
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
