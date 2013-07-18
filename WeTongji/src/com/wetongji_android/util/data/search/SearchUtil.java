package com.wetongji_android.util.data.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.SearchResult;
import com.wetongji_android.data.User;

import android.util.Pair;

public class SearchUtil 
{
	@SuppressWarnings("unchecked")
	public static List<Pair<String, List<SearchResult>>> generateSearchResults(String jsonStr)
	{
		List<Pair<String, List<SearchResult>>> re = new ArrayList<Pair<String, List<SearchResult>>>();
		
		try 
		{
			JSONObject json = new JSONObject(jsonStr);
			Iterator<String> keyIterator = (Iterator<String>)json.keys();
			while(keyIterator.hasNext())
			{
				String key = keyIterator.next();
				JSONArray array = json.getJSONArray(key);
				re.add(generateResultPair(key, array));
			}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return re;
	}
	
	private static Pair<String, List<SearchResult>> generateResultPair(String key, JSONArray array) throws JSONException
	{
		return new Pair<String, List<SearchResult>>(key, generateListResult(key, array));
	}
	
	private static List<SearchResult> generateListResult(String key, JSONArray array) throws JSONException
	{
		List<SearchResult> results = new ArrayList<SearchResult>();
		
		if(key.equals("Information"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(1);
				Information info = (Information)array.get(i);
				result.setAvatar(info.getOrganizerAvatar());
				result.setTitle(info.getTitle());
				result.setDesc(info.getSource());
				results.add(result);
			}
		}else if(key.equals("Accounts"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(2);
				Account account = (Account)array.get(i);
				result.setAvatar(account.getImage());
				result.setTitle(account.getTitle());
				result.setDesc(account.getDescription());
				results.add(result);
			}
		}else if(key.equals("Users"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(3);
				User user = (User)array.get(i);
				result.setAvatar(user.getAvatar());
				result.setTitle(user.getName());
				result.setDesc(user.getWords());
				results.add(result);
			}
		}else if(key.equals("Courses"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(4);
				Course course = (Course)array.get(i);
				result.setAvatar(course.getTeacher());
				result.setTitle(course.getTitle());
				result.setDesc(course.getLocation());
				results.add(result);
			}
		}else if(key.equals("Activities"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(5);
				Activity activity = (Activity)array.get(i);
				result.setAvatar(activity.getImage());
				result.setTitle(activity.getTitle());
				result.setDesc(activity.getDescription());
				results.add(result);
			}
		}else
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(6);
				Person person = (Person)array.get(i);
				result.setAvatar(person.getAvatar());
				result.setTitle(person.getTitle());
				result.setDesc(person.getDescription());
				results.add(result);
			}
		}
		
		return results;
	}
}
