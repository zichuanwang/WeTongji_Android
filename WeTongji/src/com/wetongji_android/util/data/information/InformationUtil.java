package com.wetongji_android.util.data.information;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.util.Pair;

import com.wetongji_android.data.Information;
import com.wetongji_android.util.date.DateParser;

public class InformationUtil 
{
	private static boolean isAtTheSameTime(Information first, Information second)
	{
		Calendar begin = DateParser.parseDateAndTime(first.getCreatedAt());
		Calendar end = DateParser.parseDateAndTime(second.getCreatedAt());
		
		return begin.get(Calendar.YEAR) == end.get(Calendar.YEAR)
				&& begin.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR);
	}
	
	public static List<Pair<Date, List<Information>>> getSectionedInformationList(List<Information> informations)
	{
		List<Pair<Date, List<Information>>> result = new ArrayList<Pair<Date, List<Information>>>();
		
		if(informations == null || informations.isEmpty())
		{
			return null;
		}
		
		int currentPosition = 0;
		int startPosition = 0;
		for(;currentPosition != informations.size(); currentPosition++)
		{
			if(isAtTheSameTime(informations.get(startPosition), informations.get(currentPosition)))
			{
				continue;
			}else
			{
				Pair<Date, List<Information>> pair = new Pair<Date, List<Information>>(informations.get(startPosition).getCreatedAt(),
						informations.subList(startPosition, currentPosition));
				result.add(pair);
				startPosition = currentPosition;
			}
			
		}
		
		Pair<Date, List<Information>> pair = new Pair<Date, List<Information>>(informations.get(startPosition).getCreatedAt(),
				informations.subList(startPosition, currentPosition));
		result.add(pair);
		return result;
	}
}
