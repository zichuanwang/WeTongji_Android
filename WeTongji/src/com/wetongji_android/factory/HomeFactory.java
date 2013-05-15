package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;

import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Information;

public class HomeFactory {

	private ActivityFactory actFactory;
	private InformationFactory infoFactory;
	private PersonFactory personFactory;
	private AccountFactory accountFactory;
	
	public HomeFactory(Fragment fragment){
		actFactory=new ActivityFactory(fragment);
		infoFactory=new InformationFactory(fragment);
		personFactory=new PersonFactory(fragment);
		accountFactory=new AccountFactory();
	}
	
	public List<Activity> createActivities(String jsonStr){
		List<Activity> result=new ArrayList<Activity>();
		result=actFactory.createObjects(jsonStr, false);
		return result;
	}
	
	public List<Information> createInfos(String jsonStr){
		List<Information> result=new ArrayList<Information>();
		result=infoFactory.createObjects(jsonStr, false);
		return result;
	}
	
	public List<Object> createFeatures(String jsonStr){
		List<Object> result=new ArrayList<Object>();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			result.add(personFactory.createObject(outer.getString("Person")));
			result.add(accountFactory.createObjects(outer.getString("AccountPopulor")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
