package com.wetongji_android.data;

import com.j256.ormlite.field.DatabaseField;

public class SearchHistory {
	@DatabaseField
	private int Type;
	@DatabaseField(id=true)
	private String Keywords;
	
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SearchHistory)) {
			return false;
		}
		SearchHistory obj = (SearchHistory)o;
		if (this.Type == obj.getType() && this.Keywords.equals(obj.getKeywords())) {
			return true;
		}
		return false;
	}
	
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
