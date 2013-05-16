package com.wetongji_android.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityList implements Serializable{

	private static final long serialVersionUID = -1206921764794609032L;
	
	private List<Activity> items = new ArrayList<Activity>();
	
	public List<Activity> getList() {
		return items;
	}
	
	public void setItems(List<Activity> items) {
		this.items = items;
	}
	
}
