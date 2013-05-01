package com.wetongji_android.util.data.exam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExamUtil {

	public static Date parseExamDate(String strDate){
		SimpleDateFormat serverSourse=new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		Date result=new Date();
		try {
			result=serverSourse.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
