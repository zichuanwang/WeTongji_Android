package com.wetongji_android.util.data.notification;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Notification;
import com.wetongji_android.data.User;
import com.wetongji_android.util.data.DbListLoader;

public class NotificationsLoader extends DbListLoader<Notification, Integer> 
{
    private Dao<Activity, Integer> activityDao;
    private Dao<Course, Integer> courseDao;
    private Dao<User, Integer> userDao;
	
	public NotificationsLoader(Context context) 
	{
		super(context, Notification.class);
        try {
            activityDao = dbHelper.getDao(Activity.class);
            courseDao = dbHelper.getDao(Course.class);
            userDao = dbHelper.getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public List<Notification> loadInBackground() 
	{
        List<Notification> notifications;
		try
		{
            notifications = mDao.queryForAll();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}

        try {
            for (Notification n : notifications) {
                if (n.getType() == Notification.TYPE_FRIEND_INVITE) {
                    userDao.refresh(n.getUser());
                } else if (n.getType() == Notification.TYPE_ACTIVITY_INVITE) {
                    activityDao.refresh(n.getActivity());
                } else if (n.getType() == Notification.TYPE_COURSE_INVITE) {
                    courseDao.refresh(n.getCourse());
                }
            }
        } catch (SQLException e) {
            return notifications;
        }


        return notifications;
	}
}