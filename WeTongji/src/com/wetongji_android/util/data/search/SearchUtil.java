package com.wetongji_android.util.data.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wetongji_android.data.SearchResult;

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
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("OrganizerAvatar"));
				result.setTitle(json.getString("Title"));
				result.setDesc(json.getString("Summary"));
				results.add(result);
			}
		}else if(key.equals("Accounts"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(2);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Image"));
				result.setDesc(json.getString("Description"));
				result.setTitle(json.getString("Title"));
				results.add(result);
			}
		}else if(key.equals("Users"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(3);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Avatar"));
				result.setTitle(json.getString("DisplayName"));
				result.setDesc(json.getString("Department"));
				results.add(result);
			}
		}else if(key.equals("Courses"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(4);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Teacher"));
				result.setTitle(json.getString("Name"));
				result.setDesc(json.getString("Teacher"));
				results.add(result);
			}
		}else if(key.equals("Activities"))
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(5);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("OrganizerAvatar"));
				result.setTitle(json.getString("Title"));
				result.setDesc(json.getString("Description"));
				results.add(result);
			}
		}else
		{
			for(int i = 0; i < array.length(); i++)
			{
				SearchResult result = new SearchResult();
				result.setType(6);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Avatar"));
				result.setTitle(json.getString("Title"));
				result.setDesc(json.getString("Words"));
				results.add(result);
			}
		}
		
		return results;
	}
}
