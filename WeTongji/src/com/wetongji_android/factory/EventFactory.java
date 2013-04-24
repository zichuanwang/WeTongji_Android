package com.wetongji_android.factory;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji_android.data.Activity;

public class EventFactory {
	private Activity activity=new Activity();

	public void create(JSONObject jsonObject) throws Exception {
		Gson gson=new Gson();
		Activity activity=new Activity();
		activity=gson.fromJson(jsonObject.toString(), Activity.class);
		setActivity(activity);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
