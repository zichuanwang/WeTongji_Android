package com.wetongji_android.data;

import java.io.Serializable;

public class Sections implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Location;
	private String WeekDay;
	private String WeekType;
	private int SectionEnd;
	private String UNO;
	private int SectionStart;
	
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getWeekDay() {
		return WeekDay;
	}
	public void setWeekDay(String weekDay) {
		WeekDay = weekDay;
	}
	public String getWeekType() {
		return WeekType;
	}
	public void setWeekType(String weekType) {
		WeekType = weekType;
	}
	public int getSectionEnd() {
		return SectionEnd;
	}
	public void setSectionEnd(int sectionEnd) {
		SectionEnd = sectionEnd;
	}
	public String getUNO() {
		return UNO;
	}
	public void setUNO(String uNO) {
		UNO = uNO;
	}
	public int getSectionStart() {
		return SectionStart;
	}
	public void setSectionStart(int sectionStart) {
		SectionStart = sectionStart;
	}
}
