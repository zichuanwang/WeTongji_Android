package com.wetongji_android.util.data.event;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.data.Event;
import com.wetongji_android.util.data.DbListLoader;
import com.wetongji_android.util.data.QueryHelper;

public class EventsLoader extends DbListLoader<Event, Integer> {

	private PreparedQuery<Event> query=null;
	private Bundle args;
	
	public EventsLoader(Context context, Bundle args) {
		super(context, Event.class);
		this.args=args;
	}
	
	@Override
	public List<Event> loadInBackground() {
    	try{
    		Log.v("Event Db", "count="+mDao.countOf());
    		query=getEventsQuery(args);
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
	
	private PreparedQuery<Event> getEventsQuery(Bundle args){
		Calendar begin=(Calendar) args.getSerializable(QueryHelper.ARGS_BEGIN);
		Calendar end=(Calendar) args.getSerializable(QueryHelper.ARGS_END);
		QueryBuilder<Event, Integer> qb=mDao.queryBuilder();
		Where<Event, Integer> where=qb.where();
		try {
			where.ge("Begin", begin.getTime());
			where.le("End", end.getTime());
			where.and(2);
			return qb.prepare();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
