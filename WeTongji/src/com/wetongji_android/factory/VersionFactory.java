package com.wetongji_android.factory;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji_android.data.Version;

public class VersionFactory implements ICreateWTObjects<Version>{

	@Override
	public Version createObject(String jsonStr){
		Version version=new Version();
		JSONObject result;
		try {
			result = new JSONObject(jsonStr);
			Gson gson=new Gson();
			version=gson.fromJson(result.toString(), Version.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return version;
	}

	@Override
	@Deprecated
	public List<Version> createObjects(String jsonStr) {
		return null;
	}

}
