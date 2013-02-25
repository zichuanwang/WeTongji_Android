package com.wetongji.data;

import com.j256.ormlite.field.DatabaseField;

public abstract class Event {
	@DatabaseField(id=true)
	int Id;
	@DatabaseField
	String Begin;
	@DatabaseField
	String End;
	@DatabaseField
	String Title;
	@DatabaseField
	String Location;
	@DatabaseField
	String Description;
	@DatabaseField
	int Like;
	@DatabaseField
	boolean CanLike;
	
	public Event() {
		super();
	}

	public Event(int id, String begin, String end, String title,
			String location, String description, int like, boolean canLike) {
		super();
		Id = id;
		Begin = begin;
		End = end;
		Title = title;
		Location = location;
		Description = description;
		Like = like;
		CanLike = canLike;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getBegin() {
		return Begin;
	}

	public void setBegin(String begin) {
		Begin = begin;
	}

	public String getEnd() {
		return End;
	}

	public void setEnd(String end) {
		End = end;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
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
	
}
