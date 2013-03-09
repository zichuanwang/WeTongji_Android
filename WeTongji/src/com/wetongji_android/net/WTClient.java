package com.wetongji_android.net;

import java.io.File;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.wetongji_android.data.User;

/**
 * @author hezibo
 *
 */
public class WTClient 
{	
	private HttpClient httpClient;
	private HttpGet request;
	
	private boolean hasError;
	private String errorDesc;
	private int responseStatusCode;
	
	private String responseStr;
	private String session;
	
	private boolean sessionRequired;
	private boolean uidRequired;
	
	private Map<String, String> params;

	
	//private static String API_DOMAIN = "http://we.tongji.edu.cn/api/call";
	private static String API_DOMAIN="http://leiz.name:8080/api/call";// test server
	private static String SORTTYPE_BEGIN = "`begin`";
	private static String SORTTYPE_LIKEDESC = "`like`";
	private static String SORTTYPE_PUBLISHDESC="`id`";

	//实现单例模式
	private WTClient()
	{
		httpClient = new DefaultHttpClient();
		request = new HttpGet();
		setHasError(false);
		setErrorDesc(null);
		setResponseStatusCode(0);
		setSessionRequired(false);
		setUidRequired(false);
		params = new HashMap<String, String>();
	}
	
	//实现单例模式
	private static class WTClientContainer
	{
		private static WTClient client = new WTClient();
	}
	
