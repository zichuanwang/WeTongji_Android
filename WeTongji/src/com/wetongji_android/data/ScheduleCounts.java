package com.wetongji_android.data;

import java.io.Serializable;

public class ScheduleCounts implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int Course;
	private int Activity;
	public int getCourse() {
		return Course;
	}
	public void setCourse(int course) {
		Course = course;
	}
	public int getActivity() {
		return Activity;
	}
	public void setActivity(int activity) {
		Activity = activity;
	}
}
