package com.wetongji3.data;

public class Version {
	private String Url;
	private String Latest;
	private String Current;
	
	public Version() {
		super();
	}

	public Version(String url, String latest, String current) {
		super();
		Url = url;
		Latest = latest;
		Current = current;
	}

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
	
	public boolean hasUpdate(){
		if((Latest!=null)&&(!Latest.equals(""))){
			return Current.compareTo(Latest)<0;
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString()+"Latest:"+Latest;
	}
	
}
