package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbListSaver;

public class BaseFactory<T, ID> implements LoaderCallbacks<Void>{
	
	public static final String ARG_NEED_TO_REFRESH="needToRefresh";
	
	protected Fragment fragment;
	private Context context;
	protected List<T> list;
	private Class<T> clazz;
	private int loaderId;
	
	protected BaseFactory(Fragment fragment, Class<T> clazz, int loaderId){
		this.fragment=fragment;
		this.context=this.fragment.getActivity();
		list=new ArrayList<T>();
		this.clazz=clazz;
		if(loaderId>0){
			this.loaderId=loaderId;
		}
		else{
			this.loaderId=WTApplication.DB_LIST_SAVER;
		}
	}
	
	protected List<T> createObjects(String jsonStr){
		return createObjects(jsonStr, false);
	}
	
	protected List<T> createObjects(String jsonStr, boolean needToRefresh) {
		list.clear();
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JSONArray array;
		try {
			array = new JSONArray(jsonStr);
			for(int i=0;i!=array.length();i++){
				T t=gson.fromJson(array.getString(i).toString(), clazz);
				list.add(t);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle args=new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(loaderId, args, this).forceLoad();
		return list;
	}
	
	protected List<T> unserializeObjects(String jsonStr) {
		list.clear();
		Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JSONArray array;
		try {
			array = new JSONArray(jsonStr);
			for(int i=0;i!=array.length();i++){
				T t=gson.fromJson(array.getString(i).toString(), clazz);
				list.add(t);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle args) {
		return new DbListSaver<T, ID>(context, clazz, list, args);
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
	}
	
}
