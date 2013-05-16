package com.wetongji_android.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.wetongji_android.R;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * 解析服务器返回日期和时间的工具类
 * @author John
 *
 */
public class DateParser {
	
	private static SimpleDateFormat serverSourse=new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
	private static SimpleDateFormat serverTarget=new SimpleDateFormat(
			"yyyy-MM-dd", Locale.US);
	
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
	
	public static Calendar parseDateAndTime(Date dateFromServer){
		Calendar cal=Calendar.getInstance();
		cal.setTime(dateFromServer);
		return cal;
	}
	
	public static Date parseDateAndTime(String dateStr){
		Date date=new Date();
		try {
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
					DateUtils.FORMAT_24HOUR|DateUtils.FORMAT_SHOW_TIME);
			
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
	
	public static String parseDateForInformation(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return buildDateAndTime(cal);
	}
}
