package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.gson.Gson;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbListSaver;

public class UserFactory implements LoaderCallbacks<Void>{
	
	private User mUser;
	private SherlockFragmentActivity mActivity;
	private Gson gson=new Gson();
	
	
	public UserFactory(SherlockFragmentActivity activity) {
		mActivity = activity;
	}

	
	public User createObject(String jsonStr){
		mUser = gson.fromJson(jsonStr, User.class);
		
		Bundle args=new Bundle();
		args.putBoolean(BaseFactory.ARG_NEED_TO_REFRESH, true);
		mActivity.getSupportLoaderManager().initLoader(WTApplication.USER_SAVER, args, this).forceLoad();
		
		return mUser;
	}

	public List<User> createObjects(String jsonStr)
	{
		List<User> users = new ArrayList<User>();
		JSONArray array;
		
		try 
		{
			array = new JSONArray(jsonStr);
			for(int i = 0; i < array.length(); i++)
			{
				users.add(createObject(array.getString(i)));
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Bundle args=new Bundle();
		args.putBoolean(BaseFactory.ARG_NEED_TO_REFRESH, true);
		mActivity.getSupportLoaderManager().initLoader(WTApplication.USER_SAVER, args, this).forceLoad();*/
		return users;
	}
	
	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		List<User> list = new ArrayList<User>();
		list.add(mUser);
		return new DbListSaver<User, String>(mActivity, User.class, list, arg1);
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
	}
	
}
