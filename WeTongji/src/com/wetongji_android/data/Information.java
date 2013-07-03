package com.wetongji_android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Information implements Comparable<Information>, Parcelable,
Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(id=true)
	private int Id;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Context;
	@DatabaseField
	private String Source;
	@DatabaseField
	private String Summary;
	@DatabaseField
	private String Contact;
	@DatabaseField
	private String Location;
	@DatabaseField
	private boolean HasTicket;
	@DatabaseField
	private String TicketService;
	@DatabaseField
	private int Read;
	@DatabaseField
	private Date CreatedAt;
	@DatabaseField
	private String Category;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;
	@DatabaseField
	private String Organizer;
	@DatabaseField
	private String OrganizerAvatar;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private ArrayList<String> Images;
	
	public Information() 
	{
		super();
	}

	public Information(int id, String title, String context, String source,
			String summary, String contact, String location, boolean hasTicket,
			String ticketService, int read, Date createdAt, String category,
			int like, boolean canLike, String organizer,
			String organizerAvatar, ArrayList<String> images) {
		super();
		Id = id;
		Title = title;
		Context = context;
		Source = source;
		Summary = summary;
		Contact = contact;
		Location = location;
		HasTicket = hasTicket;
		TicketService = ticketService;
		Read = read;
		CreatedAt = createdAt;
		Category = category;
		Like = like;
		CanLike = canLike;
		Organizer = organizer;
		OrganizerAvatar = organizerAvatar;
		Images = images;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public boolean isHasTicket() {
		return HasTicket;
	}

	public void setHasTicket(boolean hasTicket) {
		HasTicket = hasTicket;
	}

	public String getTicketService() {
		return TicketService;
	}

	public void setTicketService(String ticketService) {
		TicketService = ticketService;
	}

	public int getRead() {
		return Read;
	}

	public void setRead(int read) {
		Read = read;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
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

	public ArrayList<String> getImages() {
		return Images;
	}

	public void setImages(ArrayList<String> images) {
		Images = images;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeString(Title);
		dest.writeString(Context);
		dest.writeString(Source);
		dest.writeString(Summary);
		dest.writeString(Contact);
		dest.writeString(Location);
		dest.writeByte((byte)(HasTicket?1:0));
		dest.writeString(TicketService);
		dest.writeInt(Read);
		dest.writeLong(CreatedAt.getTime());
		dest.writeString(Category);
		dest.writeInt(Like);
		dest.writeByte((byte)(CanLike?1:0));
		dest.writeString(Organizer);
		dest.writeString(OrganizerAvatar);
		dest.writeList(Images);
	}
	
	@SuppressWarnings("unchecked")
	private Information(Parcel source){
		Id=source.readInt();
		Title=source.readString();
		Context=source.readString();
		Source=source.readString();
		Summary=source.readString();
		Contact=source.readString();
		Location=source.readString();
		HasTicket=source.readByte()==1;
		TicketService=source.readString();
		Read=source.readInt();
		CreatedAt=new Date(source.readLong());
		Category=source.readString();
		Like=source.readInt();
		CanLike=source.readByte()==1;
		Organizer=source.readString();
		OrganizerAvatar=source.readString();
		Images=source.readArrayList(ArrayList.class.getClassLoader());
	}
	
	public static final Creator<Information> CREATOR=new Creator<Information>() {
		
		@Override
		public Information[] newArray(int size) {
			return new Information[size];
		}
		
		@Override
		public Information createFromParcel(Parcel source) {
			return new Information(source);
		}
	};

	@Override
	public int compareTo(Information another) 
	{
		// TODO Auto-generated method stub
		return this.CreatedAt.compareTo(another.CreatedAt);
	}
}
