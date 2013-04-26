package com.wetongji_android.factory;

import com.google.gson.Gson;
import com.wetongji_android.data.Version;

public class VersionFactory{

	public Version createObject(String jsonStr){
		Version version=new Version();
		Gson gson=new Gson();
		version=gson.fromJson(jsonStr, Version.class);
		return version;
	}
	
}
