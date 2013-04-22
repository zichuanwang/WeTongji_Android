package com.wetongji_android.ui.event;

import java.util.ArrayList;
import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.ui.widgets.WTXListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventsFragment extends Fragment implements LoaderCallbacks<String>{
	
	private WTXListView listActivity;
	private EventsListAdapter adapter;
	private List<Activity> eventsList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_events, null);		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("mColorRes", mColorRes);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		createEventList(12);
		listActivity = (WTXListView)getActivity().findViewById(R.id.lst_events);
		adapter = new EventsListAdapter(getActivity(), R.layout.row_event,
				R.id.tv_event_title, eventsList);
		listActivity.setAdapter(adapter);
		
	}

	@Override
	public Loader<String> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<String> arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void createEventList(int count) {
		eventsList = new ArrayList<Activity>();
		Activity activity;
		for(int i = 0; i < count; i++) {
			activity = new Activity();
			activity.setTitle("Test Event " + count);
			activity.setLocation("Jiading campus");
			eventsList.add(activity);
		}
		
	}
}
