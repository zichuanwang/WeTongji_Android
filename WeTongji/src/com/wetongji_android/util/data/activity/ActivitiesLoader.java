package com.wetongji_android.util.data.activity;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.data.Activity;
import com.wetongji_android.util.data.DbListLoader;
import com.wetongji_android.util.data.QueryHelper;

public class ActivitiesLoader extends DbListLoader<Activity, Integer> {
	
	private PreparedQuery<Activity> query=null;
	private Bundle args;

	public ActivitiesLoader(Context context, Bundle args) {
		super(context, Activity.class);
		this.args=args;
	}
	
    @Override
	public List<Activity> loadInBackground() {
    	try{
    		query=getActivitiesQuery(args);
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

	private PreparedQuery<Activity> getActivitiesQuery(Bundle args){
		if(args==null){
			return null;
		}
    	QueryBuilder<Activity, Integer> qb=mDao.queryBuilder();
		qb.orderBy(args.getString(QueryHelper.ARGS_ORDER_BY), args.getBoolean(QueryHelper.ARGS_ASCENDING));
		Where<Activity, Integer> where=qb.where();
		int count=0;
		try{
			if(args.getBoolean(QueryHelper.ARGS_HAS_CHANNEL_1)){
				where.eq("Channel_Id", 1);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARGS_HAS_CHANNEL_2)){
				where.eq("Channel_Id", 2);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARGS_HAS_CHANNEL_3)){
				where.eq("Channel_Id", 3);
				count++;
			}
			if(args.getBoolean(QueryHelper.ARGS_HAS_CHANNEL_4)){
				where.eq("Channel_Id", 4);
				count++;
			}
			if(count>1){
				where.or(count);
			}
			if(!args.getBoolean(QueryHelper.ARGS_HAS_EXPIRED)){
				where.ge("End", new Date());
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
