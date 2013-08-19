package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wetongji_android.data.Account;

public class AccountFactory {

	private Gson gson=new Gson();
	private int nextPage;
	
	public Account createObject(String jsonStr) {
		Account result=new Account();
		result=gson.fromJson(jsonStr, Account.class);
		return result;
	}
	
	public List<Account> createObjects(String jsonStr) {
		List<Account> accounts = new ArrayList<Account>();
		
		try 
		{
			JSONObject json = new JSONObject(jsonStr);
			nextPage = json.getInt("NextPager");
			JSONArray likes = json.getJSONArray("Like");
			for(int i = 0; i < likes.length(); i++) {
				accounts.add(createObject(likes.getJSONObject(i).getJSONObject("ModelDetails").toString()));
			}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return accounts;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
