package com.wetongji_android.util.data.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.Search;
import com.wetongji_android.util.data.DbListLoader;

public class SearchLoader extends DbListLoader<Search, String>{

	public SearchLoader(Context context) {
		super(context, Search.class);
	}

	@Override
	public List<Search> loadInBackground() 
	{
		try 
		{
			return mDao.queryForAll();
		} catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
