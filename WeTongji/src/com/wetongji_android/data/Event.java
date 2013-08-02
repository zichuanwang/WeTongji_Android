package com.wetongji_android.data;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class Event implements Comparable<Event>, Parcelable{
	@DatabaseField(generatedId=true, allowGeneratedIdInsert=true)
	private int Id;
	@DatabaseField
	private Date Begin;
	@DatabaseField
	private Date End;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Location;
	
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		if (Begin != null) {
			dest.writeLong(Begin.getTime());
		}
		if (End != null) {
			dest.writeLong(End.getTime());
		}
		dest.writeString(Title);
		dest.writeString(Location);
	}
	
	protected Event(Parcel source){
		Id=source.readInt();
		Begin=new Date(source.readLong());
		End=new Date(source.readLong());
		Title=source.readString();
		Location=source.readString();
	}
	
}
