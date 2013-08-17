/**
 * Information factory used to store information data
 * into the database
 */
package com.wetongji_android.factory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.ui.today.TodayFragment;

public class InformationFactory extends BaseFactory<Information, Integer> 
{
	private int nextPage;
	private Gson gson = new GsonBuilder().
			setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	
	public InformationFactory(Fragment fragment) {
		super(fragment, Information.class, WTApplication.INFORMATION_SAVER);
	}
	
	public Pair<Integer, List<Information>> createObjects(String jsonStr, int currentPage) 
	{
		
		List<Information> result=new ArrayList<Information>();
		int nextPager=0;
		try {
			JSONObject outer=new JSONObject(jsonStr);
			nextPager=outer.getInt("NextPager");
			setNextPage(nextPager);
			if(currentPage!=1)
			{
				result=createObjects(jsonStr,false);
			}
			else
			{
				//If it is the first page or the top 20 informations we need to store
				//into the database
				result=createObjects(jsonStr,true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Pair<Integer, List<Information>>(nextPager, result);
	}

	@Override
	protected List<Information> createObjects(String jsonStr,
			boolean needToRefresh) {
		list.clear();
		JSONArray array;
		JSONObject outer;
		
		try {
			outer=new JSONObject(jsonStr);
			if(outer.has("Like")){
				needToRefresh = false;
				array = outer.getJSONArray("Like");
				for(int j = 0; j != array.length(); j++){
					JSONObject json = array.getJSONObject(j);
					list.add(createObject(json.getJSONObject("ModelDetails").toString()));
				}
			}else{
				array = outer.getJSONArray("Information");
				for(int i = 0; i != array.length(); i++) {
					Information info = createObject(array.getString(i));
					list.add(info);
				}
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if(fragment instanceof TodayFragment)
		{
			
		}else
		{
			if(needToRefresh){
				Bundle args=new Bundle();
				args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
				fragment.getLoaderManager().initLoader(WTApplication.INFORMATION_SAVER, args, this).forceLoad();
			}
		}
		
		return list;
	}
	
	public Information createObject(String jsonStr){
		return gson.fromJson(jsonStr, Information.class);
	}
	
	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
