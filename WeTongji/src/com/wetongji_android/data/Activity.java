package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Activity extends Event{
	@DatabaseField
	private int Channel_Id;
	@DatabaseField
	private String Organizer;
	@DatabaseField
	private String OrganizerAvatar;
	@DatabaseField
	private String Status;
	@DatabaseField
	private String Image;
	@DatabaseField
	private String CreatedAt;
	
	public Activity() {
		super();
	}
	
	public Activity(int channel_Id, String organizer, String organizerAvatar,
			String status, String image, String createdAt) {
		super();
		Channel_Id = channel_Id;
		Organizer = organizer;
		OrganizerAvatar = organizerAvatar;
		Status = status;
		Image = image;
		CreatedAt = createdAt;
	}
	
	public int getChannel_Id() {
		return Channel_Id;
	}
	public void setChannel_Id(int channel_Id) {
		Channel_Id = channel_Id;
	}
	public String getOrganizer() {
		return Organizer;
	}
	public void setOrganizer(String organizer) {
		Organizer = organizer;
	}
	public String getOrganizerAvatar() {
		return OrganizerAvatar;
	}
	public void setOrganizerAvatar(String organizerAvatar) {
		OrganizerAvatar = organizerAvatar;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	
}
