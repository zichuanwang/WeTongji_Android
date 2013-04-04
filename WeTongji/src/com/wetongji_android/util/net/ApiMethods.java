package com.wetongji_android.util.net;

import com.wetongji_android.util.auth.RSAEncrypter;
import com.wetongji_android.util.common.WTApplication;

import android.content.Context;
import android.os.Bundle;

/**
 * @author John
 * Helper class for easy api access
 */
public class ApiMethods {
	// api arguments
	private static final String API_ARGS_DEVICE="D";
	public static final String API_ARGS_METHOD="M";
	private static final String API_ARGS_VERSION="V";
	private static final String API_ARGS_UID="U";
	private static final String API_ARGS_PAGE="P";
	private static final String API_ARGS_SESSION="S";
	
	private static Bundle bundle=new Bundle();
	
	private static void putBasicArgs(){
		bundle.putString(API_ARGS_DEVICE, WTApplication.API_DEVICE);
		bundle.putString(API_ARGS_VERSION, WTApplication.API_VERSION);
	}
	
	public static Bundle getUserLogOn(String no,String password,Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.LogOn");
		bundle.putString("NO", no);
		bundle.putString("Password", RSAEncrypter.encrypt(password, context));
		return bundle;
	}
	
	public static Bundle getSystemVersion(){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "System.Version");
		return bundle;
	}
	
}
