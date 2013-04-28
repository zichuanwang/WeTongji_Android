package com.wetongji_android.ui.event;

import java.util.List;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.factory.ActivityFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class EventsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>{
	
	public AmazingListView mListActivity;
	private EventsListAdapter mAdapter;
	private ActivityFactory mFactory;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_events, null);

		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getActivities(1, 15, ApiHelper.API_ARGS_SORT_BY_LIKE_DESC, false);
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER, args, this);
		
		mListActivity = (AmazingListView) view.findViewById(R.id.lst_events);
		mListActivity.noMorePages();
		mAdapter = new EventsListAdapter(this);
		mListActivity.setAdapter(mAdapter);
		mListActivity.setOnItemClickListener(onItemClickListener);
		//mListActivity.setLoadingView(inflater.inflate(R.layout.amazing_lst_view_loading_view, null));
		TextView text = new TextView(getActivity());
		text.setText("‘ÿ»Î");
		mListActivity.setLoadingView(text);
		mAdapter.notifyMayHaveMorePages();
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public Loader<HttpRequestResult> onCreateLoader(int id, Bundle args) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, args);
	}
	
	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {		
		if(result.getResponseCode()==0){
			if(mFactory==null){
				mFactory=new ActivityFactory(this);
			}
			int currentPage=mAdapter.getPage();
			Pair<Integer, List<Activity>> pair=
					mFactory.createObjects(result.getStrResponseCon(), currentPage);
			List<Activity> activities=pair.second;
			mAdapter.nextPage();
			if(currentPage==1){
				mListActivity.mayHaveMorePages();
				mAdapter.setContentList(activities);
			}
			if(pair.first==0){
				mListActivity.noMorePages();
			}
			else{
				mListActivity.mayHaveMorePages();
				mAdapter.addData(activities);
			}
			mAdapter.notifyDataSetChanged();
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			startActivity(new Intent(getActivity(), EventDetailActivity.class));
		}
		
	};
	
}
