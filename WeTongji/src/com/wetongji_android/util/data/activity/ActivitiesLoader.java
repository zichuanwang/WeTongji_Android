package com.wetongji_android.util.data.activity;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.data.DbLoader;
import com.wetongji_android.util.date.DateParser;

public class ActivitiesLoader extends DbLoader<Activity, Integer> {
	
	private PreparedQuery<Activity> query=null;

	public ActivitiesLoader(Context context, Dao<Activity, Integer> dao,
			Bundle args) {
		super(context, dao, args);
		query=getActivityQuery(args);
	}
	

    @Override
	public List<Activity> loadInBackground() {
    	try{
	    	if(query!=null){
	    		return mDao.query(query);
	    	}
	    	else{
	    		return mDao.queryForAll();
	    	}
    	}
    	catch(SQLException e){
    		e.printStackTrace();
    		return null;
    	}
	}


	private PreparedQuery<Activity> getActivityQuery(Bundle args){
		if(args==null){
			return null;
		}
    	QueryBuilder<Activity, Integer> qb=(QueryBuilder<Activity, Integer>) mDao.queryBuilder();
		qb.orderBy(args.getString(QueryHelper.ARG_ORDER_BY), args.getBoolean(QueryHelper.ARG_ASCENDING));
		Where<Activity, Integer> where=qb.where();
		int count=0;
		try{
			if(args.getBoolean(QueryHelper.ARG_HAS_CHANNEL_1)){
				where.eq("Channel_Id", 1);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARG_HAS_CHANNEL_2)){
				where.eq("Channel_Id", 2);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARG_HAS_CHANNEL_3)){
				where.eq("Channel_Id", 3);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARG_HAS_CHANNEL_4)){
				where.eq("Channel_Id", 4);
				count++;
			}
			if(count>1){
				where.or(count);
			}
			if(!args.getBoolean(QueryHelper.ARG_HAS_EXPIRED)){
				where.ge("End", DateParser.buildDateAndTime(Calendar.getInstance()));
				where.and(2);
			}
			return qb.prepare();
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
    }


}
