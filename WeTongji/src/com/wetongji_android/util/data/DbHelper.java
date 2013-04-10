package com.wetongji_android.util.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.android.support.extras.AndroidBaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Around;
import com.wetongji_android.data.ClubNews;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Exam;
import com.wetongji_android.data.ForStaff;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.PersonImage;
import com.wetongji_android.data.SchoolNews;
import com.wetongji_android.data.User;

public class DbHelper extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME="wetongji.db";
	private static final int DB_VERSION=2;
	
	private AndroidBaseDaoImpl<Activity, Integer> actDao;
	private AndroidBaseDaoImpl<Course, Integer> courseDao;
	private AndroidBaseDaoImpl<Exam, Integer> examDao;
	private AndroidBaseDaoImpl<Around, Integer> aroundDao;
	private AndroidBaseDaoImpl<ClubNews, Integer> clubDao;
	private AndroidBaseDaoImpl<ForStaff, Integer> staffDao;
	private AndroidBaseDaoImpl<SchoolNews, Integer> schoolDao;
	private AndroidBaseDaoImpl<Person, Integer> personDao;
	private AndroidBaseDaoImpl<PersonImage, String> imageDao;
	private AndroidBaseDaoImpl<User, String> userDao;
	
	public DbHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connSource) {
		try {
			TableUtils.createTableIfNotExists(connSource, Activity.class);
			TableUtils.createTableIfNotExists(connSource, Course.class);
			TableUtils.createTableIfNotExists(connSource, Exam.class);
			TableUtils.createTableIfNotExists(connSource, Around.class);
			TableUtils.createTableIfNotExists(connSource, ClubNews.class);
			TableUtils.createTableIfNotExists(connSource, ForStaff.class);
			TableUtils.createTableIfNotExists(connSource, SchoolNews.class);
			TableUtils.createTableIfNotExists(connSource, Person.class);
			TableUtils.createTableIfNotExists(connSource, PersonImage.class);
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
			TableUtils.dropTable(connSource, Around.class, false);
			TableUtils.dropTable(connSource, ClubNews.class, false);
			TableUtils.dropTable(connSource, ForStaff.class, false);
			TableUtils.dropTable(connSource, SchoolNews.class, false);
			TableUtils.dropTable(connSource, Person.class, false);
			TableUtils.dropTable(connSource, PersonImage.class, false);
			TableUtils.dropTable(connSource, User.class, false);
			onCreate(db, connSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		super.close();
		actDao=null;
		courseDao=null;
		examDao=null;
		aroundDao=null;
		clubDao=null;
		staffDao=null;
		schoolDao=null;
		personDao=null;
		imageDao=null;
		userDao=null;
	}

	public AndroidBaseDaoImpl<Activity, Integer> getActDao() throws SQLException {
		if(actDao==null){
			actDao=getDao(Activity.class);
		}
		return actDao;
	}

	public AndroidBaseDaoImpl<Course, Integer> getCourseDao() throws SQLException {
		if(courseDao==null){
			courseDao=getDao(Course.class);
		}
		return courseDao;
	}

	public AndroidBaseDaoImpl<Exam, Integer> getExamDao() throws SQLException {
		if(examDao==null){
			examDao=getDao(Exam.class);
		}
		return examDao;
	}

	public AndroidBaseDaoImpl<Around, Integer> getAroundDao() throws SQLException {
		if(aroundDao==null){
			aroundDao=getDao(Around.class);
		}
		return aroundDao;
	}

	public AndroidBaseDaoImpl<ClubNews, Integer> getClubDao() throws SQLException {
		if(clubDao==null){
			clubDao=getDao(ClubNews.class);
		}
		return clubDao;
	}

	public AndroidBaseDaoImpl<ForStaff, Integer> getStaffDao() throws SQLException {
		if(staffDao==null){
			staffDao=getDao(ForStaff.class);
		}
		return staffDao;
	}

	public AndroidBaseDaoImpl<SchoolNews, Integer> getSchoolDao() throws SQLException {
		if(schoolDao==null){
			schoolDao=getDao(SchoolNews.class);
		}
		return schoolDao;
	}

	public AndroidBaseDaoImpl<Person, Integer> getPersonDao() throws SQLException {
		if(personDao==null){
			personDao=getDao(Person.class);
		}
		return personDao;
	}

	public AndroidBaseDaoImpl<PersonImage, String> getImageDao() throws SQLException {
		if(imageDao==null){
			imageDao=getDao(PersonImage.class);
		}
		return imageDao;
	}

	public AndroidBaseDaoImpl<User, String> getUserDao() throws SQLException {
		if(userDao==null){
			userDao=getDao(User.class);
		}
		return userDao;
	}
	
}
