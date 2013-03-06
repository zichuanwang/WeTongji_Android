package com.wetongji3.data;

import com.j256.ormlite.field.DatabaseField;

public class ClubNews extends News {
	@DatabaseField
	private String Organizer;
	@DatabaseField
	private String OrganizerAvatar;
	
	public ClubNews() {
		super();
	}

	public ClubNews(String organizer, String organizerAvatar) {
		super();
		Organizer = organizer;
		OrganizerAvatar = organizerAvatar;
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
	
	
}
