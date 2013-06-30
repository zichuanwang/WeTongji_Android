package com.wetongji_android.util.data.loader;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.Search;
import com.wetongji_android.util.data.DbListLoader;

public class SearchLoader extends DbListLoader<Search, String>{

	public SearchLoader(Context context, Class<Search> clazz) {
		super(context, clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Search> loadInBackground() 
	{
		// TODO Auto-generated method stub
		try 
		{
			return mDao.queryForAll();
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
