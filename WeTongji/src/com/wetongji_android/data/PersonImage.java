package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class PersonImage {
	@DatabaseField(id=true)
	String Url;
	@DatabaseField
	String Describtion;
	@DatabaseField(canBeNull = false, foreign = true)
	private Person person;
	
	public PersonImage() {}
	
	public PersonImage(String url, String describtion, Person person) {
		super();
		Url = url;
		Describtion = describtion;
		this.person = person;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getDescribtion() {
		return Describtion;
	}
	
	public void setDescribtion(String describtion) {
		Describtion = describtion;
	}
	
}