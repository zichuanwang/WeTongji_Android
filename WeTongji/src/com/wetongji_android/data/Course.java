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
	
	public Course() {
		super();
	}

	public Course(int id, Date begin, Date end, String title, String location, 
			String nO, int hours, float point, String teacher, String required) {
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
	}
	
	protected Course(Parcel source){
		super(source);
		NO=source.readString();
		Hours=source.readInt();
		Point=source.readFloat();
		Teacher=source.readString();
		Required=source.readString();
	}
	
	public static final Creator<Course> CREATOR=new Creator<Course>() {
		
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
