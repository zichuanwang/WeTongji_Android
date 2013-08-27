package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class UserProfile {
	@DatabaseField
	private int id;
	@DatabaseField
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
