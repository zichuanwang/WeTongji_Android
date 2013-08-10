package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Notification;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.date.DateParser;

public class NotificationFactory 
{
	private int nextPage;
	
	public NotificationFactory()
	{
		
	}
	
	public List<Notification> createObjects(String jsonStr)
	{
		List<Notification> results = new ArrayList<Notification>();
		int nextPage = 0;
		
		try 
		{
			JSONObject data = new JSONObject(jsonStr);
			nextPage = data.getInt("NextPager");
			setNextPage(nextPage);
			JSONArray notifications = data.getJSONArray("Notifications");
			for(int i = 0; i < notifications.length(); i++)
			{
				results.add(createObject(notifications.getJSONObject(i)));
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}
	
	private Notification createObject(JSONObject json)
	{
		Notification notification = new Notification();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		try 
		{
			notification.setId(json.getInt("Id"));
			notification.setDescription(json.getString("Description"));
			notification.setTitle(json.getString("Title"));
			notification.setRead(json.getBoolean("UnRead"));
			String type = json.getString("SourceType");
			notification.setSourceId(json.getInt("SourceId"));
			JSONObject detail = json.getJSONObject("SourceDetails");
			notification.setConfirmed(notification.getTitle().contains("已经接受"));
			
			if (type.equals("CourseInvite")) {
				notification.setType(1);
				Course course = gson.fromJson(detail.getString("CourseDetails"), Course.class);
				notification.setContent(course);
			} else if (type.equals("FriendInvite")) {
				notification.setType(2);
				User user;
				if (!notification.isConfirmed()) {
					user = gson.fromJson(detail.getString("UserDetails"), User.class);
				} else {
					user = gson.fromJson(detail.getString("ToUserDetails"), User.class);
				}
				notification.setContent(user);
			} else {
				notification.setType(3);
				Activity activity = gson.fromJson(detail.getString("ActivityDetails"), Activity.class);
				notification.setContent(activity);
			}
			notification.setSentAt(DateParser.parseDateAndTime(detail.getString("SentAt")));
			notification
				.setAcceptedAt(detail.getString("AcceptedAt").equals("null") ? null
						: DateParser.parseDateAndTime(detail
								.getString("AcceptedAt")));
			
			notification.setAccepted(!detail.getString("AcceptedAt").equals("null"));
			notification.setRejectedAt(DateParser.parseDateAndTime(detail.getString("RejectedAt")));
			notification.setFrom(detail.getString("From"));
			JSONObject user = detail.getJSONObject("UserDetails");
			notification.setThumbnail(user.getString("Avatar"));
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return notification;
	}

	public int getNextPage() 
	{
		return nextPage;
	}

	public void setNextPage(int nextPage) 
	{
		this.nextPage = nextPage;
	}
}
