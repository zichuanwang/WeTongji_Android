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
import android.text.TextUtils;

/**
 * @author John
 * Helper class for easy api access
 */
/**
 * @author John
 *
 */
public class ApiHelper {
	
	private String session="";
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
	private static final String API_ARGS_CATEGORY_IDS = "Category_Ids";
	private static final String API_ARGS_SORT="Sort";
	private static final String API_ARGS_EXPIRE="Expire";
	private static final String API_ARGS_BEGIN="Begin";
	private static final String API_ARGS_END="End";
	private static final String API_ARGS_ID = "Id";
	
	public static final int API_ARGS_SORT_BY_ID_DESC = 1; 
	public static final int API_ARGS_SORT_BY_LIKE_DESC = 2; 
	public static final int API_ARGS_SORT_BY_ID = 3; 
	
	public static final int API_ARGS_CHANNEL_ACADEMIC_MASK = 1;
	public static final int API_ARGS_CHANNEL_COMPETITION_MASK = 2;
	public static final int API_ARGS_CHANNEL_EMPLOYMENT_MASK = 4;
	public static final int API_ARGS_CHANNEL_ENTERTAINMENT_MASK = 8;
	
	private void putBasicArgs(Bundle bundle){
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
	
	public void setSession(String session){
		this.session=session;
	}
	
	private void getUid(){
		AccountManager am=AccountManager.get(context);
		Account[] accounts=am.getAccountsByType(WTApplication.ACCOUNT_TYPE);
		if(accounts.length!=0){
			Account wtAccount=accounts[0];
			uid=am.getUserData(wtAccount, AccountManager.KEY_USERDATA);
		}
	}
	
	private void putLoginArgs(Bundle bundle){
		if(!TextUtils.isEmpty(session)){
			bundle.putString(API_ARGS_SESSION, session);
			bundle.putString(API_ARGS_UID, uid);
		}
	}
	
	public Bundle getUserActive(String no,String password,String name){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Active");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserLogOn(String no,String password){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.LogOn");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_PASSWORD, RSAEncrypter.encrypt(password, context));
		return bundle;
	}
	
	public Bundle getUserLogOff(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.LogOff");
		return bundle;
	}
	
	public Bundle getUserGet(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Get");
		return bundle;
	}
	
	public Bundle postUserUpdate(String updateContent){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_USER, updateContent);
		return bundle;
	}
	
	public Bundle postUserUpdateAvatar(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update.Avatar");
		//TODO object User need to be submitted
		bundle.putString(API_ARGS_IMAGE, "");
		return bundle;
	}
	
	public Bundle getUserUpdatePassword(String pwOld,String pwNew){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update.Password");
		bundle.putString(API_ARGS_OLD, RSAEncrypter.encrypt(pwOld, context));
		bundle.putString(API_ARGS_NEW, RSAEncrypter.encrypt(pwNew, context));
		return bundle;
	}
	
	public Bundle getUserResetPassword(String no,String name){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Reset.Password");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserFind(String no,String name){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Find");
		bundle.putString(API_ARGS_NO, no);
		bundle.putString(API_ARGS_NAME, name);
		return bundle;
	}
	
	public Bundle getUserProfile(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Profile");
		return bundle;
	}
	
	public Bundle postUserUpdateProfile(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update.Profile");
		//TODO object UserProfile need to be submitted
		return bundle;
	}
	
	public Bundle getSystemVersion(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "System.Version");
		return bundle;
	}
	
	public Bundle likeActivity(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.Like");
		return bundle;
	}
	
	public Bundle unlikeActivity(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.UnLike");
		return bundle;
	}
	
	public Bundle schedule(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.Schedule");
		return bundle;
	}
	
	public Bundle unSchedule(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.UnSchedule");
		return bundle;
	}
	
	public Bundle favorite(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.Favorite");
		return bundle;
	}
	
	public Bundle unFavorite(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activities.UnFavorite");
		return bundle;
	}
	
	public Bundle getActivities(int page, int channelIdsMask, int sortType, boolean expire) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		if(page < 1) {
			page = 1;
		}
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "Activities.Get");
		
		StringBuilder sbChannelId = new StringBuilder();
		if((channelIdsMask & API_ARGS_CHANNEL_ACADEMIC_MASK) != 0) {
			sbChannelId.append("1 ");
		}
		if((channelIdsMask & API_ARGS_CHANNEL_COMPETITION_MASK) != 0) {
			sbChannelId.append("2 ");
		}
		if((channelIdsMask & API_ARGS_CHANNEL_EMPLOYMENT_MASK) != 0) {
			sbChannelId.append("3 ");
		}
		if((channelIdsMask & API_ARGS_CHANNEL_ENTERTAINMENT_MASK) != 0) {
			sbChannelId.append("4 ");
		}
		if(channelIdsMask != 0) {
			bundle.putString(API_ARGS_CHANNEL_IDS,
					sbChannelId.toString().trim().replace(" ", ","));
		}
		
		String sort = "";
		switch(sortType) {
		case API_ARGS_SORT_BY_ID:
			sort = "`id`";
			break;
		case API_ARGS_SORT_BY_LIKE_DESC:
			sort = "`like` DESC";
			break;
		case API_ARGS_SORT_BY_ID_DESC:
			sort = "`id` DESC";
			break;
		}
		if(!sort.equals("")) {
			bundle.putString(API_ARGS_SORT, sort);
		}
		
		bundle.putString(API_ARGS_EXPIRE, expire ? String.valueOf(1) : String.valueOf(0));
		return bundle;
	}
	
	public Bundle getInformations(int page, int sortType, String categoryIds)
	{
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		if(page <= 0)
			page = 1;
		
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "Information.GetList");
		bundle.putString(API_ARGS_CATEGORY_IDS, categoryIds);
		
		String sort = "";
		switch(sortType) {
		case API_ARGS_SORT_BY_ID:
			sort = "`id`";
			break;
		case API_ARGS_SORT_BY_LIKE_DESC:
			sort = "`like` DESC";
			break;
		case API_ARGS_SORT_BY_ID_DESC:
			sort = "`id` DESC";
			break;
		}
		if(!sort.equals("")) {
			bundle.putString(API_ARGS_SORT, sort);
		}
		
		return bundle;
	}

	public Bundle getNotifications()
	{
		Bundle bundle = new Bundle();
		return bundle;
	}
	
	public Bundle getTimetable(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Timetable.Get");
		return bundle;
	}
	
	public Bundle getSchedule(Calendar begin, Calendar end){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Schedule.Get");
		bundle.putString(API_ARGS_BEGIN, DateParser.buildDateAndTime(begin));
		bundle.putString(API_ARGS_END, DateParser.buildDateAndTime(end));
		return bundle;
	}
	
	public Bundle getHome(){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Home");
		return bundle;
	}
	
}
