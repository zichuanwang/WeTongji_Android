package com.wetongji_android.util.data.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Pair;

import com.wetongji_android.data.Event;
import com.wetongji_android.util.date.DateParser;

public class EventUtil {
	
	public static boolean isNextEvent(Event event){
		Calendar now=Calendar.getInstance();
		Calendar eventBegin=DateParser.parseDateAndTime(event.getBegin());
		Calendar eventEnd=DateParser.parseDateAndTime(event.getEnd());
		if(now.before(eventEnd)&&now.after(eventBegin)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean isPastEvent(Event event){
		Calendar now=Calendar.getInstance();
		Calendar eventEnd=DateParser.parseDateAndTime(event.getEnd());
		if(now.after(eventEnd)){
			return true;
		}
		else{
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getEventDisplayTime(Event event,Context context){
		Calendar eventBegin=DateParser.parseDateAndTime(event.getBegin());
		Calendar eventEnd=DateParser.parseDateAndTime(event.getEnd());
		return DateUtils.formatDateRange(context, eventBegin.getTimeInMillis(),
				eventEnd.getTimeInMillis(),
				DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_24HOUR);
	}
	
	public static List<Pair<String, List<Event>>> getSectionedEventList(List<Event> list){
		List<Pair<String, List<Event>>> result=new ArrayList<Pair<String,List<Event>>>();
		if(list.isEmpty()){
			return result;
		}
		int current=0;
		int start=0;
		for(;current!=list.size();current++){
			if(areEventsBeginInSameDay(list.get(start), list.get(current))){
				continue;
			}
			else{
				Pair<String, List<Event>> pair=
						new Pair<String, List<Event>>(list.get(start).getBegin(),
								list.subList(start, current-1));
				result.add(pair);
				start=current;
			}
		}
		Pair<String, List<Event>> pair=
				new Pair<String, List<Event>>(list.get(start).getBegin(),
						list.subList(start, current));
		result.add(pair);
		return result;
	}
	
	public static boolean areEventsBeginInSameDay(Event a,Event b){
		Calendar aBegin=DateParser.parseDateAndTime(a.getBegin());
		Calendar bBegin=DateParser.parseDateAndTime(b.getBegin());
		return aBegin.get(Calendar.YEAR)==bBegin.get(Calendar.YEAR)&&
				aBegin.get(Calendar.DAY_OF_YEAR)==bBegin.get(Calendar.DAY_OF_YEAR);
	}
	
}
