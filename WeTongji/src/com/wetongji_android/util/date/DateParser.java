package com.wetongji_android.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 解析服务器返回日期和时间的工具类
 * @author John
 *
 */
public class DateParser {
	
	private static SimpleDateFormat serverSourse=new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
	private static SimpleDateFormat serverTarget=new SimpleDateFormat(
			"yyyy-MM-dd'%'", Locale.US);
	
	public static int getWeekNumber(String schoolYearStartAt) {
	    int weekCount = 0;
	    if(schoolYearStartAt.equals(""))
            return 0; 
	    
	    Date date=new Date();
        try {
            date=serverSourse.parse(schoolYearStartAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    
        long start = date.getTime();
        long now = System.currentTimeMillis();
        
        if(start > now)
            return 0;
        else
            weekCount = (int)((now - start) / (1000 * 60 * 60 * 24 * 7) + 1);
        
	    return weekCount;
	}
	
	public static Calendar parseDateAndTime(String dateFromServer){
		try {
			Date date=serverSourse.parse(dateFromServer);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
			return Calendar.getInstance();
		}
	}
	
	public static String buildDateAndTime(Calendar cal){
		return serverTarget.format(cal.getTime());
	}
	
	public static Calendar getFirstDayOfWeek(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		return cal;
	}
	
	public static Calendar getLastDayOfWeek(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 7);
		return cal;
	}
	
}
