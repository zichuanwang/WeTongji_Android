package com.wetongji_android.data;

public class CourseInvite extends Notification
{
	private Course course;
	
	public CourseInvite(Course course)
	{
		super();
		this.setCourse(course);
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
