package com.wetongji_android.util.data.loader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.wetongji_android.data.Schedule;
import com.wetongji_android.util.data.DbListLoader;

public class ScheduleLoader extends DbListLoader<Schedule, Integer>{

	private int id;
	
	public ScheduleLoader(Context context, int id) {
		super(context, Schedule.class);
		this.id = id;
	}

	@Override
	public List<Schedule> loadInBackground() {
		List<Schedule> result = new ArrayList<Schedule>();
		try {
			Schedule schedule = mDao.queryForId(id);
			result.add(schedule);
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
		
		return result;
	}
	
	

}
