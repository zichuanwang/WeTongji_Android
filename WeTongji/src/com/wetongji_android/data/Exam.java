package com.wetongji_android.data;

import java.util.Date;

import android.os.Parcel;

public class Exam extends Course {
	
	public Exam() {
		super();
	}

	public Exam(int id, Date begin, Date end, String title, String location, 
			String nO, int hours, float point, String teacher, String required) {
		super(id, begin, end, title, location, required, hours, point, required, required);
	}
	
	private Exam(Parcel source) {
		super(source);
	}
	
	public static final Creator<Exam> CREATOR=new Creator<Exam>() {
		
		@Override
		public Exam[] newArray(int size) {
			return new Exam[size];
		}
		
		@Override
		public Exam createFromParcel(Parcel source) {
			return new Exam(source);
		}
	};
	
}
