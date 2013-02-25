package com.wetongji.data;

import com.j256.ormlite.field.DatabaseField;

public abstract class News {
	@DatabaseField(id=true)
	int Id;
	@DatabaseField
	String Title;
	@DatabaseField
	String Context;
	@DatabaseField
	int Read;
	@DatabaseField
	String Image;
	@DatabaseField
	String CreatedAt;
	@DatabaseField
	int Like;
	@DatabaseField
	boolean CanLike;
	@DatabaseField
	String Summary;
	@DatabaseField
	String Source;
	
	public News() {
		super();
	}
	
	public News(int id, String title, String context, int read, String image,
			String createdAt, int like, boolean canLike, String summary,
			String source) {
		super();
		Id = id;
		Title = title;
		Context = context;
		Read = read;
		Image = image;
		CreatedAt = createdAt;
		Like = like;
		CanLike = canLike;
		Summary = summary;
		Source = source;
	}
	
	public int getId() {
		return Id;
	}
	
	public void setId(int id) {
		Id = id;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String title) {
		Title = title;
	}
	
	public String getContext() {
		return Context;
	}
	
	public void setContext(String context) {
		Context = context;
	}
	
	public int getRead() {
		return Read;
	}
	
	public void setRead(int read) {
		Read = read;
	}
	
	public String getImage() {
		return Image;
	}
	
	public void setImage(String image) {
		Image = image;
	}
	
	public String getCreatedAt() {
		return CreatedAt;
	}
	
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	
	public int getLike() {
		return Like;
	}
	
	public void setLike(int like) {
		Like = like;
	}
	
	public boolean isCanLike() {
		return CanLike;
	}
	
	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}
	
	public String getSummary() {
		return Summary;
	}
	
	public void setSummary(String summary) {
		Summary = summary;
	}
	
	public String getSource() {
		return Source;
	}
	
	public void setSource(String source) {
		Source = source;
	}
	
}
