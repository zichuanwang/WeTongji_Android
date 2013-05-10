package com.wetongji_android.util.data.event;

import java.sql.SQLException;
import java.util.Calendar;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.wetongji_android.data.Event;
import com.wetongji_android.util.data.QueryHelper;

import android.content.Context;
import android.os.Bundle;

public class EventLoader extends EventsLoader {

	public EventLoader(Context context, Bundle args) {
		super(context, args);
	}

	@Override
	protected PreparedQuery<Event> getEventsQuery(Bundle args) {
		Calendar begin=(Calendar) args.getSerializable(QueryHelper.ARGS_BEGIN);
		QueryBuilder<Event, Integer> qb=mDao.queryBuilder();
		Where<Event, Integer> where=qb.where();
		try {
			where.ge("Begin", begin.getTime());
			return qb.orderBy("Begin", true).prepare();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
