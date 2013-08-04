package com.wetongji_android.util.net;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.wetongji_android.data.User;
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
import android.util.Log;

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
	private String uid="";
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
	private static final String API_ARGS_USER_ID = "UID";
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
	private static final String API_ARGS_UNREAD = "OnlyNew";
	private static final String API_ARGS_KEYWORDS = "Keywords";
	private static final String API_ARGS_CATEGORY = "Type";
	private static final String API_ARGS_MODEL = "Model";
	
	public static final int API_ARGS_SORT_BY_PUBLISH_DESC = 1;
	public static final int API_ARGS_SORT_BY_PUBLISH_ASC = 2;
	public static final int API_ARGS_SORT_BY_LIKE_ASC = 3;
	public static final int API_ARGS_SORT_BY_LIKE_DESC = 4; 
	public static final int API_ARGS_SORT_BY_BEGIN_ASC = 5;
	public static final int API_ARGS_SORT_BY_BEGIN_DESC = 6;
	
	public static final int API_ARGS_CHANNEL_ACADEMIC_MASK = 1;
	public static final int API_ARGS_CHANNEL_COMPETITION_MASK = 2;
	public static final int API_ARGS_CHANNEL_EMPLOYMENT_MASK = 4;
	public static final int API_ARGS_CHANNEL_ENTERTAINMENT_MASK = 8;
	
	public static final int API_ARGS_INFO_CAMPUS = 1;
	public static final int API_ARGS_INFO_ADMINISTRATIVE = 2;
	public static final int API_ARGS_INFO_CLUB = 4;
	public static final int API_ARGS_INFO_LOCAL = 8;
	
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
	
	public void setUID(String uid){
		this.uid=uid;
	}
	
	private void putLoginArgs(Bundle bundle){
		if(!TextUtils.isEmpty(session)){
			Log.v("session", "not null");
			bundle.putString(API_ARGS_SESSION, session);
			bundle.putString(API_ARGS_UID, uid);
		}else
		{
			if(WTApplication.getInstance().hasAccount)
			{
				bundle.putString(API_ARGS_SESSION, WTApplication.getInstance().session);
				bundle.putString(API_ARGS_UID, WTApplication.getInstance().uid);
			}
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
	
	public Bundle postUserUpdate(User user){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update");
		//TODO object User need to be submitted
		JSONObject json = new JSONObject();
		JSONObject userJson = new JSONObject();
		
		try {
			json.put("DisplayName", user.getDisplayName());
			json.put("Email", user.getEmail());
			json.put("SinaWeibo", user.getSinaWeibo());
			json.put("Phone", user.getPhone());
			json.put("QQ", user.getQQ());
			json.put("Room", user.getRoom());
			json.put("Words", user.getWords());
			userJson.put("User", json);
		} catch (JSONException e) {
		}
		
		bundle.putString(API_ARGS_USER, userJson.toString());
		return bundle;
	}
	
	public Bundle postUserUpdateAvatar(String path){
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "User.Update.Avatar");
		bundle.putString(API_ARGS_IMAGE, path);
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
	
	public Bundle likeInfo(int id) {
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Information.Like");
		return bundle;
	}
	
	public Bundle unLikeInfo(int id) {
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Information.UnLike");
		return bundle;
	}
	
	public Bundle likeActivity(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.Like");
		return bundle;
	}
	
	public Bundle unlikeActivity(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.UnLike");
		return bundle;
	}
	
	public Bundle schedule(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.Schedule");
		return bundle;
	}
	
	public Bundle unSchedule(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.UnSchedule");
		return bundle;
	}
	
	public Bundle favorite(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.Favorite");
		return bundle;
	}
	
	public Bundle unFavorite(int id) {
		Bundle bundle=new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_METHOD, "Activity.UnFavorite");
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
		switch(sortType) 
		{
		case API_ARGS_SORT_BY_PUBLISH_DESC:
			sort = "`created_at` DESC";
			break;
		case API_ARGS_SORT_BY_PUBLISH_ASC:
			sort = "`created_at` ASC";
			break;
		case API_ARGS_SORT_BY_LIKE_DESC:
			sort = "`like` DESC";
			break;
		case API_ARGS_SORT_BY_LIKE_ASC:
			sort = "`like` ASC";
			break;
		case API_ARGS_SORT_BY_BEGIN_DESC:
			sort = "`begin` DESC";
			break;
		case API_ARGS_SORT_BY_BEGIN_ASC:
			sort = "`begin` ASC";
			break;
		}
		
		if(!sort.equals("")) {
			bundle.putString(API_ARGS_SORT, sort);
		}
		
		bundle.putString(API_ARGS_EXPIRE, expire ? String.valueOf(1) : String.valueOf(0));
		return bundle;
	}
	
	//By default we just get information order by created_at time descending
	public Bundle getInformations(int page, int infoType)
	{
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		if(page <= 0)
			page = 1;
		
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "Information.GetList");
		bundle.putString(API_ARGS_SORT, "`created_at` DESC");
		
		StringBuilder sb = new StringBuilder();
		if((infoType & API_ARGS_INFO_CAMPUS) != 0)
		{
			sb.append("1 ");
		}
		if((infoType & API_ARGS_INFO_ADMINISTRATIVE) != 0)
		{
			sb.append("2 ");
		}
		if((infoType & API_ARGS_INFO_CLUB) != 0)
		{
			sb.append("3 ");
		}
		if((infoType & API_ARGS_INFO_LOCAL) != 0)
		{
			sb.append("4 ");
		}
		
		if(infoType != 0)
		{
			bundle.putString(API_ARGS_CATEGORY_IDS, sb.toString().trim().replace(" ", ","));
		}
		
		return bundle;
	}

	public Bundle getNotifications(boolean read)
	{
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Notifications.Get");
		bundle.putString(API_ARGS_UNREAD, read ? String.valueOf(1) : String.valueOf(0));
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
		//putLoginArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Home");
		return bundle;
	}
	
	public Bundle getPeople(int page) {
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		if(page <= 0) {
			page = 1;
		}
		bundle.putString(API_ARGS_METHOD, "People.Get");
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		
		return bundle;
	}
	
	public Bundle getFriends()
	{
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_METHOD, "Friends.Get");
		
		return bundle;
	}
	
	public Bundle getSearchResult(int category, String keywords) {
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		bundle.putString(API_ARGS_METHOD, "Search");
		bundle.putString(API_ARGS_KEYWORDS, keywords);
		if (category != 0) {
			bundle.putString(API_ARGS_CATEGORY, String.valueOf(category));
		}
		
		return bundle;
	}
	
	public Bundle getActivityInviate(int id, String uid)
	{
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		
		bundle.putString(API_ARGS_METHOD, "Activity.Invite");
		bundle.putString(API_ARGS_ID, String.valueOf(id));
		bundle.putString(API_ARGS_USER_ID, uid);
		
		return bundle;
	}
	
	public Bundle getActivityByUser(String uid, int page, int channelIdsMask,
			int sortType, boolean expire) {
		Bundle bundle = getActivities(page, channelIdsMask, sortType, true);
		bundle.putString(API_ARGS_USER_ID, uid);
		bundle.remove(API_ARGS_METHOD);
		bundle.putString(API_ARGS_METHOD, "Activities.Get.ByUser");
		return bundle;
	}
	
	public Bundle getCourseByUser(String uid, int page) {
		Bundle bundle = new Bundle();
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_USER_ID, uid);
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "CourseSections.Get.ByUser");
		return bundle;
	}
	
	public Bundle getLikedObjectsListWithModelType(int page, int modelType){
		Bundle bundle = new Bundle();
		
		putBasicArgs(bundle);
		putLoginArgs(bundle);
		bundle.putString(API_ARGS_PAGE, String.valueOf(page));
		bundle.putString(API_ARGS_METHOD, "Like.List");
		
		if(modelType == 1)
		{
			bundle.putString(API_ARGS_MODEL, "Activity");
		}else if(modelType == 2)
		{
			bundle.putString(API_ARGS_MODEL, "Information");
		}
		
		return bundle;
	}
}
