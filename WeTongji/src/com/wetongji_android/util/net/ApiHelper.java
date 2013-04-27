package com.wetongji_android.util.net;

import java.io.IOException;
import java.util.Calendar;

import com.wetongji_android.util.auth.RSAEncrypter;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
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
public class ApiHelper {
	
	private String session;
	private String uid;
	private Context context;
	
	static class SinglentonHolder{
		static ApiHelper instance;
	}
	
	private ApiHelper(Context context){
		this.context=context;
		getSession();
		getUid();
	}
	
	public static ApiHelper getInstance(Context context){
		if(SinglentonHolder.instance==null){
			SinglentonHolder.instance=new ApiHelper(context);
		}
		return SinglentonHolder.instance;
	}
	
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
	private static final String API_ARGS_CHANNEL_IDS="Channel_Ids";
	private static final String API_ARGS_SORT="Sort";
	private static final String API_ARGS_EXPIRE="Expire";
	private static final String API_ARGS_BEGIN="Begin";
	private static final String API_ARGS_END="End";
	
	private Bundle bundle=new Bundle();
	
	private void putBasicArgs(){
		bundle.putString(API_ARGS_DEVICE, WTApplication.API_DEVICE);
		bundle.putString(API_ARGS_VERSION, WTApplication.API_VERSION);
	}
	
	@SuppressWarnings("deprecation")
	private void getSession(){
		AccountManager am=AccountManager.get(context);
		Account[] accounts=am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if(accounts.length!=0){
			Account wtAccount=accounts[0];
			am.getAuthToken(wtAccount, WTApplication.AUTHTOKEN_TYPE, true, new AccountManagerCallback<Bundle>() {
				
				@Override
				public void run(AccountManagerFuture<Bundle> amf) {
					try {
						ApiHelper.this.session=amf.getResult().getString(AccountManager.KEY_AUTHTOKEN);
					} catch (OperationCanceledException e) {
						e.printStackTrace();
					} catch (AuthenticatorException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					};
				}
			}, null);
		}
	}
	
	private void getUid(){
		AccountManager am=AccountManager.get(context);
		Account[] accounts=am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if(accounts.length!=0){
			Account wtAccount=accounts[0];
			uid=am.getUserData(wtAccount, AccountManager.KEY_USERDATA);
		}
	}
	
	private void putLoginArgs(){
		bundle.putString(API_ARGS_SESSION, session);
		bundle.putString(API_ARGS_UID, uid);
	}
	
	public Bundle getUserActive(String no,String password,String name){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Active");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserLogOn(String no,String password){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.LogOn");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		return bundle;
	}
	
	public Bundle getUserLogOff(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.LogOff");
		return bundle;
	}
	
	public Bundle getUserGet(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Get");
		return bundle;
	}
	
	public Bundle postUserUpdate(String updateContent){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_USER, updateContent);
		return bundle;
	}
	
	public Bundle postUserUpdateAvatar(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Avatar");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_IMAGE, "");
		return bundle;
	}
	
	public Bundle getUserUpdatePassword(String pwOld,String pwNew){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Password");
		bundle.putString(API_ARGS_OLD, RSAEncrypter.encrypt(pwOld, context));
		bundle.putString(API_ARGS_NEW, RSAEncrypter.encrypt(pwNew, context));
		return bundle;
	}
	
	public Bundle getUserResetPassword(String no,String name){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "User.Reset.Password");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserFind(String no,String name){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Find");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserProfile(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Profile");
		return bundle;
	}
	
	public Bundle postUserUpdateProfile(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "User.Update.Profile");
		//TODO object UserProfile need to be submitted
		return bundle;
	}
	
	public Bundle getSystemVersion(){
		bundle.clear();
		putBasicArgs();
		bundle.putString(API_ARGS_METHOD, "System.Version");
		return bundle;
	}
	
	public Bundle getActivities(int page, String ids, String sort, boolean expire) {
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		
		if(page < 1) {
			page = 1;
		}
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "Activities.Get");
		if(!ids.equals("")) {
			bundle.putString(API_ARGS_CHANNEL_IDS, ids);
		}
		if(!sort.equals("")) {
			bundle.putString(API_ARGS_SORT, sort);
		}
		bundle.putString(API_ARGS_EXPIRE, String.valueOf(expire));
		return bundle;
	}
	
	public Bundle getTimetable(){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "Timetable.Get");
		return bundle;
	}
	
	public Bundle getSchedule(Calendar begin, Calendar end){
		bundle.clear();
		putBasicArgs();
		putLoginArgs();
		bundle.putString(API_ARGS_METHOD, "Schedule.Get");
		bundle.putString(API_ARGS_BEGIN, DateParser.buildDateAndTime(begin));
		bundle.putString(API_ARGS_END, DateParser.buildDateAndTime(end));
		return bundle;
	}
	
}
