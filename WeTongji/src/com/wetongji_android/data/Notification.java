package com.wetongji_android.data;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class Notification 
{
	@DatabaseField
	private int Id;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Description;
	
	/**
	 * Type: 
	 * 1 CourseInvite
	 * 2 FriendInvite
	 * 3 ActivityInvite
	 * 4 FriendInviteConfirm
	 */
	@DatabaseField
	private int Type;
	@DatabaseField
	private boolean Read;
	@DatabaseField
	private int SourceId;
	@DatabaseField
	private String From;
	@DatabaseField
	private Date SentAt;
	@DatabaseField
	private Date AcceptedAt;
	@DatabaseField
	private Date RejectedAt;
	private boolean isAccepted;
	

	public Notification()
	{
		
	}
	
	public Notification(int id, String title, String description, int type,
			boolean Read, int sourceId, String from, Date sent, Date accept, Date reject) {
		super();
		this.Id = id;
		this.Title = title;
		this.Description = description;
		this.Type = type;
		this.Read = Read;
		this.SourceId = sourceId;
		this.From = from;
		this.SentAt = sent;
		this.AcceptedAt = accept;
		this.RejectedAt = reject;
	}

	public boolean isAccepted() {
		return isAccepted;
	}
	
	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
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

	public boolean getRead() {
		return Read;
	}

	public void setRead(boolean Read) {
		this.Read = Read;
	}

	public int getSourceId() {
		return SourceId;
	}

	public void setSourceId(int sourceId) {
		this.SourceId = sourceId;
	}

	public String getFrom() {
		return From;
	}

	public void setFrom(String from) {
		From = from;
	}

	public Date getSentAt() {
		return SentAt;
	}

	public void setSentAt(Date sentAt) {
		SentAt = sentAt;
	}

	public Date getAcceptedAt() {
		return AcceptedAt;
	}

	public void setAcceptedAt(Date acceptedAt) {
		AcceptedAt = acceptedAt;
	}

	public Date getRejectedAt() {
		return RejectedAt;
	}

	public void setRejectedAt(Date rejectedAt) {
		RejectedAt = rejectedAt;
	}
}
