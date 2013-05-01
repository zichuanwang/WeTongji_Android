package com.wetongji_android.util.data.course;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseUtil {
	
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
	
	public static Date parseCourseStartTime(String strDate, int section){
		SimpleDateFormat serverSourse=new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		Date result=new Date();
		try {
			result=serverSourse.parse(strDate);
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(result);
			switch(section){
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
			result=calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Date parseCourseEndTime(String strDate, int section){
		SimpleDateFormat serverSourse=new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		Date result=new Date();
		try {
			result=serverSourse.parse(strDate);
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(result);
			switch(section){
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
			result=calendar.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
		
}
