package com.wetongji_android.data;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;

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

	public String getS1_WeekDay() {
		return S1_WeekDay;
	}

	public void setS1_WeekDay(String s1_WeekDay) {
		S1_WeekDay = s1_WeekDay;
	}

	public String getS2_WeekDay() {
		return S2_WeekDay;
	}

	public void setS2_WeekDay(String s2_WeekDay) {
		S2_WeekDay = s2_WeekDay;
	}

	public String getS1_Begin() {
		return S1_Begin;
	}

	public void setS1_Begin(String s1_Begin) {
		S1_Begin = s1_Begin;
	}

	public String getS1_End() {
		return S1_End;
	}

	public void setS1_End(String s1_End) {
		S1_End = s1_End;
	}

	public String getS1_TimeType() {
		return S1_TimeType;
	}

	public void setS1_TimeType(String s1_TimeType) {
		S1_TimeType = s1_TimeType;
	}

	public String getS1_Location() {
		return S1_Location;
	}

	public void setS1_Location(String s1_Location) {
		S1_Location = s1_Location;
	}

	public String getS2_Begin() {
		return S2_Begin;
	}

	public void setS2_Begin(String s2_Begin) {
		S2_Begin = s2_Begin;
	}

	public String getS2_End() {
		return S2_End;
	}

	public void setS2_End(String s2_End) {
		S2_End = s2_End;
	}

	public String getS2_TimeType() {
		return S2_TimeType;
	}

	public void setS2_TimeType(String s2_TimeType) {
		S2_TimeType = s2_TimeType;
	}

	public String getS2_Location() {
		return S2_Location;
	}

	public void setS2_Location(String s2_Location) {
		S2_Location = s2_Location;
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

	@DatabaseField
	private String S1_Begin;
	@DatabaseField
	private String S1_End;
	@DatabaseField
	private String S1_TimeType;
	@DatabaseField
	private String S1_Location;
	@DatabaseField
	private String S1_WeekDay;

	@DatabaseField
	private String S2_Begin;
	@DatabaseField
	private String S2_End;
	@DatabaseField
	private String S2_TimeType;
	@DatabaseField
	private String S2_Location;
	@DatabaseField
	private String S2_WeekDay;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;

	@DatabaseField
	private int FriendsCount;

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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(NO);
		dest.writeInt(Hours);
		dest.writeFloat(Point);
		dest.writeString(Teacher);
		dest.writeString(Required);
		dest.writeString(S1_Begin);
		dest.writeString(S1_End);
		dest.writeString(S1_TimeType);
		dest.writeString(S1_Location);
		dest.writeString(S1_WeekDay);
		dest.writeString(S2_Begin);
		dest.writeString(S2_End);
		dest.writeString(S2_TimeType);
		dest.writeString(S2_Location);
		dest.writeString(S2_WeekDay);
		dest.writeInt(Like);
		dest.writeInt(FriendsCount);
		dest.writeByte((byte)(CanLike?1:0));
	}

	protected Course(Parcel source) {
		super(source);
		NO = source.readString();
		Hours = source.readInt();
		Point = source.readFloat();
		Teacher = source.readString();
		Required = source.readString();

		S1_Begin = source.readString();
		S1_End = source.readString();
		S1_TimeType = source.readString();
		S1_Location = source.readString();
		S1_WeekDay = source.readString();
		S2_Begin = source.readString();
		S2_End = source.readString();
		S2_TimeType = source.readString();
		S2_Location = source.readString();
		S2_WeekDay = source.readString();
		Like = source.readInt();
		FriendsCount = source.readInt();
		CanLike=source.readByte()==1;
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
