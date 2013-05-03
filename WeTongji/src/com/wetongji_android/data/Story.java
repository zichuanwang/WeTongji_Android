package com.wetongji_android.data;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable{

	private int Id;
	private String Title;
	private String Image;
	private String Body;
	private Date PublishedAt;
	private int CommentsCount;
	private String UserName;
	
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

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public Date getPublishedAt() {
		return PublishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		PublishedAt = publishedAt;
	}

	public int getCommentsCount() {
		return CommentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		CommentsCount = commentsCount;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeString(Title);
		dest.writeString(Image);
		dest.writeString(Body);
		dest.writeLong(PublishedAt.getTime());
		dest.writeInt(CommentsCount);
		dest.writeString(UserName);
	}
	
	private Story(Parcel source){
		Id=source.readInt();
		Title=source.readString();
		Image=source.readString();
		Body=source.readString();
		PublishedAt=new Date(source.readLong());
		CommentsCount=source.readInt();
		UserName=source.readString();
	}
	
	public static final Creator<Story> CREATOR=new Creator<Story>() {
		
		@Override
		public Story[] newArray(int size) {
			return new Story[size];
		}
		
		@Override
		public Story createFromParcel(Parcel source) {
			return new Story(source);
		}
	};
	
}
