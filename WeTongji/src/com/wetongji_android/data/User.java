package com.wetongji_android.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class User {
	@DatabaseField
	private String NO;
	@DatabaseField
	private String Name;
	@DatabaseField(id=true)
	private String UID;
	@DatabaseField
	private String Phone;
	@DatabaseField
	private String DisplayName;
	@DatabaseField
	private String Major;
	@DatabaseField
	private String NativePlace;
	@DatabaseField
	private String Degree;
	@DatabaseField
	private String Gender;
	@DatabaseField
	private String Year;
	@DatabaseField
	private String Birthday;
	@DatabaseField
	private String Plan;
	@DatabaseField
	private String SinaWeibo;
	@DatabaseField
	private String QQ;
	@DatabaseField
	private String Department;
	@DatabaseField
	private String Email;
	@DatabaseField
	private String Words;
	@DatabaseField
	private String Room;
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private LikeCounts LikeCount;
	@DatabaseField
	private boolean IsFriend;
	@DatabaseField
	private int FriendCount;
	@DatabaseField
	private String Avatar;
	
	public User() {
		super();
	}

	public User(String nO, String name, String uID, String phone,
			String displayName, String major, String nativePlace,
			String degree, String gender, String year, String birthday,
			String plan, String sinaWeibo, String qQ, String department,
			String email, int friendCount, String avatar) {
		super();
		NO = nO;
		Name = name;
		UID = uID;
		Phone = phone;
		DisplayName = displayName;
		Major = major;
		NativePlace = nativePlace;
		Degree = degree;
		Gender = gender;
		Year = year;
		Birthday = birthday;
		Plan = plan;
		SinaWeibo = sinaWeibo;
		QQ = qQ;
		Department = department;
		Email = email;
		FriendCount = friendCount;
		Avatar = avatar;
	}

	public String getAvatar() {
		return Avatar;
	}

	public void setAvatar(String avatar) {
		Avatar = avatar;
	}

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public String getMajor() {
		return Major;
	}

	public void setMajor(String major) {
		Major = major;
	}

	public String getNativePlace() {
		return NativePlace;
	}

	public void setNativePlace(String nativePlace) {
		NativePlace = nativePlace;
	}

	public String getDegree() {
		return Degree;
	}

	public void setDegree(String degree) {
		Degree = degree;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getPlan() {
		return Plan;
	}

	public void setPlan(String plan) {
		Plan = plan;
	}

	public String getSinaWeibo() {
		return SinaWeibo;
	}

	public void setSinaWeibo(String sinaWeibo) {
		SinaWeibo = sinaWeibo;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getWords() {
		return Words;
	}

	public void setWords(String words) {
		Words = words;
	}

	public String getRoom() {
		return Room;
	}

	public void setRoom(String room) {
		Room = room;
	}

	public LikeCounts getLikeCount() {
		return LikeCount;
	}

	public void setLikeCount(LikeCounts likeCount) {
		LikeCount = likeCount;
	}

	public boolean isIsFriend() {
		return IsFriend;
	}

	public void setIsFriend(boolean isFriend) {
		IsFriend = isFriend;
	}
	
	public int getFriendCount() {
		return FriendCount;
	}

	public void setFriendCount(int friendCount) {
		FriendCount = friendCount;
	}
}
