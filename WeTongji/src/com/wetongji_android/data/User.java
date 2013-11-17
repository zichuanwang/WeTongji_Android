package com.wetongji_android.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class User implements Parcelable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2309411520291683279L;
    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private int id;
	@DatabaseField
	private String NO;
	@DatabaseField
	private String Name;
	@DatabaseField
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
	@DatabaseField(dataType=DataType.SERIALIZABLE)
	private ScheduleCounts ScheduleCount;
	@DatabaseField
	private boolean IsFriend;
	@DatabaseField
	private int FriendCount;
	@DatabaseField
	private int Like;
	@DatabaseField
	private boolean CanLike;
	@DatabaseField
	private String Avatar;
	@DatabaseField
	private String UserType;
	
	public User() {
		super();
	}

	public User(String nO, String name, String uID, String phone,
			String displayName, String major, String nativePlace,
			String degree, String gender, String year, String birthday,
			String plan, String sinaWeibo, String qQ, String department,
			String email, int friendCount, int like, boolean canLike, String avatar, String userType) {
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
		Like = like;
		CanLike = canLike;
		Avatar = avatar;
		UserType = userType;
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

	public ScheduleCounts getScheduleCount() {
		return ScheduleCount;
	}

	public void setScheduleCount(ScheduleCounts scheduleCount) {
		ScheduleCount = scheduleCount;
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

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(NO);
		dest.writeString(Name);
		dest.writeString(UID);
		dest.writeString(Phone);
		dest.writeString(DisplayName);
		dest.writeString(Major);
		dest.writeString(NativePlace);
		dest.writeString(Degree);
		dest.writeString(Gender);
		dest.writeString(Year);
		dest.writeString(Birthday);
		dest.writeString(Plan);
		dest.writeString(SinaWeibo);
		dest.writeString(QQ);
		dest.writeString(Department);
		dest.writeString(Email);
		dest.writeString(Words);
		dest.writeString(Room);
		dest.writeSerializable(LikeCount);
		dest.writeSerializable(ScheduleCount);
		dest.writeByte((byte)(IsFriend?1:0));
		dest.writeInt(FriendCount);
		dest.writeInt(Like);
		dest.writeByte((byte)(CanLike?1:0));
		dest.writeString(Avatar);
		dest.writeString(UserType);
	}
	
	private User(Parcel source)
	{
		NO = source.readString();
		Name = source.readString();
		UID = source.readString();
		Phone = source.readString();
		DisplayName = source.readString();
		Major = source.readString();
		NativePlace = source.readString();
		Degree = source.readString();
		Gender = source.readString();
		Year = source.readString();
		Birthday = source.readString();
		Plan = source.readString();
		SinaWeibo = source.readString();
		QQ = source.readString();
		Department = source.readString();
		Email = source.readString();
		Words = source.readString();
		Room = source.readString();
		LikeCount = (LikeCounts) source.readSerializable();
		ScheduleCount = (ScheduleCounts)source.readSerializable();
		IsFriend = source.readByte() == 1;
		FriendCount = source.readInt();
		Like = source.readInt();
		CanLike = source.readByte() == 1;
		Avatar = source.readString();
		UserType = source.readString();
	}
	
	public static final Creator<User> CREATOR = new Creator<User>()
	{
		@Override
		public User createFromParcel(Parcel source) 
		{
			return new User(source);
		}

		@Override
		public User[] newArray(int size) 
		{
			return new User[size];
		}
	};
}
