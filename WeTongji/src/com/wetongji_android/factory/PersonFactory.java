package com.wetongji_android.factory;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wetongji_android.data.Person;
import com.wetongji_android.util.common.WTApplication;

public class PersonFactory extends BaseFactory<Person, Integer> {

	private Gson gson = new Gson();
	
	public PersonFactory(Fragment fragment) {
		super(fragment, Person.class, WTApplication.PERSON_SAVER);
	}
	
	@Override
	public List<Person> createObjects(String jsonStr, boolean needToRefresh){
		list.clear();
		JSONArray array;
		try {
			array = new JSONArray(jsonStr);
			for(int i=0;i!=array.length();i++){
				Person person=createObject(array.getString(i));
				list.add(person);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle args=new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(WTApplication.PERSON_SAVER, args, this).forceLoad();
		return list;
	}
	
	public Person createObject(String jsonStr){
		return gson.fromJson(jsonStr, Person.class);
	}

}
