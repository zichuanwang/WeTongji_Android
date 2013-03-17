package com.wetongji_android.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Person {
	@DatabaseField(id=true)
	private int Id;
	@DatabaseField
	private String Name;
	@DatabaseField
	private String JobTitle;
	@DatabaseField
	private String Words;
	@DatabaseField
	private String NO;
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
	@ForeignCollectionField(eager=true)
	ForeignCollection<PersonImage> Images;
	
	public Person() {
		super();
	}

	public Person(int id, String name, String jobTitle, String words,
			String nO, String avatar, String title, String description,
			int read, int like, boolean canLike,
			ForeignCollection<PersonImage> images) {
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

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
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

	public ForeignCollection<PersonImage> getImages() {
		return Images;
	}

	public void setImages(ForeignCollection<PersonImage> images) {
		Images = images;
	}
	
}
