package com.wetongji_android.factory;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;

public class UserFactory extends BaseFactory<User, String>
{	
	private int nextPage = 0;
	private Gson gson = new GsonBuilder().
			setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

	public UserFactory(Fragment fragment) 
	{
		super(fragment, User.class, WTApplication.USER_SAVER);
	}

	private User createObject(String jsonStr)
	{
		return gson.fromJson(jsonStr, User.class);
	}
	
	public User createSingleObject(String jsonStr)
	{
		list.clear();
		User user = createObject(jsonStr);
		
		list.add(user);
		Bundle args=new Bundle();
		args.putBoolean(BaseFactory.ARG_NEED_TO_REFRESH, true);
		fragment.getLoaderManager().initLoader(WTApplication.USER_SAVER, args, this).forceLoad();
		
		return user;
	}
	
	@Override
	public List<User> createObjects(String jsonStr, boolean needToRefresh) 
	{
		list.clear();
		JSONArray array;
		
		try {
			JSONObject json = new JSONObject(jsonStr);
			nextPage = json.optInt("NextPager");
			if(json.has("Like")){
				array = json.getJSONArray("Like");
				for(int i = 0; i < array.length(); i++){
					list.add(createObject(array.getJSONObject(i).getJSONObject("ModelDetails").toString()));
				}
			}else{
				array = json.getJSONArray("Users");
				for(int j = 0; j < array.length(); j++){
					list.add(createObject(array.getJSONObject(j).toString()));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Bundle args=new Bundle();
		args.putBoolean(BaseFactory.ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(WTApplication.USER_SAVER, args, this).forceLoad();
		
		return list;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
