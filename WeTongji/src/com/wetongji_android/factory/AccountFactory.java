package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.wetongji_android.data.Account;

public class AccountFactory {

	private Gson gson=new Gson();
	
	public Account createObject(String jsonStr){
		Account result=new Account();
		result=gson.fromJson(jsonStr, Account.class);
		return result;
	}
	
	public List<Account> createObjects(String jsonStr){
		List<Account> accounts = new ArrayList<Account>();
		
		try 
		{
			JSONArray array = new JSONArray(jsonStr);
			for(int i = 0; i < array.length(); i++){
				Account account = createObject(array.getJSONObject(i).toString());
				accounts.add(account);
			}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return accounts;
	}
}
