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

import com.google.gson.JsonSyntaxException;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.ui.today.TodayFragment;

public class InformationFactory extends BaseFactory<Information, Integer> 
{
	private int nextPage;
	
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
			WTUtility.log("Information Factory", "Next Page: " + nextPager);
			WTUtility.log("Information Factory", "Current Page: " + currentPage);
			if(currentPage!=1){
				result=createObjects(jsonStr,false);
			}
			else{
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
			array = outer.getJSONArray("Information");
			WTUtility.log("Information Factory", " " + array.length());
			for(int i=0;i!=array.length();i++){
				Information info=createObject(array.getString(i));
				list.add(info);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		WTUtility.log("Information Factory", "" + needToRefresh);
		if(fragment instanceof TodayFragment)
		{
			
		}else
		{
			Bundle args=new Bundle();
			args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
			fragment.getLoaderManager().initLoader(WTApplication.INFORMATION_SAVER, args, this).forceLoad();
		}
		
		return list;
	}
	
	private Information createObject(String jsonStr){
		Information info=new Information();
		try {
			JSONObject jsonObject=new JSONObject(jsonStr);
			info.setId(jsonObject.getInt("Id"));
			info.setTitle(jsonObject.getString("Title"));
			info.setContext(jsonObject.getString("Context"));
			if(jsonObject.has("Source")){
				info.setSource(jsonObject.getString("Source"));
			}
			else{
				info.setSource("");
			}
			if(jsonObject.has("Summary")){
				info.setSummary(jsonObject.getString("Summary"));
			}
			else{
				info.setSummary("");
			}
			if(jsonObject.has("Contact")){
				info.setContact(jsonObject.getString("Contact"));
			}
			else{
				info.setContact("");
			}
			if(jsonObject.has("Location")){
				info.setLocation(jsonObject.getString("Location"));
			}
			else{
				info.setLocation("");
			}
			if(jsonObject.has("HasTicket")&&!jsonObject.isNull("HasTicket")){
				info.setHasTicket(jsonObject.getBoolean("HasTicket"));
			}
			else{
				info.setHasTicket(false);
			}
			if(jsonObject.has("TicketService")){
				info.setTicketService(jsonObject.getString("TicketService"));
			}
			else{
				info.setTicketService("");
			}
			info.setRead(jsonObject.getInt("Read"));
			info.setCreatedAt(DateParser.parseDateAndTime(jsonObject.getString("CreatedAt")));
			info.setCategory(jsonObject.getString("Category"));
			info.setLike(jsonObject.getInt("Like"));
			info.setCanLike(jsonObject.getBoolean("CanLike"));
			info.setOrganizer(jsonObject.getString("Organizer"));
			info.setOrganizerAvatar(jsonObject.getString("OrganizerAvatar"));
			
			JSONArray jsonImage=jsonObject.getJSONArray("Images");
			ArrayList<String> images=new ArrayList<String>();
			for(int i=0;i!=jsonImage.length();i++){
				images.add(jsonImage.getString(i));
			}
			info.setImages(images);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
}
