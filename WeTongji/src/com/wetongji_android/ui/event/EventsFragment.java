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
		mAdapter = new EventsListAdapter(this);
		mListActivity.setAdapter(mAdapter);
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
			final List<com.wetongji_android.data.Activity> list = mFactory.createObjects(result.getStrResponseCon());
			
			/*getActivity().runOnUiThread(new Runnable() {
				  public void run() {
					  mAdapter.setContentList(list);
					  mAdapter.nextPage();
					  mAdapter.notifyDataSetChanged();
				  }
			});*/
			mAdapter.reloadData();
			
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
}
