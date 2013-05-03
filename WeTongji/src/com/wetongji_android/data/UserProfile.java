package com.wetongji_android.data;

public class UserProfile {
	
	private boolean CanBeFound;
	private boolean AllowAddFriend;
	private boolean PublicSchedule;
	private boolean PublicScheduleToAll;
	private boolean PublicInformation;
	private boolean PublicInformationToAll;

	public boolean isCanBeFound() {
		return CanBeFound;
	}

	public void setCanBeFound(boolean canBeFound) {
		CanBeFound = canBeFound;
	}

	public boolean isAllowAddFriend() {
		return AllowAddFriend;
	}

	public void setAllowAddFriend(boolean allowAddFriend) {
		AllowAddFriend = allowAddFriend;
	}

	public boolean isPublicSchedule() {
		return PublicSchedule;
	}

	public void setPublicSchedule(boolean publicSchedule) {
		PublicSchedule = publicSchedule;
	}

	public boolean isPublicScheduleToAll() {
		return PublicScheduleToAll;
	}

	public void setPublicScheduleToAll(boolean publicScheduleToAll) {
		PublicScheduleToAll = publicScheduleToAll;
	}

	public boolean isPublicInformation() {
		return PublicInformation;
	}

	public void setPublicInformation(boolean publicInformation) {
		PublicInformation = publicInformation;
	}

	public boolean isPublicInformationToAll() {
		return PublicInformationToAll;
	}

	public void setPublicInformationToAll(boolean publicInformationToAll) {
		PublicInformationToAll = publicInformationToAll;
	}
	
}
