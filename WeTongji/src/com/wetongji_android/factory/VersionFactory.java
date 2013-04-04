package com.wetongji_android.factory;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji_android.data.Version;

public class VersionFactory{

	public static Version create(String resultStr) throws JSONException {
		Version version=new Version();
		JSONObject result=new JSONObject(resultStr);
		Gson gson=new Gson();
		version=gson.fromJson(result.toString(), Version.class);
		return version;
	}

}
