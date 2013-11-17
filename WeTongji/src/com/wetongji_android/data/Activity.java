package com.wetongji_android.data;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Activity extends Event implements Serializable{
	private static final long serialVersionUID = -2953062693889370366L;
	
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
	private String Description;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;
	@DatabaseField
	private int Schedule;
	@DatabaseField
	private boolean CanSchedule;
	@DatabaseField
	private int FriendsCount;
	@DatabaseField
	private int AccountId;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private AccountDetails AccountDetails;
	
	public Activity() {
	}
	
	public Activity(int channel_Id, String organizer, String organizerAvatar,
			String status, String image, Date createdAt, String description,
			int like, boolean canLike, int schedule, boolean canSchedule,
			int friendsCount, int accountId) {
		super();
		Channel_Id = channel_Id;
		Organizer = organizer;
		OrganizerAvatar = organizerAvatar;
		Status = status;
		Image = image;
		CreatedAt = createdAt;
		Description = description;
		Like = like;
		CanLike = canLike;
		Schedule = schedule;
		CanSchedule = canSchedule;
		FriendsCount = friendsCount;
		AccountId = accountId;
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

	public int getSchedule() {
		return Schedule;
	}

	public void setSchedule(int schedule) {
		Schedule = schedule;
	}

	public boolean isCanSchedule() {
		return CanSchedule;
	}

	public void setCanSchedule(boolean canSchedule) {
		CanSchedule = canSchedule;
	}

	public int getFriendsCount() {
		return FriendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		FriendsCount = friendsCount;
	}

	public int getAccountId() {
		return AccountId;
	}

	public void setAccountId(int accountId) {
		AccountId = accountId;
	}

	public AccountDetails getAccountDetails() {
		return AccountDetails;
	}

	public void setAccountDetails(AccountDetails accountDetails) {
		AccountDetails = accountDetails;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(Channel_Id);
		dest.writeString(Organizer);
		dest.writeString(OrganizerAvatar);
		dest.writeString(Status);
		dest.writeString(Image);

        dest.writeInt((CreatedAt != null) ? 1 : 0);
        dest.writeLong((CreatedAt != null) ? CreatedAt.getTime() : 0);

		dest.writeString(Description);
		dest.writeInt(Like);
		dest.writeByte((byte)(CanLike?1:0));
		dest.writeInt(Schedule);
		dest.writeByte((byte)(CanSchedule?1:0));
		dest.writeInt(FriendsCount);
		dest.writeInt(AccountId);
		dest.writeSerializable(AccountDetails);
	}
	
	private Activity(Parcel source) {
		super(source);
		Channel_Id=source.readInt();
		Organizer=source.readString();
		OrganizerAvatar=source.readString();
		Status=source.readString();
		Image=source.readString();

		if (source.readInt() == 1) {
            CreatedAt = new Date(source.readLong());
        } else {
            source.readLong();
            CreatedAt = null;
        }

		Description=source.readString();
		Like=source.readInt();
		CanLike=source.readByte()==1;
		Schedule=source.readInt();
		CanSchedule=source.readByte()==1;
		FriendsCount=source.readInt();
		AccountId=source.readInt();
		AccountDetails=(AccountDetails)source.readSerializable();
	}
	
	public static final Creator<Activity> CREATOR=new Creator<Activity>() {
		
		@Override
		public Activity[] newArray(int size) {
			return new Activity[size];
		}
		
		@Override
		public Activity createFromParcel(Parcel source) {
			return new Activity(source);
		}
	};
	
}
