package com.wetongji_android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Course extends Event implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 274875110056856277L;
	
	@DatabaseField
	private String NO;
	@DatabaseField
	private int Hours;
	@DatabaseField
	private float Point;
	@DatabaseField
	private String Teacher;
	@DatabaseField
	private String Required;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;
	@DatabaseField
	private boolean CanSchedule;
	@DatabaseField
	private boolean IsAudit;
	@DatabaseField
	private String UNO;
	@DatabaseField
	private int FriendsCount;
	@DatabaseField
	private String WeekDay;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	ArrayList<Sections> Sections;
	
	public Course() {
		super();
	}

	public Course(int id, Date begin, Date end, String title, String location, String nO, int hours,
			float point, String teacher, String required) {
		super(id, begin, end, title, location);
		NO = nO;
		Hours = hours;
		Point = point;
		Teacher = teacher;
		Required = required;
	}

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}

	public int getHours() {
		return Hours;
	}

	public void setHours(int hours) {
		Hours = hours;
	}

	public float getPoint() {
		return Point;
	}

	public void setPoint(float point) {
		Point = point;
	}

	public String getTeacher() {
		return Teacher;
	}

	public void setTeacher(String teacher) {
		Teacher = teacher;
	}

	public String isRequired() {
		return Required;
	}

	public void setRequired(String required) {
		Required = required;
	}

	public int getLike() {
		return Like;
	}

	public void setLike(int like) {
		Like = like;
	}

	public int getFriendsCount() {
		return FriendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		FriendsCount = friendsCount;
	}

	public boolean isCanLike() {
		return CanLike;
	}

	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}

	public boolean isCanSchedule() {
		return CanSchedule;
	}

	public void setCanSchedule(boolean canSchedule) {
		CanSchedule = canSchedule;
	}

	public boolean isIsAudit() {
		return IsAudit;
	}

	public void setIsAudit(boolean isAudit) {
		IsAudit = isAudit;
	}

	public String getUNO() {
		return UNO;
	}

	public void setUNO(String uNO) {
		UNO = uNO;
	}

	public String getWeekDay() {
		return WeekDay;
	}

	public void setWeekDay(String weekDay) {
		WeekDay = weekDay;
	}

	public ArrayList<Sections> getSections() {
		return Sections;
	}

	public void setSections(ArrayList<Sections> sections) {
		Sections = sections;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(NO);
		dest.writeInt(Hours);
		dest.writeFloat(Point);
		dest.writeString(Teacher);
		dest.writeString(Required);
		dest.writeInt(Like);
		dest.writeInt(FriendsCount);
		dest.writeByte((byte) (CanLike ? 1 : 0));
		dest.writeByte((byte) (CanSchedule ? 1 : 0));
		dest.writeByte((byte) (IsAudit ? 1 : 0));
		dest.writeString(UNO);
		dest.writeString(WeekDay);
		dest.writeList(Sections);
	}

	@SuppressWarnings("unchecked")
	protected Course(Parcel source) {
		super(source);
		NO = source.readString();
		Hours = source.readInt();
		Point = source.readFloat();
		Teacher = source.readString();
		Required = source.readString();
		Like = source.readInt();
		FriendsCount = source.readInt();
		CanLike = source.readByte() == 1;
		CanSchedule = source.readByte() == 1;
		IsAudit = source.readByte() == 1;
		UNO=source.readString();
		WeekDay = source.readString();
		Sections = source.readArrayList(ArrayList.class.getClassLoader());
	}

	public static final Creator<Course> CREATOR = new Creator<Course>() {

		@Override
		public Course[] newArray(int size) {
			return new Course[size];
		}

		@Override
		public Course createFromParcel(Parcel source) {
			return new Course(source);
		}
	};

}
