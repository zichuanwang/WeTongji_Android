package com.wetongji_android.data;

import java.io.Serializable;

public class AccountDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean CanLike;
	private String Name;
	private String Description;
	private String Email;
	private int InformationCount;
	private int ActivitiesCount;
	private String Image;
	private String Display;
	private String Background;
	private int Id;
	private int Like;
	private String Title;

	public boolean isCanLike() {
		return CanLike;
	}

	public void setCanLike(boolean canLike) {
		CanLike = canLike;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
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

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getDisplay() {
		return Display;
	}

	public void setDisplay(String display) {
		Display = display;
	}

	public String getBackground() {
		return Background;
	}

	public void setBackground(String background) {
		Background = background;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
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
}
