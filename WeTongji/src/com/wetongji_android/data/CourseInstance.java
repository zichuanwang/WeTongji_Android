package com.wetongji_android.data;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseInstance extends Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String NO;
	private String UNO;
	private String Point;
	private int Like;
	private int FriendsCount;
	private int Hours;
	private boolean CanSchedule;
	private String Teacher;
	private String Required;
	private boolean isAudit;
	private String Name;
	private boolean CanLike;
	private ArrayList<Sections> Sections;
	
	public CourseInstance() {
		super();
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
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
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
	public int getHours() {
		return Hours;
	}
	public void setHours(int hours) {
		Hours = hours;
	}
	public boolean isCanSchedule() {
		return CanSchedule;
	}
	public void setCanSchedule(boolean canSchedule) {
		CanSchedule = canSchedule;
	}
	public String getTeacher() {
		return Teacher;
	}
	public void setTeacher(String teacher) {
		Teacher = teacher;
	}
	public String getRequired() {
		return Required;
	}
	public void setRequired(String required) {
		Required = required;
	}
	public boolean isAudit() {
		return isAudit;
	}
	public void setAudit(boolean isAudit) {
		this.isAudit = isAudit;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public boolean isCanLike() {
		return CanLike;
	}
	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}
	public ArrayList<Sections> getSections() {
		return Sections;
	}
	public void setSections(ArrayList<Sections> sections) {
		Sections = sections;
	} 
}