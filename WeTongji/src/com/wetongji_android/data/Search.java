package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class Search {
	@DatabaseField
	private int Type;
	@DatabaseField(id=true)
	private String Keywords;
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public String getKeywords() {
		return Keywords;
	}
	public void setKeywords(String keywords) {
		Keywords = keywords;
	}
	
	
}