	public static synchronized WTClient getInstance()
	{
		return WTClientContainer.client;
	}
	
	
	public String hashQueryString(String s)
	{
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
				'a', 'b', 'c', 'd', 'e', 'f' };   
		try {   
			byte[] strTemp = s.getBytes();   
			//ʹ��MD5����MessageDigest����   
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");   
			mdTemp.update(strTemp);   
			byte[] md = mdTemp.digest();   
			int j = md.length;   
			char str[] = new char[j * 2];   
			int k = 0;   
			for (int i = 0; i < j; i++) {   
				byte b = md[i];   
				//System.out.println((int)b);   
				//��û����(int)b����˫�ֽڼ���   
				str[k++] = hexDigits[b >> 4 & 0xf];   
				str[k++] = hexDigits[b & 0xf];   
			}   
			return new String(str);   
		} 
		catch (Exception e) {
			return null;
		} 
	}
	
	//形成排序后的参数�?
	public String queryString()
	{
		String str = "?";
		List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(params.entrySet());
		
		Collections.sort(mappingList, new Comparator<Map.Entry<String, String>>(){
					public int compare(Map.Entry<String, String> entry1, Map.Entry<String, String> entry2)
					{
						return entry1.getKey().compareTo(entry2.getKey());
					}
				}
				);
		
		for(Map.Entry<String, String> entry : mappingList)
		{
			str += entry.getKey() + "=" + entry.getValue();
			str += "&";
		}
		
		String subStr = str.substring(1, str.length() - 1);
		return subStr;
	}
	
	//形成新的URL�?
	public String buildURL()
	{
		params.put("D", "android1.0.0");
		params.put("V", "1.2");
		String queryStr = queryString();
		String hashStr = hashQueryString(queryStr);
		String url = API_DOMAIN + "?" + queryStr + "&H=" + hashStr;
		Log.i("queryURL", url);
		
		return url;
	}

	//执行request
	public void executeRequest() throws Exception
	{
		URI finalURI = new URI(this.buildURL());
		request.setURI(finalURI);
		
		
		HttpResponse response = httpClient.execute(request);
		
		switch(response.getStatusLine().getStatusCode())
		{
			case 200:
			{
				this.requestFinished(response);
			}
				break;
			default:
			{
				//出现网络错误
				this.setHasError(true);
				this.setResponseStatusCode(response.getStatusLine().getStatusCode());
				this.setErrorDesc(response.getStatusLine().getReasonPhrase());
			}
		}
	}
	
	//request执行完成
	public void requestFinished(HttpResponse response) throws RuntimeException, Exception
	{
		responseStr = EntityUtils.toString(response.getEntity());
		
		JSONObject json = new JSONObject(responseStr);
		JSONObject data = json.getJSONObject("Data");
		JSONObject status = json.getJSONObject("Status");  //保存状�?信息的json object
		
		String id = status.getString("Id");
		String memo = status.getString("Memo");
		
		if(Integer.valueOf(id) == 0)
		{
			this.setResponseStr(data.toString());//responseStr存放的是整个data这个json
			this.setHasError(false);  //没有错误就置为false
			if(params.get("M").equals("User.LogOn")) 
			{
			    String session = data.getString("Session");			    
			    this.setSession(session);
			}
			Log.i("responseStr", responseStr);
			this.setResponseStatusCode(Integer.valueOf(id));
		}else
		{
			this.setHasError(true);
			this.setResponseStatusCode(Integer.valueOf(id));
			this.setErrorDesc(memo);
		}
		
		this.params.clear();//每次执行完请求之后都清空参数map
	}
	
	//�?��用户账号
	public void activeUser(String num, String name, String password) throws Exception
	{
	    name = URLEncoder.encode(name, "UTF-8");
		params.put("M", "User.Active");
		params.put("NO", num);
		params.put("Name", name);
		params.put("Password", password);
		this.executeRequest();
		
	}
	
	//验证用户登录
	public void login(String name, String password) throws Exception
	{
		params.put("M", "User.LogOn");
		params.put("NO", name);
		params.put("Password", password);
		this.executeRequest();
	}
	
	//修改用户密码
	public void updatePassword(String oldPassword, String newPassword, String session, String uid) throws Exception
	{
		params.put("M", "User.Update.Password");
		params.put("Old", oldPassword);
		params.put("New", newPassword);
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//用户重置密码
	public void resetPasswordWithUserName(String name, String number) throws Exception
	{
		name = URLEncoder.encode(name, "UTF-8");
		
		params.put("M", "User.Reset.Password");
		params.put("Name", name);
		params.put("NO", number);
		
		this.executeRequest();
	}
	//用户登出
	public void logout() throws Exception
	{
		params.put("M", "User.LogOut");
		this.executeRequest();
	}
	
	//更新用户头像
	public void updateUserAvatar(String bitmapSrc, String session, String uid)
	{
		params.put("M", "User.Update.Avatar");
		params.put("S", session);
		params.put("U", uid);
		
		HttpPost httpPost = new HttpPost(this.buildURL());
		
		FileBody fileBody = new FileBody(new File(bitmapSrc));
		try
		{
			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart("Image", fileBody);
			
			httpPost.setEntity(multipartEntity);
			
			HttpResponse response = httpClient.execute(httpPost);
			
			switch(response.getStatusLine().getStatusCode())
			{
				case 200:
				{
					this.requestFinished(response);
				}
					break;
				default:
				{
					//出现网络错误
					this.setHasError(true);
					this.setResponseStatusCode(response.getStatusLine().getStatusCode());
					this.setErrorDesc(response.getStatusLine().getReasonPhrase());
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
    //更新用户资料
	public void updateUser(User user, String session) throws Exception
	{
		params.put("M", "User.Update");
		params.put("S", session);
		params.put("U", user.getUID());
		
		JSONObject json = new JSONObject();
        json.put("DisplayName", user.getDisplayName());
        json.put("Email", user.getEmail());
        json.put("SinaWeibo", user.getSinaWeibo());
        json.put("Phone", user.getPhone());
        json.put("QQ", user.getQQ());		          

		JSONObject userJson = new JSONObject();
		userJson.put("User", json);
		
        HttpPost httpPost = new HttpPost(this.buildURL());
		
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        		
		StringBody body = new StringBody(userJson.toString(), "appliction/json" ,Charset.forName("UTF-8"));		//StringBody body = new StringBody(userJson.toString())
		multipartEntity.addPart("User", body);
		
		httpPost.setEntity(multipartEntity);
		
		HttpResponse response = httpClient.execute(httpPost);
		
		switch(response.getStatusLine().getStatusCode())
		{
			case 200:
			{
				this.requestFinished(response);
			}
				break;
			default:
			{
				//出现网络错误
				this.setHasError(true);
				this.setResponseStatusCode(response.getStatusLine().getStatusCode());
				this.setErrorDesc(response.getStatusLine().getReasonPhrase());
			}
		}
	}
	
	//获取用户信息
	public void getUser(String session, String uid) throws Exception
	{
		params.put("M", "User.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//查找指定用户
	public void findUser(String name, String num) throws Exception
	{
		params.put("M", "User.Find");
		params.put("NO", num);
		params.put("Name", name);
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//济人部分
	//获取�?��济人,这里有个sort参数，这里就不要了，默认排序就好
	public void getPerson(int page) throws Exception
	{
		params.put("M", "People.Get");
		if(page <= 0)
			page = 1;
		String pageStr = String.valueOf(page);
		params.put("P", pageStr);
		this.executeRequest();
	}
	
	//获取�?���?��济人
	public void getLatestPerson(String session, String uid) throws Exception
	{
		params.put("M", "People.GetLatest");
		
		if(session != null)
		{
			params.put("S", session);
		}
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		this.executeRequest();
	}
	
	//查看济人
	public void readPerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.Read");
		params.put("Id", String.valueOf(id));
		
		if(session != null)
		{
			params.put("S", session);
		}
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		this.executeRequest();
	}
	
	//收藏济人
	public void favoritePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.Favorite");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//取消收藏
	public void unFavoritePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.UnFavorite");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//喜欢济人
	public void likePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.Like");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//取消喜欢
	public void unLikePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.UnLike");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//countdown部分
	//获取�?��倒计�?
	public void getAllCountDown() throws Exception
	{
		params.put("M", "CountDowns.Get");
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//获取某个倒计�?
	public void getCountDown(int id) throws Exception
	{
		params.put("M", "CountDown.Get");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}

	//版本更新部分
	//�?��版本
	public void checkVersion() throws Exception
	{
		params.put("M", "System.Version");
		this.executeRequest();
	}
	
	
	//济事部分
	//获取�?��济事
	public void getAllAchievement() throws Exception
	{
		params.put("M", "Achievements.Get");
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//标识为已完成
	public void recordAchievement(int id) throws Exception
	{
		params.put("M", "Achievement.Record");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//标识为未完成
	public void unRecordAchievement(int id) throws Exception
	{
		params.put("M", "Achievement.UnRecord");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//获取校园新闻列表
	public void getNewsList(int page) throws Exception
	{
		params.put("M", "News.GetList");
		if(page <= 0)
			page = 1;
		String pageStr = String.valueOf(page);
		params.put("P", pageStr);
		this.executeRequest();
	}
	
	//获取某个频道的活动列�?
	public void getActivitiesWithChannelIds(String channelId, int sortType, int page, 
			String session, String uid, int expire) throws Exception
	{
		params.put("M", "Activities.Get");
		
		if(session != null)
		{
			params.put("S", session);
		}
		
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		channelId = URLEncoder.encode(channelId, "UTF-8");
		params.put("Channel_Ids", channelId);
		
		params.put("Expire", String.valueOf(expire));
		
		if(page <= 0)
			page = 1;
		String pageStr = String.valueOf(page);
		params.put("P", pageStr);
		
		if(sortType == 1)
		{
			String sort = URLEncoder.encode(SORTTYPE_BEGIN, "UTF-8");
			params.put("Sort", sort);
		}else if(sortType == 3)
		{
			String sort = URLEncoder.encode(SORTTYPE_LIKEDESC, "UTF-8");
			sort += "%20DESC";
			params.put("Sort", sort);
		}
		else if(sortType==4){
			String sort=URLEncoder.encode(SORTTYPE_PUBLISHDESC, "UTF-8");
			sort+="%20DESC";
			params.put("Sort", sort);
		}
		
		this.executeRequest();
	}
	
	//喜欢某个活动
	public void likeActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Like");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//添加活动到日程列�?
	public void scheduleActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Schedule");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//关注某个活动
	public void favoriteActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Favorite");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//取消喜欢某个活动
	public void unlikeActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnLike");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//从日程删除某个活�?
	public void unscheduleActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnSchedule");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//取消关注某个活动
	public void unfavoriteActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnFavorite");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//获取日程�?
	public void getSchedule(String session, String uid, String begin, String end) throws Exception
	{
		params.put("M", "Schedule.Get");
		params.put("S", session);
		params.put("U", uid);
		params.put("Begin", begin);
		params.put("End", end);
		this.executeRequest();
	}
	
	//获取课程�?
	public void getCourse(String session, String uid) throws Exception
	{
		params.put("M", "TimeTable.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//喜欢某个课程
	public void likeCourse(String courseId, String session, String uid) throws Exception
	{
		params.put("M", "Course.Like");
		params.put("S", session);
		params.put("U", uid);
		params.put("Id", courseId);
		this.executeRequest();
	}
	
	//取消喜欢某个课程
	public void unlikeCourse(String courseId, String session, String uid) throws Exception
	{
		params.put("M", "Course.UnLike");
		params.put("Id", courseId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//获取收藏夹内�?
	public void getFavoriteList(String session, String uid) throws Exception
	{
		params.put("M", "Favorite.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//阅读某一条新�?
	public void readNews(String newsId) throws Exception
	{
		params.put("M", "News.Read");
		params.put("Id", newsId);
		this.executeRequest();
	}
	
	//资讯部分
	//获取资讯列表
	public void getInformationsList(int sortType, String session, String uid) throws Exception
	{
		params.put("M", "Information.GetList");
		
		if(session != null)
		{
			params.put("S", session);
		}
		
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		if(sortType == 2)
		{
			
		}
		
		this.executeRequest();
	}
	
	//获取资讯明细
	public void getInformation(String informationId, String session, String uid) throws Exception
	{
		params.put("M", "Information.Get");
		
		if(session != null)
		{
			params.put("S", session);
		}
		
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		params.put("Id", informationId);
		
		this.executeRequest();
	}
	
	//阅读资讯
	public void readInformation(String informationId, String session, String uid) throws Exception
	{
		params.put("M", "Information.Read");
		
		if(session != null)
		{
			params.put("S", session);
		}
		
		if(uid != null)
		{
			params.put("U", uid);
		}
		
		params.put("Id", informationId);
		
		this.executeRequest();
	}
	
	//some set/get methods
	public String getErrorDesc() 
	{
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) 
	{
		this.errorDesc = errorDesc;
	}
	public boolean isHasError() 
	{
		return hasError;
	}
	public void setHasError(boolean hasError) 
	{
		this.hasError = hasError;
	}
	public int getResponseStatusCode() 
	{
		return responseStatusCode;
	}
	public void setResponseStatusCode(int responseStatusCode) 
	{
		this.responseStatusCode = responseStatusCode;
	}
	public String getResponseStr() 
	{
		return responseStr;
	}
	public void setResponseStr(String responseStr) 
	{
		this.responseStr = responseStr;
	}
	public String getSession() 
	{
		return session;
	}
	public void setSession(String session) 
	{
		this.session = session;
	}

	public boolean isSessionRequired() {
		return sessionRequired;
	}

	public void setSessionRequired(boolean sessionRequired) {
		this.sessionRequired = sessionRequired;
	}

	public boolean isUidRequired() {
		return uidRequired;
	}

	public void setUidRequired(boolean uidRequired) {
		this.uidRequired = uidRequired;
	}
}
