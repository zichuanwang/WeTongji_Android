package com.wetongji_android.factory;

import com.google.gson.Gson;
import com.wetongji_android.data.Version;

public class VersionFactory{
	
	private Gson gson=new Gson();

	public Version createObject(String jsonStr){
		Version version=new Version();
		version=gson.fromJson(jsonStr, Version.class);
		return version;
	}
	
}
