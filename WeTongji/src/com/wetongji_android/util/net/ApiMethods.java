package com.wetongji_android.util.net;

import java.io.IOException;

import com.wetongji_android.util.auth.RSAEncrypter;
import com.wetongji_android.util.common.WTApplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;

/**
 * @author John
 * Helper class for easy api access
 */
/**
 * @author John
 *
 */
public class ApiMethods {
	// api arguments
	private static final String API_ARGS_DEVICE="D";
	public static final String API_ARGS_METHOD="M";
	private static final String API_ARGS_VERSION="V";
	private static final String API_ARGS_UID="U";
	private static final String API_ARGS_PAGE="P";
	private static final String API_ARGS_SESSION="S";
	private static final String API_ARGS_NO="NO";
	private static final String API_ARGS_PASSWORD="Password";
	private static final String API_ARGS_NAME="Name";
	private static final String API_ARGS_USER="User";
	private static final String API_ARGS_IMAGE="Image";
	private static final String API_ARGS_OLD="Old";
	private static final String API_ARGS_NEW="New";
	
	private static Bundle bundle=new Bundle();
	
	private static void putBasicArgs(){
		bundle.putString(API_ARGS_DEVICE, WTApplication.API_DEVICE);
		bundle.putString(API_ARGS_VERSION, WTApplication.API_VERSION);
	}
	
	private static String getSession(Context context){
		AccountManager am=AccountManager.get(context);
		Account[] accounts=am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if(accounts.length!=0){
			Account wtAccount=accounts[0];
			AccountManagerFuture<Bundle> amf=am.getAuthToken(wtAccount, WTApplication.AUTHTOKEN_TYPE, null,true, null, null);
			try {
				return amf.getResult().getString(AccountManager.KEY_AUTHTOKEN);
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Bundle getUserActive(String no,String password,String name,Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Active");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public static Bundle getUserLogOn(String no,String password,Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.LogOn");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		return bundle;
	}
	
	public static Bundle getUserLogOff(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.LogOff");
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle getUserGet(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Get");
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle postUserUpdate(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_USER, "");
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle postUserUpdateAvatar(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Avatar");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_IMAGE, "");
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle getUserUpdatePassword(String pwOld,String pwNew,Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Password");
		bundle.putString(API_ARGS_OLD, RSAEncrypter.encrypt(pwOld, context));
		bundle.putString(API_ARGS_NEW, RSAEncrypter.encrypt(pwNew, context));
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle getUserResetPassword(String no,String name){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Reset.Password");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public static Bundle getUserFind(String no,String name,Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Find");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle getUserProfile(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Profile");
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle postUserUpdateProfile(Context context){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Profile");
		//TODO object UserProfile need to be submitted
		bundle.putString(API_ARGS_SESSION, getSession(context));
		return bundle;
	}
	
	public static Bundle getSystemVersion(){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "System.Version");
		return bundle;
	}
	
}
