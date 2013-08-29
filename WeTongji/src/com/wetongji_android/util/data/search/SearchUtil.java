package com.wetongji_android.util.data.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.SearchResult;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;

import android.util.Pair;

public class SearchUtil {
	@SuppressWarnings("unchecked")
	public static List<Pair<String, List<SearchResult>>> generateSearchResults(
			String jsonStr) {
		List<Pair<String, List<SearchResult>>> re = new ArrayList<Pair<String, List<SearchResult>>>();

		try {
			JSONObject json = new JSONObject(jsonStr);
			Iterator<String> keyIterator = (Iterator<String>) json.keys();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				JSONArray array = json.getJSONArray(key);
				re.add(generateResultPair(key, array));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	private static Pair<String, List<SearchResult>> generateResultPair(
			String key, JSONArray array) throws JSONException {
		return new Pair<String, List<SearchResult>>(key, generateListResult(
				key, array));
	}

	private static List<SearchResult> generateListResult(String key,
			JSONArray array) throws JSONException {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		List<SearchResult> results = new ArrayList<SearchResult>();

		if (key.equals("Information")) {
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(1);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("OrganizerAvatar"));
				result.setTitle(json.getString("Title"));
				Information info = gson.fromJson(json.toString(),
						Information.class);
				result.setContent(info);
				results.add(result);
			}
		} else if (key.equals("Accounts")) {
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(2);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Image"));
				result.setDesc(json.getString("Description"));
				result.setTitle(json.getString("Title"));
				Account account = gson.fromJson(json.toString(), Account.class);
				result.setContent(account);
				results.add(result);
			}
		// **** Only Login User can search user
		} else if (key.equals("Users") && WTApplication.getInstance().hasAccount) {
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(3);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Avatar"));
				result.setTitle(json.getString("DisplayName"));
				result.setDesc(json.getString("Department"));
				User user = gson.fromJson(json.toString(), User.class);
				result.setContent(user);
				results.add(result);
			}
		} /*else if (key.equals("Courses")) {
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(4);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("Teacher"));
				result.setTitle(json.getString("Name"));
				result.setDesc(json.getString("Teacher"));
				Course course = gson.fromJson(json.toString(), Course.class);
				result.setContent(course);
				results.add(result);
			}
		}*/ else if (key.equals("Activities")) {
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(5);
				JSONObject json = array.getJSONObject(i);
				result.setAvatar(json.getString("OrganizerAvatar"));
				result.setTitle(json.getString("Title"));
				result.setDesc(json.getString("Description"));
				Activity activity = gson.fromJson(json.toString(),
						Activity.class);
				result.setContent(activity);
				results.add(result);
			}
		} else if (key.equals("Person")) {
			// Stars
			for (int i = 0; i < array.length(); i++) {
				SearchResult result = new SearchResult();
				result.setType(6);
				JSONObject json = array.getJSONObject(i);
				Person person = gson.fromJson(json.toString(), Person.class);
				result.setContent(person);
				results.add(result);
			}
		}

		return results;
	}
}
