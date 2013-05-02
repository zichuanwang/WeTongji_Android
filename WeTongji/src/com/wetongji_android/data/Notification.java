package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Notification 
{
	@DatabaseField
	private int id;
	@DatabaseField
	private String title;
	@DatabaseField
	private String description;
	@DatabaseField
	private int type;
	@DatabaseField
	private int status;
	@DatabaseField
	private int sourceId;

	public Notification(int id, String title, String description, int type,
			int status, int sourceId) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.type = type;
		this.status = status;
		this.sourceId = sourceId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
}
