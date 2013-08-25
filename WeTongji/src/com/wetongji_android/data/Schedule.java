package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Schedule {
	@DatabaseField(id=true)
	private int WeekNum;
	@DatabaseField
	private String JsonContent;
	
	public int getWeekNum() {
		return WeekNum;
	}
	public void setWeekNum(int weekNum) {
		WeekNum = weekNum;
	}
	public String getJsonContent() {
		return JsonContent;
	}
	public void setJsonContent(String jsonContent) {
		JsonContent = jsonContent;
	}
	
}
