/**
 * This loader is used to load information
 * data from the database and also order by the
 * created date descending
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
	
	/**
	 * Generate the query option and for all the query the sort
	 * is stable, but has different category
	 * @param args
	 * @return
	 */
	private PreparedQuery<Information> getInformationQuery(Bundle bundle)
	{
		if(bundle == null)
		{
			return null;
		}
		
		QueryBuilder<Information, Integer> qb = mDao.queryBuilder();
		//Sort by the create time descending
		qb.orderBy(QueryHelper.ARGS_INFO_ORDER_BY, false);
		Where<Information, Integer> where = qb.where();
		int count = 0;
		
		try
		{	
			if(bundle.getBoolean(QueryHelper.ARGS_INFO_TYPE_ONE))
			{
				where.eq("Category", QueryHelper.ARGS_INFO_TYPE_ONE);
				count ++;
			}
			if(bundle.getBoolean(QueryHelper.ARGS_INFO_TYPE_TWO))
			{
				where.eq("Category", QueryHelper.ARGS_INFO_TYPE_TWO);
				count ++;
			}
			if(bundle.getBoolean(QueryHelper.ARGS_INFO_TYPE_THREE))
			{
				where.eq("Category", QueryHelper.ARGS_INFO_TYPE_THREE);
				count ++;
			}
			if(bundle.getBoolean(QueryHelper.ARGS_INFO_TYPE_FOUR))
			{
				where.eq("Category", QueryHelper.ARGS_INFO_TYPE_FOUR);
				count ++;
			}
			
			if(count > 1)
			{
				where.or(count);
			}
			
			return qb.prepare();
		}catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}