package com.wetongji_android.data;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Event implements Comparable<Event> {
	@DatabaseField(generatedId=true)
	int Id;
	@DatabaseField
	Date Begin;
	@DatabaseField
	Date End;
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
	}

	public Event(int id, Date begin, Date end, String title,
			String location, String description, int like, boolean canLike) {
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

	public Date getBegin() {
		return Begin;
	}

	public void setBegin(Date begin) {
		Begin = begin;
	}

	public Date getEnd() {
		return End;
	}

	public void setEnd(Date end) {
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

	@Override
	public int compareTo(Event another) {
		return this.Begin.compareTo(another.Begin);
	}
	
}
