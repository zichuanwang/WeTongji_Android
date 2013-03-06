package com.wetongji3.util.factory;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji3.data.Version;

public class WTVersionFactory{

	public static Version create(JSONObject result) throws JSONException {
		Version version=new Version();
		JSONObject object=result.getJSONObject("Version");
		Gson gson=new Gson();
		version=gson.fromJson(object.toString(), Version.class);
		return version;
	}

}
