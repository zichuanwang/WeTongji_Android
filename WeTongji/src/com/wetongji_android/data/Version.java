package com.wetongji_android.data;

public class Version {
	private String Url;
	private String Latest;
	private String Current;
	private String Description;
	
	public String getCurrent() {
		return Current;
	}
	
	public void setCurrent(String current) {
		Current = current;
	}
	
	public String getLatest() {
		return Latest;
	}
	
	public void setLatest(String latest) {
		Latest = latest;
	}
	
	public String getUrl() {
		return Url;
	}
	
	public void setUrl(String url) {
		Url = url;
	}
	
	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
