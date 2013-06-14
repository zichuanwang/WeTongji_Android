package com.wetongji_android.ui.people;

import android.os.Bundle;
import android.view.Window;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;

public class PeopleDetailActivtiy extends SherlockFragmentActivity {

	private Person mPerson;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recieveData();
		setUpUI();
	}


	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_detail);
	}


	private void recieveData() {
		// TODO Auto-generated method stub
		
	}
}
