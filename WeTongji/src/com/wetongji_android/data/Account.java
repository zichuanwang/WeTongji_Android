package com.wetongji_android.data;

public class Account {

	private int Id;
	private String Name;
	private String Display;
	private String Description;
	
	public Account(){
	}

	public Account(int id, String name, String display, String description) {
		super();
		Id = id;
		Name = name;
		Display = display;
		Description = description;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDisplay() {
		return Display;
	}

	public void setDisplay(String display) {
		Display = display;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
}
