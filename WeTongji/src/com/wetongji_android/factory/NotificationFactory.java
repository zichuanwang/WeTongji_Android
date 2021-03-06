package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Notification;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;

public class NotificationFactory extends BaseFactory<Notification, Integer>{
	private int nextPage;

	public NotificationFactory(Fragment fragment) {
		super(fragment, Notification.class, WTApplication.EVENTS_SAVER);
	}

    public static List<Notification> parseObjects(String jsonStr) {
        List<Notification> results = new ArrayList<Notification>();
        int nextPage = 0;
        try {
            JSONObject data = new JSONObject(jsonStr);
            nextPage = data.getInt("NextPager");
            //setNextPage(nextPage);
            JSONArray notifications = data.getJSONArray("Notifications");
            for (int i = 0; i < notifications.length(); i++) {
                results.add(createObject(notifications.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

	public List<Notification> createObjects(String jsonStr) {
        List<Notification> results = parseObjects(jsonStr);
        list.clear();
        List<Activity> activityList = new ArrayList<Activity>();
        List<User> userList = new ArrayList<User>();
        List<Course> courseList = new ArrayList<Course>();

        for (int i = 0; i < results.size(); i++) {
            Notification notification = results.get(i);
            if (!notification.isIsConfirmed()) {
                int type = notification.getType();
                if (type == Notification.TYPE_COURSE_INVITE) {
                    courseList.add(notification.getCourse());
                } else if (type == Notification.TYPE_FRIEND_INVITE) {
                    userList.add(notification.getUser());
                } else if (type == Notification.TYPE_ACTIVITY_INVITE) {
                    activityList.add(notification.getActivity());
                }
                list.add(notification);
            }

        }

		Bundle args = new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, false);
		fragment.getLoaderManager().initLoader(WTApplication.NOTIFICCATOINS_SAVER, args, this).forceLoad();

        if (!userList.isEmpty()) {
            UserFactory userFactory = new UserFactory(fragment);
            userFactory.setList(userList);
            fragment.getLoaderManager().initLoader(WTApplication.USER_SAVER, args, userFactory).forceLoad();
        }
        if (!courseList.isEmpty()) {
            CourseFactory courseFactory = new CourseFactory(fragment);
            courseFactory.setList(courseList);
            fragment.getLoaderManager().initLoader(WTApplication.COURSES_SAVER, args, courseFactory).forceLoad();
        }
        if (!activityList.isEmpty()) {
            ActivityFactory activityFactory = new ActivityFactory(fragment);
            activityFactory.setList(activityList);
            fragment.getLoaderManager().initLoader(WTApplication.ACTIVITIES_SAVER, args, activityFactory).forceLoad();
        }
		return results;
	}

	public static Notification createObject(JSONObject json) {
		Notification notification = new Notification();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		try {
			notification.setId(json.getInt("Id"));
			notification.setDescription(json.getString("Description"));
			notification.setTitle(json.getString("Title"));
			notification.setRead(json.getBoolean("UnRead"));
			String type = json.getString("SourceType");
			notification.setSourceId(json.getInt("SourceId"));
			JSONObject detail = json.getJSONObject("SourceDetails");
			notification.setIsConfirmed(notification.getTitle().contains("已经接受"));

			if (type.equals("CourseInvite")) {
				notification.setType(Notification.TYPE_COURSE_INVITE);
				Course course = gson.fromJson(
						detail.getString("CourseDetails"), Course.class);
				notification.setCourse(course);
			} else if (type.equals("FriendInvite")) {
				notification.setType(Notification.TYPE_FRIEND_INVITE);
				User user;
				if (!notification.isIsConfirmed()) {
					user = gson.fromJson(detail.getString("UserDetails"),
							User.class);
				} else {
					user = gson.fromJson(detail.getString("ToUserDetails"),
							User.class);
				}
				notification.setUser(user);
			} else {
				notification.setType(Notification.TYPE_ACTIVITY_INVITE);
				Activity activity = gson.fromJson(
						detail.getString("ActivityDetails"), Activity.class);
				notification.setActivity(activity);
			}
			notification.setSentAt(detail.getString("SentAt").equals(
					"null") ? null : DateParser.parseDateAndTime(detail
					.getString("SentAt")));
			notification.setAcceptedAt(detail.getString("AcceptedAt").equals(
					"null") ? null : DateParser.parseDateAndTime(detail
					.getString("AcceptedAt")));

			notification.setAccepted(!detail.getString("AcceptedAt").equals(
					"null"));
			notification.setRejectedAt(DateParser.parseDateAndTime(detail
					.getString("RejectedAt")));
			notification.setFrom(detail.getString("From"));
			
			JSONObject user;
			if (notification.isIsConfirmed()) {
				user = detail.getJSONObject("ToUserDetails");
			} else {
				user = detail.getJSONObject("UserDetails");
			}
			notification.setThumbnail(user.getString("Avatar"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return notification;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
