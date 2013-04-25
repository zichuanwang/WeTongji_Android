package com.wetongji_android.factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.data.DbSaver;

public class ActivityFactory implements ICreateWTObjects<Activity>, LoaderCallbacks<Void>{
	
	private Fragment fragment;
	private Context context;
	private List<Activity> list;
	
	public ActivityFactory(Fragment fragment){
		this.fragment=fragment;
		this.context=this.fragment.getActivity();
		list=new ArrayList<Activity>();
	}

	@Deprecated
	@Override
	public Activity createObject(String jsonStr) {
		Activity activity=new Activity();
		Gson gson=new Gson();
		activity=gson.fromJson(jsonStr, Activity.class);
		return activity;
	}

	@Override
	public List<Activity> createObjects(String jsonStr) {
		list.clear();
		try {
			JSONObject outer=new JSONObject(jsonStr);
			JSONArray array=outer.getJSONArray("Activities");
			for(int i=0;i!=array.length();i++){
				Activity activity=createObject(array.getJSONObject(i).toString());
				list.add(activity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		fragment.getLoaderManager().initLoader(WTApplication.DB_SAVER, null, this);
		return list;
	}

	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle arg1) {
		DbHelper dbHelper=OpenHelperManager.getHelper(context, DbHelper.class);
		Dao<Activity, Integer> dao;
		try {
			dao = dbHelper.getActDao();
			return new DbSaver<Activity, Integer>(context, dao, list);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
		OpenHelperManager.releaseHelper();
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
	}

}
