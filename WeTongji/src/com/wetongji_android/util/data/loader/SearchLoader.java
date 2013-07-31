package com.wetongji_android.util.data.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.SearchHistory;
import com.wetongji_android.util.data.DbListLoader;

public class SearchLoader extends DbListLoader<SearchHistory, String>{

	public SearchLoader(Context context) {
		super(context, SearchHistory.class);
	}

	@Override
	public List<SearchHistory> loadInBackground() 
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
