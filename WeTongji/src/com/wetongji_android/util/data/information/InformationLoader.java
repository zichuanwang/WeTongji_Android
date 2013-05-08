package com.wetongji_android.util.data.information;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import android.os.Bundle;

import com.j256.ormlite.stmt.PreparedQuery;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.data.DbListLoader;

public class InformationLoader extends DbListLoader<Information, Integer> 
{
	private PreparedQuery<Information> query = null;
	private Bundle args;
	
	public InformationLoader(Context context, Bundle args)
	{
		super(context, Information.class);
		this.args = args;
	}

	@Override
	public List<Information> loadInBackground() 
	{
		// TODO Auto-generated method stub
		try 
		{	
			if(query != null)
			{
				return mDao.query(query);
			}else
			{
				return mDao.queryForAll();
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private PreparedQuery<Information> getInformationQuery(Bundle args)
	{
		if(args == null)
			return null;
		
		return null;
	}
}