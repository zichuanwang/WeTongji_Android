package com.wetongji_android.util.data.user;

import android.content.Context;

import com.wetongji_android.data.User;
import com.wetongji_android.util.data.DbListLoader;

public class UserLoader extends DbListLoader<User, String> {

	public UserLoader(Context context) {
		super(context, User.class);
		
	}
	
}
