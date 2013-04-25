package com.wetongji_android.ui.event;

import java.util.List;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EventsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>{
	
	private AmazingListView mListActivity;
	private EventsListAdapter mAdapter;
	private ActivityFactory mFactory;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_events, null);

		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(1, "", "", false);
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER, args, this);
		
		mListActivity = (AmazingListView) view.findViewById(R.id.lst_events);
		mAdapter = new EventsListAdapter(this);
		mListActivity.setAdapter(mAdapter);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}
	
	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {		
		if(result.getResponseCode()==0){
			if(mFactory==null){
				mFactory=new ActivityFactory(this);
			}
			List<com.wetongji_android.data.Activity> list=mFactory.createObjects(result.getStrResponseCon());
			mAdapter.setContentList(list);
			mAdapter.notifyDataSetChanged();
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
}
