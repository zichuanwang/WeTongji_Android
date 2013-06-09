/**
 * This loader is used to load information
 * data from the database
 */
package com.wetongji_android.util.data.information;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import android.os.Bundle;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.data.DbListLoader;
import com.wetongji_android.util.data.QueryHelper;

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
			query = getInformationQuery(args);
			
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
		
		QueryBuilder<Information, Integer> qb = mDao.queryBuilder();
		//Sort by the create time descending
		qb.orderBy(QueryHelper.ARGS_INFO_ORDER_BY, false);
		Where<Information, Integer> where = qb.where();
		
		try 
		{
			where.eq("Category", args.get(QueryHelper.ARGS_INFO_TYPE));
			
			return qb.prepare();
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}