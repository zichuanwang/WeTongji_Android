package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Notification 
{
	@DatabaseField
	private int Id;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Description;
	@DatabaseField
	private int Type;
	@DatabaseField
	private int Status;
	@DatabaseField
	private int SourceId;

	public Notification(int id, String title, String description, int type,
			int status, int sourceId) {
		super();
		this.Id = id;
		this.Title = title;
		this.Description = description;
		this.Type = type;
		this.Status = status;
		this.SourceId = sourceId;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		this.Type = type;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		this.Status = status;
	}

	public int getSourceId() {
		return SourceId;
	}

	public void setSourceId(int sourceId) {
		this.SourceId = sourceId;
	}
}
