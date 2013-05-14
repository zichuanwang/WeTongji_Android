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

public class InformationFactory extends BaseFactory<Information, Integer> {

	public InformationFactory(Fragment fragment) {
		super(fragment, Information.class, WTApplication.INFORMATION_SAVER);
	}
	
	public Pair<Integer, List<Information>> createObjects(String jsonStr, int currentPage) {
		List<Information> result=new ArrayList<Information>();
		int nextPager=0;
		try {
			JSONObject outer=new JSONObject(jsonStr);
			nextPager=outer.getInt("NextPager");
			WTUtility.log("Information List", "Next Page: " + nextPager);
			WTUtility.log("Information List", "Current Page: " + currentPage);
			if(currentPage!=1){
				result=createObjects(outer.getString("Information"),false);
			}
			else{
				result=createObjects(outer.getString("Information"),true);
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
		try {
			array = new JSONArray(jsonStr);
			for(int i=0;i!=array.length();i++){
				Information info=createObject(array.getString(i));
				list.add(info);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle args=new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().initLoader(WTApplication.INFORMATION_SAVER, args, this).forceLoad();
		return list;
	}
	
	private Information createObject(String jsonStr){
		Information info=new Information();
		try {
			JSONObject jsonObject=new JSONObject(jsonStr);
			info.setId(jsonObject.getInt("Id"));
			info.setTitle(jsonObject.getString("Title"));
			info.setContext(jsonObject.getString("Context"));
			info.setSource(jsonObject.getString("Source"));
			info.setSummary(jsonObject.getString("Summary"));
			info.setContact(jsonObject.getString("Contact"));
			info.setLocation(jsonObject.getString("Location"));
			info.setHasTicket(jsonObject.getBoolean("HasTicket"));
			info.setTicketService(jsonObject.getString("TicketService"));
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

}
