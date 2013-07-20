package com.wetongji_android.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int Id;
	private String Name;
	private String Display;
	private String Description;
	private String Image;
	private String Email;
	private int InformationCount;
	private int ActivitiesCount;
	private String Background;
	private int Like;
	private String Title;
	private boolean CanLike;
	
	public Account(){
	}

	public Account(int id, String name, String display, String description) {
		super();
		Id = id;
		Name = name;
		Display = display;
		Description = description;
	}

	public Account(int id, String name, String display, String description,
			String image, String email, int informationCount,
			int activitiesCount, String background, int like, String title,
			boolean canLike) {
		super();
		Id = id;
		Name = name;
		Display = display;
		Description = description;
		Image = image;
		Email = email;
		InformationCount = informationCount;
		ActivitiesCount = activitiesCount;
		Background = background;
		Like = like;
		Title = title;
		CanLike = canLike;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDisplay() {
		return Display;
	}

	public void setDisplay(String display) {
		Display = display;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public int getInformationCount() {
		return InformationCount;
	}

	public void setInformationCount(int informationCount) {
		InformationCount = informationCount;
	}

	public int getActivitiesCount() {
		return ActivitiesCount;
	}

	public void setActivitiesCount(int activitiesCount) {
		ActivitiesCount = activitiesCount;
	}

	public String getBackground() {
		return Background;
	}

	public void setBackground(String background) {
		Background = background;
	}

	public int getLike() {
		return Like;
	}

	public void setLike(int like) {
		Like = like;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public boolean isCanLike() {
		return CanLike;
	}

	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Name);
		dest.writeInt(Id);
		dest.writeString(Display);
		dest.writeString(Description);
		dest.writeString(Image);
		dest.writeString(Email);
		dest.writeInt(InformationCount);
		dest.writeInt(ActivitiesCount);
		dest.writeString(Background);
		dest.writeInt(Like);
		dest.writeString(Title);
		dest.writeByte((byte)(CanLike?1:0));
	}
	
	public static final Creator<Account> CREATOR = new Creator<Account>() {
		
		@Override
		public Account[] newArray(int size) {
			return new Account[size];
		}
		
		@Override
		public Account createFromParcel(Parcel source) {
			return new Account(source);
		}
	};

	public Account(Parcel source) {
		this.Name = source.readString();
		this.Id = source.readInt();
		this.Display = source.readString();
		this.Description = source.readString();
		this.Image = source.readString();
		this.Email = source.readString();
		this.InformationCount = source.readInt();
		this.ActivitiesCount = source.readInt();
		this.Background = source.readString();
		this.Like = source.readInt();
		this.Title = source.readString();
		this.CanLike = source.readByte() == 1;
	}
}
