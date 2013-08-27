package com.wetongji_android.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.wetongji_android.R;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

/**
 * 解析日期的工具类
 * @author John
 *
 */
public class DateParser {
	
	// hard code the start time and week count of 2013-2014 semester
	public static final String TIME_13_14_START = "2013-09-09T00:00:00+08:00";
	public static final int WEEK_COUNT_13_14 = 19;
	
	public static final String TIME_12_13_START = "2013-02-25T00:00:00+08:00";
	public static final int WEEK_COUNT_12_13 = 19;
	
	public static final String TIME_2013_SUMMER_START = "2013-07-08T00:00:00+08:00";
	public static final String TIME_2013_SUMMER_END = "2013-09-08T23:59:59+08:00";
	
	public static final int WEEK_NUMBER_SUMMER = 0;
	public static final int WEEK_NUMBER_WINTER = 20;
	
	private static final long MILLIS_WEEK = 1000 * 60 * 60 * 24 * 7;
	
	private static SimpleDateFormat serverSourse=new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
	private static SimpleDateFormat serverTarget=new SimpleDateFormat(
			"yyyy-MM-dd", Locale.US);
	
	public static int getWeekNumber() {
		long DATE_12_13_START = 0;
		long DATE_13_SUMMER_START = 0;
		long DATE_13_14_START = 0;
		try {
			DATE_12_13_START = serverSourse.parse(TIME_12_13_START).getTime();
			DATE_13_SUMMER_START = serverSourse.parse(TIME_2013_SUMMER_START).getTime();
			DATE_13_14_START = serverSourse.parse(TIME_13_14_START).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	    int weekCount = 0;
        long now = System.currentTimeMillis();
        
        if (now >= DATE_12_13_START && now < DATE_13_SUMMER_START) {
        	weekCount = (int) ((now - DATE_12_13_START) / MILLIS_WEEK + 1) * -1;
        } else if (now >= DATE_13_SUMMER_START && now < DATE_13_14_START) {
        	weekCount = WEEK_NUMBER_SUMMER;
        } else {
        	weekCount = (int) ((now - DATE_13_14_START) / MILLIS_WEEK + 1);
        }
        
        if (weekCount > WEEK_NUMBER_WINTER) {
        	weekCount = WEEK_NUMBER_WINTER;
        }
        
	    return weekCount;
	}
	
	public static Calendar parseDateAndTime(Date dateFromServer){
		Calendar cal=Calendar.getInstance();
		cal.setTime(dateFromServer);
		return cal;
	}
	
	public static Date parseDateAndTime(String dateStr){
		Date date=new Date();
		try {
			if (dateStr.equals("null")) {
				return null;
			}
			date=serverSourse.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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
	
	@SuppressWarnings("deprecation")
	public static String getEventTime(Context context, Date begin, Date end) {
		Calendar calBegin = parseDateAndTime(begin);
		Calendar calEnd = parseDateAndTime(end);
		Calendar calNow = Calendar.getInstance();
		
		//Now
		if(calNow.after(calBegin) && calNow.before(calEnd)) {
			return context.getResources().getString(R.string.text_now_indicator);
		}
		
		//Today
		if(calNow.get(Calendar.DAY_OF_YEAR) - calBegin.get(Calendar.DAY_OF_YEAR) == 0) {
			String timeBegin = DateUtils.formatDateTime(context, calBegin.getTimeInMillis(),
					DateUtils.FORMAT_24HOUR|DateUtils.FORMAT_SHOW_TIME);
			String timeEnd = DateUtils.formatDateTime(context, calEnd.getTimeInMillis(),
					DateUtils.FORMAT_SHOW_TIME);
			
			StringBuilder sb = new StringBuilder(timeBegin);
			sb.append(" - ").append(timeEnd);
			
			return sb.toString();
		}
		
		//Tomorrow
		if(calBegin.get(Calendar.DAY_OF_YEAR) - calNow.get(Calendar.DAY_OF_YEAR) == 1) {
			StringBuilder sb = 
					new StringBuilder(context.getResources().getString(R.string.text_tomorrow));
			
			String timeBegin = DateUtils.formatDateTime(context, calBegin.getTimeInMillis(),
					DateUtils.FORMAT_24HOUR|DateUtils.FORMAT_SHOW_TIME);
			
			sb.append(" ").append(timeBegin);
			return  sb.toString();
		}
		
		// Other conditions
		String timeBegin = DateUtils.formatDateTime(context, calBegin.getTimeInMillis(),
				DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_24HOUR);
		String timeEnd = DateUtils.formatDateTime(context, calEnd.getTimeInMillis(),
				DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_24HOUR);
	
		Log.v("end time", timeEnd);
		int location = timeEnd.lastIndexOf(" ");
		Log.v("location", "" + location);
		timeEnd = timeEnd.substring(location + 1, timeEnd.length());
		StringBuilder sb = new StringBuilder(timeBegin);
		sb.append(" - ").append(timeEnd);
		return sb.toString();
	}
	
	public static boolean isNow(Date begin, Date end) {
		
		Date now=new Date();
		return (now.after(begin) && now.before(end));
	}

	public static boolean isToday(Date time)
	{
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		
		if((today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) && (today.get(Calendar.DAY_OF_YEAR) 
				== cal.get(Calendar.DAY_OF_YEAR)))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isYesterday(Date time)
	{
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		
		if((today.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) && (today.get(Calendar.DAY_OF_YEAR) 
				== cal.get(Calendar.DAY_OF_YEAR) + 1))
		{
			return true;
		}
		
		return false;
	}
	
	public static String parseDateForInformation(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return format.format(date);
	}
	
	public static String parseDateForNotification(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		StringBuilder sb = new StringBuilder();
		sb.append(cal.get(Calendar.HOUR_OF_DAY)).append(":").append(cal.get(Calendar.MINUTE));
		
		if(isToday(date))
		{
			sb.append(" ").append("Today");
		}else if(isYesterday(date))
		{
			sb.append(" ").append("Yesterday");
		}else
		{
			sb.append(" ").append(cal.get(Calendar.MONTH)).append("-").append(cal.get(Calendar.DAY_OF_MONTH));
		}
		
		return sb.toString();
	}
	
	public static String parseDateFromString(String time, Context context) {
		String result = null;
		Date date = parseDateAndTime(time);
		result = DateUtils.formatDateTime(context, date.getTime(),
				DateUtils.FORMAT_SHOW_DATE);
		return result;
	}
	
	public static Calendar getWeekBegin(int weekNumber) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
        try {
        	if (weekNumber == WEEK_NUMBER_SUMMER) {
        		date = serverSourse.parse(TIME_2013_SUMMER_START);
        	} else if (weekNumber < 0) {
        		date = serverSourse.parse(TIME_12_13_START);
        		date.setTime(date.getTime() - (weekNumber + 1) * MILLIS_WEEK);
        	} else if (weekNumber < 20 && weekNumber > 0) {
        		date = serverSourse.parse(TIME_13_14_START);
        		date.setTime(date.getTime() + (weekNumber - 1) * MILLIS_WEEK);
        	} else {
        		date = serverSourse.parse(TIME_13_14_START);
        		date.setTime(date.getTime() + WEEK_COUNT_13_14 * MILLIS_WEEK);
        	}
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
		return cal;
	}
	
	public static Calendar getWeekEnd(int weekNumber) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
        try {
        	if (weekNumber == WEEK_NUMBER_SUMMER) {
        		date = serverSourse.parse(TIME_2013_SUMMER_START + MILLIS_WEEK * 3);
        	} else if (weekNumber < 0) {
        		date = serverSourse.parse(TIME_12_13_START);
        		date.setTime(date.getTime() - weekNumber * MILLIS_WEEK);
        	} else if (weekNumber < 20 && weekNumber > 0) {
        		date = serverSourse.parse(TIME_13_14_START);
        		date.setTime(date.getTime() + weekNumber * MILLIS_WEEK);
        	} else {
        		date = serverSourse.parse(TIME_13_14_START);
        		date.setTime(date.getTime() + (WEEK_COUNT_13_14 + 4) * MILLIS_WEEK);
        	}
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
		return cal;
	}
}
