package com.wetongji3.data;

import com.j256.ormlite.field.DatabaseField;

public class Exam extends Event {
	@DatabaseField(id=true)
	private String NO;
	@DatabaseField
	private int Hours;
	@DatabaseField
	private float Point;
	@DatabaseField
	private String Teacher;
	@DatabaseField
	private boolean Required;
	
	public Exam() {
		super();
	}

	public Exam(String nO, int hours, float point, String teacher,
			boolean required) {
		super();
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

	public boolean isRequired() {
		return Required;
	}

	public void setRequired(boolean required) {
		Required = required;
	}
	
}
