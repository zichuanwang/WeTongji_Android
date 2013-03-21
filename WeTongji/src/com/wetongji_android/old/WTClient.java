package com.wetongji_android.old;

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

	//����烽��ゆ�����锋俊��拷���?
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
	
	//����烽��ゆ�����锋俊��拷���?
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
			//娴ｅ�������MD5�跨��婚��芥����瀚�essageDigest�跨��婚��芥����瀚�  
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");   
			mdTemp.update(strTemp);   
			byte[] md = mdTemp.digest();   
			int j = md.length;   
			char str[] = new char[j * 2];   
			int k = 0;   
			for (int i = 0; i < j; i++) {   
				byte b = md[i];   
				//System.out.println((int)b);   
				//�跨��婚�����跨��婚��芥�����?�int)b�跨��婚��芥����瀚归�宀�����濡�涵����跨��婚�锟�?
				str[k++] = hexDigits[b >> 4 & 0xf];   
				str[k++] = hexDigits[b & 0xf];   
			}   
			return new String(str);   
		} 
		catch (Exception e) {
			return null;
		} 
	}
	
	//����洪��ゆ�����烽��ゆ�����烽��ゆ��匡拷
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
	
	//����洪��ゆ��殿���RL�匡�?
	public String buildURL()
	{
		params.put("D", "android1.0.0");
		params.put("V", "2.0");
		String queryStr = queryString();
		String hashStr = hashQueryString(queryStr);
		String url = API_DOMAIN + "?" + queryStr + "&H=" + hashStr;
		
		Log.i("queryURL", url);
		
		return url;
	}

	//������request
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
				//���娅ｉ��������烽��ゆ����?
				this.setHasError(true);
				this.setResponseStatusCode(response.getStatusLine().getStatusCode());
				this.setErrorDesc(response.getStatusLine().getReasonPhrase());
			}
		}
	}
	
	//request����������烽�锟�	
	public void requestFinished(HttpResponse response) throws RuntimeException, Exception
	{
		responseStr = EntityUtils.toString(response.getEntity());
		
		JSONObject json = new JSONObject(responseStr);
		JSONObject data = json.getJSONObject("Data");
		JSONObject status = json.getJSONObject("Status");  //濞ｅ��烽��ゆ����瀚�濞ｅ�����ゆ���氮on object
		
		String id = status.getString("Id");
		String memo = status.getString("Memo");
		
		if(Integer.valueOf(id) == 0)
		{
			this.setResponseStr(data.toString());//responseStr��冻�烽��ゆ�����烽�����叉�ata����烽�娓�on
			this.setHasError(false);  //婵�备����ゆ�����烽�蹇�含�ゅ�绋��绲�se
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
		
		this.params.clear();//婵���烽��ゆ�瑜����拷����锋慨�告�����烽��ゆ�婵���烽��垫������ap
	}
	
	//�匡拷�跨��婚��ゆ���讣�烽��舵���拷
	public void activeUser(String num, String name, String password) throws Exception
	{
	    name = URLEncoder.encode(name, "UTF-8");
		params.put("M", "User.Active");
		params.put("NO", num);
		params.put("Name", name);
		params.put("Password", password);
		this.executeRequest();
		
	}
	
	//濡ょ��烽��ゆ���讣�烽�������?
	public void login(String name, String password) throws Exception
	{
		params.put("M", "User.LogOn");
		params.put("NO", name);
		params.put("Password", password);
		this.executeRequest();
	}
	
	//濞ｅ��烽��ゆ���讣�烽�纰�����?
	public void updatePassword(String oldPassword, String newPassword, String session, String uid) throws Exception
	{
		params.put("M", "User.Update.Password");
		params.put("Old", oldPassword);
		params.put("New", newPassword);
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//�������ゆ�������纰�����?
	public void resetPasswordWithUserName(String name, String number) throws Exception
	{
		name = URLEncoder.encode(name, "UTF-8");
		
		params.put("M", "User.Reset.Password");
		params.put("Name", name);
		params.put("NO", number);
		
		this.executeRequest();
	}
	//�������ゆ������
	public void logout() throws Exception
	{
		params.put("M", "User.LogOut");
		this.executeRequest();
	}
	
	//����ㄩ��ゆ���讣�峰��垫����?
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
					//���娅ｉ��������烽��ゆ����?
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
	
    //����ㄩ��ゆ���讣�烽���烽�锟�?
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
				//���娅ｉ��������烽��ゆ����?
				this.setHasError(true);
				this.setResponseStatusCode(response.getStatusLine().getStatusCode());
				this.setErrorDesc(response.getStatusLine().getReasonPhrase());
			}
		}
	}
	
	//����查��ゆ���讣�峰ǎ�ｅ���拷
	public void getUser(String session, String uid) throws Exception
	{
		params.put("M", "User.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����查��ゆ�����烽�濮�����?
	public void findUser(String name, String num) throws Exception
	{
		params.put("M", "User.Find");
		params.put("NO", num);
		params.put("Name", name);
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//婵�揪�峰Ч澶�������?
	//����查�浠��?�跨��婚�宄懊归��ョ�?����烽��ゆ������ort����烽�浠������烽��ゆ�濮��绋���ゆ�濞存��烽��ヮ�����烽��ゆ����浜搁����
	public void getPerson(int page) throws Exception
	{
		params.put("M", "People.Get");
		if(page <= 0)
			page = 1;
		String pageStr = String.valueOf(page);
		params.put("P", pageStr);
		this.executeRequest();
	}
	
	//����查�浠��?�跨��婚��芥�?�跨��婚�宄懊归��ョ�?
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
	
	//����抽���归��ョ�?
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
	
	//����奸���归��ョ�?
	public void favoritePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.Favorite");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//����烽��ゆ���府��
	public void unFavoritePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.UnFavorite");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//����烽���归��ョ�?
	public void likePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.Like");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//����烽��ゆ������
	public void unLikePerson(int id, String session, String uid) throws Exception
	{
		params.put("M", "People.UnLike");
		params.put("Id", String.valueOf(id));
		params.put("S", session);
		params.put("U", uid);
		
		this.executeRequest();
	}
	
	//countdown������锟�	//����查�浠��?�跨��婚��ゆ�����烽�锟�?
	public void getAllCountDown() throws Exception
	{
		params.put("M", "CountDowns.Get");
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//����查��ゆ��������ゆ������?
	public void getCountDown(int id) throws Exception
	{
		params.put("M", "CountDown.Get");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}

	//����烽��ゆ�����烽�濮�����?
	//�匡拷�跨��婚��ゆ������?
	public void checkVersion() throws Exception
	{
		params.put("M", "System.Version");
		this.executeRequest();
	}
	
	
	//婵�揪�烽��ゆ������
	//����查�浠��?�跨��婚�宄懊归��ゆ�?
	public void getAllAchievement() throws Exception
	{
		params.put("M", "Achievements.Get");
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//����烽��ョ��������告����?
	public void recordAchievement(int id) throws Exception
	{
		params.put("M", "Achievement.Record");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//����烽��ョ�����烽��告����?
	public void unRecordAchievement(int id) throws Exception
	{
		params.put("M", "Achievement.UnRecord");
		params.put("Id", String.valueOf(id));
		this.setSessionRequired(true);
		this.setUidRequired(true);
		this.executeRequest();
	}
	
	//����查��ゆ��ョ��烽��������烽�濮��?
	public void getNewsList(int page) throws Exception
	{
		params.put("M", "News.GetList");
		if(page <= 0)
			page = 1;
		String pageStr = String.valueOf(page);
		params.put("P", pageStr);
		this.executeRequest();
	}
	
	//����查��ゆ������Λ甯������烽��ャ�������浠��?
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
	
	//����烽��ゆ��������额����?
	public void likeActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Like");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//婵���ч���虹�璇ф����澧���������烽�锟�
	public void scheduleActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Schedule");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//���娅������������额����?
	public void favoriteActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.Favorite");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����烽��ゆ�����烽��ゆ��叉�煤�����?
	public void unlikeActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnLike");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//濞寸��烽��������烽�濮�����绋���ャ��匡拷
	public void unscheduleActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnSchedule");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����烽��ゆ��������ゆ��叉�煤�����?
	public void unfavoriteActivity(String activityId, String session, String uid) throws Exception
	{
		params.put("M", "Activity.UnFavorite");
		params.put("Id", activityId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����查��ゆ������匡拷
	public void getSchedule(String session, String uid, String begin, String end) throws Exception
	{
		params.put("M", "Schedule.Get");
		params.put("S", session);
		params.put("U", uid);
		params.put("Begin", begin);
		params.put("End", end);
		this.executeRequest();
	}
	
	//����查���������匡拷
	public void getCourse(String session, String uid) throws Exception
	{
		params.put("M", "TimeTable.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����烽��ゆ��������у⒔��拷
	public void likeCourse(String courseId, String session, String uid) throws Exception
	{
		params.put("M", "Course.Like");
		params.put("S", session);
		params.put("U", uid);
		params.put("Id", courseId);
		this.executeRequest();
	}
	
	//����烽��ゆ�����烽��ゆ��叉�������?
	public void unlikeCourse(String courseId, String session, String uid) throws Exception
	{
		params.put("M", "Course.UnLike");
		params.put("Id", courseId);
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����查��ゆ���府�峰���������?
	public void getFavoriteList(String session, String uid) throws Exception
	{
		params.put("M", "Favorite.Get");
		params.put("S", session);
		params.put("U", uid);
		this.executeRequest();
	}
	
	//����烽��ゆ�����烽�瑙ｅ������?
	public void readNews(String newsId) throws Exception
	{
		params.put("M", "News.Read");
		params.put("Id", newsId);
		this.executeRequest();
	}
	
	//��������烽����?
	//����查��������烽��ゆ����?
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
	
	//����查��������烽��ゆ����?
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
	
	//����烽���������
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