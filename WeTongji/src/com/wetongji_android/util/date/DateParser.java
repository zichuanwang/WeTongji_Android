package com.wetongji_android.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.text.format.DateUtils;

/**
 * 解析服务器返回日期和时间的工具类
 * @author John
 *
 */
public class DateParser {
	
	private static SimpleDateFormat format=new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	private static SimpleDateFormat targetFormat=new SimpleDateFormat("yyyy-MM-dd'%'");
	
	private static final int activityDateFlag=DateUtils.FORMAT_SHOW_DATE|
			DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_SHOW_WEEKDAY|
			DateUtils.FORMAT_ABBREV_ALL|DateUtils.FORMAT_24HOUR;
	private static final int courseSection1StartHour=8;
	private static final int courseSection1StartMinute=0;
	private static final int courseSection2StartHour=8;
	private static final int courseSection2StartMinute=55;
	private static final int courseSection3StartHour=10;
	private static final int courseSection3StartMinute=0;
	private static final int courseSection4StartHour=10;
	private static final int courseSection4StartMinute=55;
	private static final int courseSection5StartHour=13;
	private static final int courseSection5StartMinute=30;
	private static final int courseSection6StartHour=14;
	private static final int courseSection6StartMinute=20;
	private static final int courseSection7StartHour=15;
	private static final int courseSection7StartMinute=25;
	private static final int courseSection8StartHour=16;
	private static final int courseSection8StartMinute=15;
	private static final int courseSection9StartHour=18;
	private static final int courseSection9StartMinute=30;
	private static final int courseSection10StartHour=19;
	private static final int courseSection10StartMinute=25;
	private static final int courseSection11StartHour=20;
	private static final int courseSection11StartMinute=20;
	private static final int courseSection1EndHour=8;
	private static final int courseSection1EndMinute=45;
	private static final int courseSection2EndHour=9;
	private static final int courseSection2EndMinute=40;
	private static final int courseSection3EndHour=10;
	private static final int courseSection3EndMinute=45;
	private static final int courseSection4EndHour=11;
	private static final int courseSection4EndMinute=40;
	private static final int courseSection5EndHour=14;
	private static final int courseSection5EndMinute=15;
	private static final int courseSection6EndHour=15;
	private static final int courseSection6EndMinute=05;
	private static final int courseSection7EndHour=16;
	private static final int courseSection7EndMinute=10;
	private static final int courseSection8EndHour=17;
	private static final int courseSection8EndMinute=0;
	private static final int courseSection9EndHour=19;
	private static final int courseSection9EndMinute=15;
	private static final int courseSection10EndHour=20;
	private static final int courseSection10EndMinute=10;
	private static final int courseSection11EndHour=21;
	private static final int courseSection11EndMinute=05;
	
