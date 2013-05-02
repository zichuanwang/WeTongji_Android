package com.wetongji_android.data;

import java.util.Date;
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
	private Date CreatedAt;
	@DatabaseField
	String Description;
	@DatabaseField
	int Like;
	@DatabaseField
	boolean CanLike;
	
	public Activity() {
	}
	
	public Activity(int id, Date begin, Date end, String title, String location, 
			String description, int like, boolean canLike, int channel_Id, 
			String organizer, String organizerAvatar, String status, String image, 
			Date createdAt) {
		super(id, begin, end, title, location);
		Channel_Id = channel_Id;
		Organizer = organizer;
		OrganizerAvatar = organizerAvatar;
		Status = status;
		Image = image;
		CreatedAt = createdAt;
		Description = description;
		Like = like;
		CanLike = canLike;
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
	
	public Date getCreatedAt() {
		return CreatedAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getLike() {
		return Like;
	}

	public void setLike(int like) {
		Like = like;
	}

	public boolean isCanLike() {
		return CanLike;
	}

	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}
	
}
