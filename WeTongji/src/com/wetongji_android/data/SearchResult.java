package com.wetongji_android.data;

import java.util.ArrayList;

public class SearchResult {
	private ArrayList<Information> Information;
	private ArrayList<Account> Accounts;
	private ArrayList<User> Users;
	private ArrayList<Activity> Activities;
	private ArrayList<Person> Person;
	public ArrayList<Information> getInformation() {
		return Information;
	}
	public void setInformation(ArrayList<Information> information) {
		Information = information;
	}
	public ArrayList<Account> getAccounts() {
		return Accounts;
	}
	public void setAccounts(ArrayList<Account> accounts) {
		Accounts = accounts;
	}
	public ArrayList<User> getUsers() {
		return Users;
	}
	public void setUsers(ArrayList<User> users) {
		Users = users;
	}
	public ArrayList<Activity> getActivities() {
		return Activities;
	}
	public void setActivities(ArrayList<Activity> activities) {
		Activities = activities;
	}
	public ArrayList<Person> getPerson() {
		return Person;
	}
	public void setPerson(ArrayList<Person> person) {
		Person = person;
	}
	
	
}
