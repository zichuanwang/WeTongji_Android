package com.wetongji.util.factory;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji.data.Version;

public class WTVersionFactory{

	public static Version create(JSONObject object) {
		Version version=new Version();
		Gson gson=new Gson();
		version=gson.fromJson(object.toString(), Version.class);
		return version;
	}

}
