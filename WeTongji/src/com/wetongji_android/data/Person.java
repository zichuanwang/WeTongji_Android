package com.wetongji_android.data;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class Person implements Parcelable{
	
	@DatabaseField(id=true)
	private int Id;
	@DatabaseField
	private String Name;
	@DatabaseField
	private String JobTitle;
	@DatabaseField
	private String Words;
	@DatabaseField
	private int NO;
	@DatabaseField
	private String Avatar;
	@DatabaseField
	private String Title;
	@DatabaseField
	private String Description;
	@DatabaseField
	private int Read;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private HashMap<String, String> Images;
	
	public Person() {
		super();
	}

	public Person(int id, String name, String jobTitle, String words,
			int nO, String avatar, String title, String description,
			int read, int like, boolean canLike, HashMap<String, String> images) {
		super();
		Id = id;
		Name = name;
		JobTitle = jobTitle;
		Words = words;
		NO = nO;
		Avatar = avatar;
		Title = title;
		Description = description;
		Read = read;
		Like = like;
		CanLike = canLike;
		Images = images;
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

	public String getJobTitle() {
		return JobTitle;
	}

	public void setJobTitle(String jobTitle) {
		JobTitle = jobTitle;
	}

	public String getWords() {
		return Words;
	}

	public void setWords(String words) {
		Words = words;
	}


	public int getNO() {
		return NO;
	}

	public void setNO(int nO) {
		NO = nO;
	}

	public String getAvatar() {
		return Avatar;
	}

	public void setAvatar(String avatar) {
		Avatar = avatar;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getRead() {
		return Read;
	}

	public void setRead(int read) {
		Read = read;
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

	public HashMap<String, String> getImages() {
		return Images;
	}

	public void setImages(HashMap<String, String> images) {
		Images = images;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeString(Name);
		dest.writeString(JobTitle);
		dest.writeString(Words);
		dest.writeInt(NO);
		dest.writeString(Avatar);
		dest.writeString(Title);
		dest.writeString(Description);
		dest.writeInt(Read);
		dest.writeInt(Like);
		dest.writeByte((byte)(CanLike?1:0));
		dest.writeMap(Images);
	}
	
	@SuppressWarnings("unchecked")
	private Person(Parcel source){
		Id=source.readInt();
		Name=source.readString();
		JobTitle=source.readString();
		Words=source.readString();
		NO=source.readInt();
		Avatar=source.readString();
		Title=source.readString();
		Description=source.readString();
		Read=source.readInt();
		Like=source.readInt();
		CanLike=source.readByte()==1;
		Images=source.readHashMap(HashMap.class.getClassLoader());
	}
	
	public static final Creator<Person> CREATOR=new Creator<Person>() {
		
		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
		
		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}
	};

}
