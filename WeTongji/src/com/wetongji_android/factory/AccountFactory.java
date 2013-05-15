package com.wetongji_android.factory;

import com.google.gson.Gson;
import com.wetongji_android.data.Account;

public class AccountFactory {

	private Gson gson=new Gson();
	
	public Account createObjects(String jsonStr){
		Account result=new Account();
		result=gson.fromJson(jsonStr, Account.class);
		return result;
	}
	
}
