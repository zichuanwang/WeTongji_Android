package com.wetongji_android.factory;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.gson.Gson;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;

public class UserFactory extends BaseFactory<User, String>
{	
	private Gson gson=new Gson();

	public UserFactory(Fragment fragment) 
	{
		super(fragment, User.class, WTApplication.USER_SAVER);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		list.clear();
		JSONArray array;
		
		try 
		{
			array = new JSONArray(jsonStr);
			for(int i = 0; i < array.length(); i++)
			{
				list.add(createObject(array.getString(i)));
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Bundle args=new Bundle();
		args.putBoolean(BaseFactory.ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(WTApplication.USER_SAVER, args, this).forceLoad();
		
		return list;
	}
}
