package com.wetongji_android.util.data.user;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.User;
import com.wetongji_android.util.data.DbListLoader;

public class UserLoader extends DbListLoader<User, String> {

	public UserLoader(Context context) {
		super(context, User.class);
	}

	@Override
	public List<User> loadInBackground() {
		try {
			return mDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
