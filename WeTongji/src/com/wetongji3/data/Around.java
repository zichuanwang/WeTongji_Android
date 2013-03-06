package com.wetongji3.data;

import com.j256.ormlite.field.DatabaseField;

public class Around extends News {
	@DatabaseField
	private String Location;
	@DatabaseField
	private String Contact;
	@DatabaseField
	private String TicketService;
	
	public Around() {
		super();
	}

	public Around(String location, String contact, String ticketService) {
		super();
		Location = location;
		Contact = contact;
		TicketService = ticketService;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public String getTicketService() {
		return TicketService;
	}

	public void setTicketService(String ticketService) {
		TicketService = ticketService;
	}
	
}
