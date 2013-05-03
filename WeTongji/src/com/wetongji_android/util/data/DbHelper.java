package com.wetongji_android.util.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Event;
import com.wetongji_android.data.Exam;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.User;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME="wetongji.db";
	private static final int DB_VERSION=2;
	
	public DbHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connSource) {
		try {
			TableUtils.createTableIfNotExists(connSource, Activity.class);
			TableUtils.createTableIfNotExists(connSource, Course.class);
			TableUtils.createTableIfNotExists(connSource, Exam.class);
			TableUtils.createTableIfNotExists(connSource, Event.class);
			TableUtils.createTableIfNotExists(connSource, Information.class);
			TableUtils.createTableIfNotExists(connSource, Person.class);
			TableUtils.createTableIfNotExists(connSource, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connSource, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(connSource, Activity.class, false);
			TableUtils.dropTable(connSource, Course.class, false);
			TableUtils.dropTable(connSource, Exam.class, false);
			TableUtils.dropTable(connSource, Event.class, false);
			TableUtils.dropTable(connSource, Information.class, false);
			TableUtils.dropTable(connSource, Person.class, false);
			TableUtils.dropTable(connSource, User.class, false);
			onCreate(db, connSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		super.close();
	}

}
