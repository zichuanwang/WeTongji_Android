package com.wetongji_android.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.gson.JsonSyntaxException;
import com.wetongji_android.data.Person;
import com.wetongji_android.util.common.WTApplication;

public class PersonFactory extends BaseFactory<Person, Integer> {

	public PersonFactory(Fragment fragment) {
		super(fragment, Person.class, WTApplication.PERSON_SAVER);
	}
	
	@Override
	protected List<Person> createObjects(String jsonStr, boolean needToRefresh){ 
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
		Person person=new Person();
		try {
			JSONObject jsonObject=new JSONObject(jsonStr);
			person.setId(jsonObject.getInt("Id"));
			person.setName(jsonObject.getString("Name"));
			person.setNO(jsonObject.getString("NO"));
			person.setNO(jsonObject.getString("NO"));
			person.setJobTitle(jsonObject.getString("JobTitle"));
			person.setWords(jsonObject.getString("Words"));
			person.setAvatar(jsonObject.getString("Avatar"));
			person.setDescription(jsonObject.getString("Description"));
			person.setCanLike(jsonObject.getBoolean("CanLike"));
			person.setLike(jsonObject.getInt("Like"));
			person.setRead(jsonObject.getInt("Read"));
			
			JSONObject jsonImage=jsonObject.getJSONObject("Images");
			HashMap<String, String> images=new HashMap<String, String>();
			Iterator<?> it=jsonImage.keys();
			while(it.hasNext()){
				String key=it.next().toString();
				String description=jsonImage.getString(key);
				images.put(key, description);
			}
			person.setImages(images);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return person;
	}

}
