package com.wetongji_android.factory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.data.DbListSaver;

public class BaseFactory<T, ID> implements LoaderCallbacks<Void>{
	
	private static final String ARG_NEED_TO_REFRESH="needToRefresh";
	
	protected Fragment fragment;
	private Context context;
	protected List<T> list;
	private Class<T> clazz;
	
	public BaseFactory(Fragment fragment, Class<T> clazz){
		this.fragment=fragment;
		this.context=this.fragment.getActivity();
		list=new ArrayList<T>();
		this.clazz=clazz;
	}
	
	public List<T> createObjects(String jsonStr){
		return createObjects(jsonStr, false);
	}

	protected List<T> createObjects(String jsonStr, boolean needToRefresh) {
		list.clear();
		Gson gson=new Gson();
		list=gson.fromJson(jsonStr, new TypeToken<List<Activity>>(){}.getType());
		
		Bundle args=new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(WTApplication.DB_LIST_SAVER, args, this);
		return list;
	}

	@Override
	public Loader<Void> onCreateLoader(int arg0, Bundle args) {
		DbHelper dbHelper=WTApplication.getInstance().getDbHelper();
		try {
			if(args!=null&&args.getBoolean(ARG_NEED_TO_REFRESH)){
				TableUtils.clearTable(dbHelper.getConnectionSource(), clazz);
			}
			return new DbListSaver<T, ID>(context, clazz, list);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Void> arg0, Void arg1) {
	}

	@Override
	public void onLoaderReset(Loader<Void> arg0) {
	}
	
}
