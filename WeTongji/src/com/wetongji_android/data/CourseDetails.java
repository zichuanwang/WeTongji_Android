package com.wetongji_android.data;

import java.io.Serializable;

public class CourseDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean CanLike;
	private String Name;
	private boolean isAudit;
	private String Required;
	private String Teacher;
	private boolean CanSchedule;
	private int Hours;
	private int FriendsCount;
	private int Like;
	private String Point;
	private String NO;
	private String UNO;
	private Sections Sections;
	public boolean isCanLike() {
		return CanLike;
	}
	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public boolean isAudit() {
		return isAudit;
	}
	public void setAudit(boolean isAudit) {
		this.isAudit = isAudit;
	}
	public String getRequired() {
		return Required;
	}
	public void setRequired(String required) {
		Required = required;
	}
	public String getTeacher() {
		return Teacher;
	}
	public void setTeacher(String teacher) {
		Teacher = teacher;
	}
	public boolean isCanSchedule() {
		return CanSchedule;
	}
	public void setCanSchedule(boolean canSchedule) {
		CanSchedule = canSchedule;
	}
	public int getHours() {
		return Hours;
	}
	public void setHours(int hours) {
		Hours = hours;
	}
	public int getFriendsCount() {
		return FriendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		FriendsCount = friendsCount;
	}
	public int getLike() {
		return Like;
	}
	public void setLike(int like) {
		Like = like;
	}
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
	}
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getUNO() {
		return UNO;
	}
	public void setUNO(String uNO) {
		UNO = uNO;
	}
	public Sections getSections() {
		return Sections;
	}
	public void setSections(Sections sections) {
		Sections = sections;
	}
}