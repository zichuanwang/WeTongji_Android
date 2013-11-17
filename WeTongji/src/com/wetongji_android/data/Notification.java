package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class Notification 
{
    /**
     * Type:
     * 1 CourseInvite
     * 2 FriendInvite
     * 3 ActivityInvite
     * 4 FriendInviteConfirm
     */
    public static final int TYPE_COURSE_INVITE = 1;
    public static final int TYPE_FRIEND_INVITE = 2;
    public static final int TYPE_ACTIVITY_INVITE = 3;
    public static final int TYPE_FRIEND_CONFIRM = 4;

	@DatabaseField(id=true)
	private int Id;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Description;
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
	@DatabaseField
	private boolean IsAccepted;
	@DatabaseField
	private String Thumbnail;

    // fields for content
	@DatabaseField(foreign = true, foreignAutoCreate = true)
	private Course course;
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    private Activity activity;
    @DatabaseField(foreign = true, foreignAutoCreate = true)
    private User user;

	@DatabaseField
	private boolean IsConfirmed;
	

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

	public String getThumbnail() {
		return Thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.Thumbnail = thumbnail;
	}

	public boolean isAccepted() {
		return IsAccepted;
	}
	
	public void setAccepted(boolean isAccepted) {
		this.IsAccepted = isAccepted;
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

	public boolean isIsAccepted() {
		return IsAccepted;
	}

	public void setIsAccepted(boolean isAccepted) {
		IsAccepted = isAccepted;
	}

	public boolean isIsConfirmed() {
		return IsConfirmed;
	}

	public void setIsConfirmed(boolean isConfirmed) {
		IsConfirmed = isConfirmed;
	}

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