	/**
     * 解析日期
     * @param jsonString 服务器返回的日期
     * @return 存入数据库的日期
	 * @throws ParseException 
     */
	public static String parseDateAndTime(String strDateSource) {
		
    	Date date=new Date();
		try {
			date=format.parse(strDateSource);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
    	return "发表于 " + sdf.format(date);
	}
	
	public static String parseTime(String strDateSource) {
		Date date=new Date();
		try {
			date=format.parse(strDateSource);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat targetFormat=new SimpleDateFormat("HH:mm");
		
		return targetFormat.format(date);
	}
	
	public static String parseBeginAndEndTime(String begin,String end){
		
		String result="";
		
		try {
			Date dateBegin=format.parse(begin);
			Date dateEnd=format.parse(end);
			result=DateUtils.formatDateRange(null, dateBegin.getTime(),
					dateEnd.getTime(), activityDateFlag);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public static long getCurrentTime(){
		Date date=new Date();
		long unixTime=date.getTime();
		return unixTime;
	}
	
	public static String buildTodayForReminder(){
		Date date=new Date();
		date.setTime(getCurrentTime());
		return targetFormat.format(date);
	}
	
	public static String buildTomorrowForReminder(){
		Date date=new Date();
		date.setTime(getCurrentTime()+86400000);
		return targetFormat.format(date);
	}
	
	public static String buildDateForSchedule(int year, int month, int day){
		Calendar canlendar=Calendar.getInstance();
		canlendar.set(year, month, day);
		return targetFormat.format(canlendar.getTime());
	}
	
	public static String buildTodayForReminderApi(){
		Calendar calendar=Calendar.getInstance();
		String result="";
		result+=calendar.get(Calendar.YEAR);
		if((calendar.get(Calendar.MONTH)+1)<10)
			result+="0"+(calendar.get(Calendar.MONTH)+1);
		else
			result+=(calendar.get(Calendar.MONTH)+1);
		if((calendar.get(Calendar.DAY_OF_MONTH))<10)
			result+="0"+calendar.get(Calendar.DAY_OF_MONTH);
		else
			result+=calendar.get(Calendar.DAY_OF_MONTH);
		return result;
	}
	
	public static String buildTomorrowForReminderApi(){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		String result="";
		result+=calendar.get(Calendar.YEAR);
		if((calendar.get(Calendar.MONTH)+1)<10)
			result+="0"+(calendar.get(Calendar.MONTH)+1);
		else
			result+=(calendar.get(Calendar.MONTH)+1);
		if((calendar.get(Calendar.DAY_OF_MONTH))<10)
			result+="0"+calendar.get(Calendar.DAY_OF_MONTH);
		else
			result+=calendar.get(Calendar.DAY_OF_MONTH);
		return result;
	}
	
	public static String buildSchoolYearStartForScheduleApi(String schoolYearStartAt){
		Date date=new Date();
		try {
			date=format.parse(schoolYearStartAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		String result="";
		result+=calendar.get(Calendar.YEAR);
		if((calendar.get(Calendar.MONTH)+1)<10)
			result+="0"+(calendar.get(Calendar.MONTH)+1);
		else
			result+=(calendar.get(Calendar.MONTH)+1);
		if((calendar.get(Calendar.DAY_OF_MONTH))<10)
			result+="0"+calendar.get(Calendar.DAY_OF_MONTH);
		else
			result+=calendar.get(Calendar.DAY_OF_MONTH);
		return result;
	}
	
	public static String buildSchoolYearEndForScheduleApi(String schoolYearStartAt,
			int schoolYearWeekCount){
		Date date=new Date();
		try {
			date=format.parse(schoolYearStartAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, schoolYearWeekCount);
		String result="";
		result+=calendar.get(Calendar.YEAR);
		if((calendar.get(Calendar.MONTH)+1)<10)
			result+="0"+(calendar.get(Calendar.MONTH)+1);
		else
			result+=(calendar.get(Calendar.MONTH)+1);
		if((calendar.get(Calendar.DAY_OF_MONTH))<10)
			result+="0"+calendar.get(Calendar.DAY_OF_MONTH);
		else
			result+=calendar.get(Calendar.DAY_OF_MONTH);
		return result;
	}
	
	public static String buildCurrentDateAndTime(){
		return buildDateAndTime(getCurrentTime());
	}
	
	public static String buildDateAndTime(long unixDate){
		Date date=new Date();
		date.setTime(unixDate);
		
		return format.format(date);
	}
	
	public static Calendar parseCourseStartDate(String schoolYearStartAt, 
			String courseWeekDay, String courseWeekType){
		
		Date date=new Date();
		try {
			date=format.parse(schoolYearStartAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		Calendar calendarStart=Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendarStart.setFirstDayOfWeek(Calendar.MONDAY);
		calendarStart.setTime(date);
		int weekDayStart=calendarStart.get(Calendar.DAY_OF_WEEK);
		int dayInWeekGap=0;
		if(courseWeekDay.equals("星期一"))
			dayInWeekGap=Calendar.MONDAY-weekDayStart;
		else if(courseWeekDay.equals("星期二"))
			dayInWeekGap=Calendar.TUESDAY-weekDayStart;
		else if(courseWeekDay.equals("星期三"))
			dayInWeekGap=Calendar.WEDNESDAY-weekDayStart;
		else if(courseWeekDay.equals("星期四"))
			dayInWeekGap=Calendar.THURSDAY-weekDayStart;
		else if(courseWeekDay.equals("星期五"))
			dayInWeekGap=Calendar.FRIDAY-weekDayStart;
		else if(courseWeekDay.equals("星期六"))
			dayInWeekGap=Calendar.SATURDAY-weekDayStart;
		else if(courseWeekDay.equals("星期日"))
			dayInWeekGap=Calendar.SUNDAY-weekDayStart;
		
		calendar.add(Calendar.DAY_OF_WEEK, dayInWeekGap);
		if(calendar.before(calendarStart))
			calendar.add(Calendar.DAY_OF_MONTH, 7);
		if(courseWeekType.equals("双"))
			calendar.add(Calendar.DAY_OF_MONTH, 7);
		
		return calendar;
	}
	
	public static Calendar parseCourseStartTime(int courseSectionStart){
		
		Calendar calendar=Calendar.getInstance();
		
		switch(courseSectionStart){
		case 1:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection1StartHour);
			calendar.set(Calendar.MINUTE, courseSection1StartMinute);
			break;
		case 2:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection2StartHour);
			calendar.set(Calendar.MINUTE, courseSection2StartMinute);
			break;
		case 3:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection3StartHour);
			calendar.set(Calendar.MINUTE, courseSection3StartMinute);
			break;
		case 4:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection4StartHour);
			calendar.set(Calendar.MINUTE, courseSection4StartMinute);
			break;
		case 5:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection5StartHour);
			calendar.set(Calendar.MINUTE, courseSection5StartMinute);
			break;
		case 6:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection6StartHour);
			calendar.set(Calendar.MINUTE, courseSection6StartMinute);
			break;
		case 7:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection7StartHour);
			calendar.set(Calendar.MINUTE, courseSection7StartMinute);
			break;
		case 8:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection8StartHour);
			calendar.set(Calendar.MINUTE, courseSection8StartMinute);
			break;
		case 9:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection9StartHour);
			calendar.set(Calendar.MINUTE, courseSection9StartMinute);
			break;
		case 10:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection10StartHour);
			calendar.set(Calendar.MINUTE, courseSection10StartMinute);
			break;
		case 11:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection11StartHour);
			calendar.set(Calendar.MINUTE, courseSection11StartMinute);
			break;
		}
		calendar.set(Calendar.SECOND, 0);
		
		return calendar;
	}
	
	public static Calendar parseCourseEndDate(Calendar courseStartAt, 
			String courseFrenquency, int schoolYearCourseWeekCount){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(courseStartAt.getTime());
		calendar.add(Calendar.WEEK_OF_YEAR, schoolYearCourseWeekCount-1);
		if(!courseFrenquency.equals("全"))
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
		
		return calendar;
	}
	
	public static Calendar parseCourseEndTime(int courseSectionEnd){
		Calendar calendar=Calendar.getInstance();
		
		switch(courseSectionEnd){
		case 1:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection1EndHour);
			calendar.set(Calendar.MINUTE, courseSection1EndMinute);
			break;
		case 2:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection2EndHour);
			calendar.set(Calendar.MINUTE, courseSection2EndMinute);
			break;
		case 3:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection3EndHour);
			calendar.set(Calendar.MINUTE, courseSection3EndMinute);
			break;
		case 4:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection4EndHour);
			calendar.set(Calendar.MINUTE, courseSection4EndMinute);
			break;
		case 5:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection5EndHour);
			calendar.set(Calendar.MINUTE, courseSection5EndMinute);
			break;
		case 6:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection6EndHour);
			calendar.set(Calendar.MINUTE, courseSection6EndMinute);
			break;
		case 7:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection7EndHour);
			calendar.set(Calendar.MINUTE, courseSection7EndMinute);
			break;
		case 8:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection8EndHour);
			calendar.set(Calendar.MINUTE, courseSection8EndMinute);
			break;
		case 9:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection9EndHour);
			calendar.set(Calendar.MINUTE, courseSection9EndMinute);
			break;
		case 10:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection10EndHour);
			calendar.set(Calendar.MINUTE, courseSection10EndMinute);
			break;
		case 11:
			calendar.set(Calendar.HOUR_OF_DAY, courseSection11EndHour);
			calendar.set(Calendar.MINUTE, courseSection11EndMinute);
			break;
		}
		calendar.set(Calendar.SECOND, 0);
		
		return calendar;
	}
	
	public static int parseCourseFrenquency(String strCourseFrenquency){
		if(strCourseFrenquency.equals("全"))
			return 1;
		else if(strCourseFrenquency.equals("单")||strCourseFrenquency.equals("双"))
			return 2;
		else
			return 0;
	}
	
	/*public static List<String> parseCourseDateAndTime(Course course, 
			String schoolYearStartAt, int schoolYearCourseWeekCount){
		Calendar startCalendar=parseCourseStartDate(schoolYearStartAt, 
				course.getWeekDay(),course.getWeekType());
		Calendar endCalendar=parseCourseEndDate(startCalendar, course.getWeekType(), 
				schoolYearCourseWeekCount);
		int frenquency=parseCourseFrenquency(course.getWeekType());
		Date startDate=startCalendar.getTime();
		Calendar calendar=Calendar.getInstance();
		
		List<String> result=new ArrayList<String>();
		int courseStartTimeHour=parseCourseStartTime(course.getSectionStart())
				.get(Calendar.HOUR_OF_DAY);
		int courseStartTimeMinute=parseCourseStartTime(course.getSectionStart())
				.get(Calendar.MINUTE);
		int courseEndTimeHour=parseCourseEndTime(course.getSectionEnd())
				.get(Calendar.HOUR_OF_DAY);
		int courseEndTimeMinute=parseCourseEndTime(course.getSectionEnd())
				.get(Calendar.MINUTE);
		
		for(calendar.setTime(startDate);calendar.before(endCalendar);
				calendar.add(Calendar.WEEK_OF_YEAR, frenquency)){
			calendar.set(Calendar.HOUR_OF_DAY, courseStartTimeHour);
			calendar.set(Calendar.MINUTE, courseStartTimeMinute);
			startDate=calendar.getTime();
			result.add(format.format(startDate));
			calendar.set(Calendar.HOUR_OF_DAY, courseEndTimeHour);
			calendar.set(Calendar.MINUTE, courseEndTimeMinute);
			startDate=calendar.getTime();
			result.add(format.format(startDate));
		}
		calendar=(Calendar) endCalendar.clone();
		calendar.set(Calendar.HOUR_OF_DAY, courseStartTimeHour);
		calendar.set(Calendar.MINUTE, courseStartTimeMinute);
		startDate=calendar.getTime();
		result.add(format.format(startDate));
		calendar.set(Calendar.HOUR_OF_DAY, courseEndTimeHour);
		calendar.set(Calendar.MINUTE, courseEndTimeMinute);
		startDate=calendar.getTime();
		result.add(format.format(startDate));
		
		return result;
	}*/
	
	public static String parseWeekDayAccordingToDate(String dateStr)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String weekStr = "";
		
		try 
		{
			Date date = sdf.parse(dateStr);
			SimpleDateFormat format = new SimpleDateFormat("E");
			weekStr = format.format(date);
		} catch (ParseException e) 
		{
			e.printStackTrace();
			weekStr = dateStr;
		}
		
		return weekStr;
	}
	
	public static String parseWeekDayToChinese(String dateStr)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = null;
		
		try 
		{
			Date date = sdf.parse(dateStr);
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			str = format.format(date);
		} catch (ParseException e) 
		{
			e.printStackTrace();
			str = dateStr;
		}
		
		return str;
	}
	
	public static boolean isCurrentDateAfterSchoolYear(String schoolYearStartAt,
			int schoolYearWeekCount){
		if(schoolYearStartAt.equals(""))
			return true;
		Date date=new Date();
		try {
			date=format.parse(schoolYearStartAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_YEAR, schoolYearWeekCount);
		Calendar CalendarNow=Calendar.getInstance();
		return !CalendarNow.before(calendar);
	}
	
	public static int getCurrentWeekNumber(String schoolYearStartAt) {
	    int weekCount = 0;
	    if(schoolYearStartAt.equals(""))
            return 0; 
	    
	    Date date=new Date();
        try {
            date=format.parse(schoolYearStartAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
	    
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        
        Calendar CalendarNow=Calendar.getInstance();
        
        long start = calendar.getTimeInMillis();
        long now = CalendarNow.getTimeInMillis();
        
        if(start > now)
            return 0;
        else
            weekCount = (int)((now - start) / (1000 * 60 * 60 * 24 * 7) + 1);
        
	    return weekCount;
	}
	
}
