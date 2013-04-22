package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Notification 
{
	@DatabaseField
	private int id;
	@DatabaseField
	private int type;
	
	public Notification(int id, int type) 
	{
		super();
		this.setId(id);
		this.setType(type);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
