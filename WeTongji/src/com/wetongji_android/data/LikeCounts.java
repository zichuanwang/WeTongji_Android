package com.wetongji_android.data;

import java.io.Serializable;


public class LikeCounts implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7702648493522927089L;
	private int Account;
	private int Information;
	private int User;
	private int Course;
	private int Activity;
	private int Person;
	public int getAccount() {
		return Account;
	}
	public void setAccount(int account) {
		Account = account;
	}
	public int getInformation() {
		return Information;
	}
	public void setInformation(int information) {
		Information = information;
	}
	public int getUser() {
		return User;
	}
	public void setUser(int user) {
		User = user;
	}
	public int getActivity() {
		return Activity;
	}
	public void setActivity(int activity) {
		Activity = activity;
	}
	public int getPerson() {
		return Person;
	}
	public void setPerson(int person) {
		Person = person;
	}
	public int getCourse() {
		return Course;
	}
	public void setCourse(int course) {
		Course = course;
	}
}
