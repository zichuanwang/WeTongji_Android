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
	
	public Event() {
	}

	public Event(int id, Date begin, Date end, String title,
			String location) {
		Id = id;
		Begin = begin;
		End = end;
		Title = title;
		Location = location;
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

	@Override
	public int compareTo(Event another) {
		return this.Begin.compareTo(another.Begin);
	}
	
}
