package com.wetongji_android.ui.event;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wetongji_android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventsFragment extends Fragment {
	
	private PullToRefreshListView lvEvents;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_events, null);
		initView(view);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//outState.putInt("mColorRes", mColorRes);
	}
	
	private void initView(View view){
		lvEvents=(PullToRefreshListView) view.findViewById(R.id.lv_events);
	}
}
