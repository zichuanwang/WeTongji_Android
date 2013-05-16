package com.wetongji_android.ui.informations;

import java.util.List;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.InformationList;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.data.information.InformationUtil;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class InformationsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult> 
{	
	private View mView;
	private AmazingListView mListNews;
	private InformationsListAdapter mAdapter;
	private InformationFactory mFactory;
	private LayoutInflater mInflater;
	
	private boolean isFirstTimeToStart = true;
	private final int FIRST_TIME_START = 0; //when activity is first time start
	private final int SCREEN_ROTATE = 1;    //when activity is destroyed and recreated because a configuration change, see setRetainInstance(boolean retain)
	private final int ACTIVITY_DESTROY_AND_CREATE = 2; 
	
	private static final String INFORMATION_LIST = "INFORMATIONS";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		if(container == null)
		{
			return null;
		}else
		{
			mView = inflater.inflate(R.layout.fragment_information, container, false);
		}
		
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mInflater = LayoutInflater.from(getActivity());
		mListNews = (AmazingListView)mView.findViewById(R.id.lst_information);
		mListNews.setPinnedHeaderView(mInflater.inflate(R.layout.information_list_header, mListNews, false));
		mListNews.setAdapter(mAdapter = new InformationsListAdapter(this)); 
		//mListNews.setLoadingView(this.mInflater.inflate(R.layout.amazing_lst_view_loading_view, null));
		
		switch(getCurrentState(savedInstanceState))
		{
		case FIRST_TIME_START:
			mAdapter.loadDataFromDB();
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			InformationList informations = (InformationList)savedInstanceState.getSerializable(
					INFORMATION_LIST);
			mAdapter.setOriginList(informations.getInformations());
			break;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		InformationList informations = new InformationList();
		informations.setInformations(mAdapter.getOriginList());
		outState.putSerializable(INFORMATION_LIST, informations);
	}

	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		getLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
		getLoaderManager().destroyLoader(WTApplication.INFORMATION_LOADER);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) 
	{
		// TODO Auto-generated method stub
		if(result.getResponseCode() != 0)
		{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}else
		{
			if(mFactory == null)
				mFactory = new InformationFactory(this);
			
			int currentPage = mAdapter.getPage();
			Pair<Integer, List<Information>> informations = mFactory.createObjects(result.getStrResponseCon(), currentPage);
			List<Information> lists = informations.second;
			
			WTUtility.log("Infromation Fragment", "list size: " + lists.size());
			mAdapter.setInformations(InformationUtil.getSectionedInformationList(lists));
			mAdapter.setOriginList(lists);
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void refreshData()
	{
		Toast.makeText(getActivity(), "refreshData", Toast.LENGTH_SHORT).show();
		
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getInformations(1, 3, "1,2,3,4");
		this.getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	private int getCurrentState(Bundle savedInstanceState)
	{
		if (savedInstanceState != null) {
            isFirstTimeToStart = false;
            return ACTIVITY_DESTROY_AND_CREATE;
        }

        if (!isFirstTimeToStart) {
            return SCREEN_ROTATE;
        }

        isFirstTimeToStart = false;
        return FIRST_TIME_START;
	}
}