package com.wetongji_android.data;

import java.util.Date;

public class Comment {
	
	private int Id;
	private String Body;
	private Date PublishedAt;
	private String UserName;
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public String getBody() {
		return Body;
	}
	
	public void setBody(String body) {
		Body = body;
	}
	
	public Date getPublishedAt() {
		return PublishedAt;
	}
	
	public void setPublishedAt(Date publishedAt) {
		PublishedAt = publishedAt;
	}
	
	public String getUserName() {
		return UserName;
	}
	
	public void setUserName(String userName) {
		UserName = userName;
	}

}
