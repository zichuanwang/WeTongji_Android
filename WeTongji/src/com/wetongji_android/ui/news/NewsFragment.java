package com.wetongji_android.ui.news;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.factory.InformationFactory;
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

public class NewsFragment extends Fragment implements LoaderCallbacks<HttpRequestResult> 
{	
	private View mView;
	private AmazingListView mListNews;
	private NewsListAdapter mAdapter;
	private InformationFactory mFactory;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getInformations(1, 4, "");
		this.getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
		
		mListNews = (AmazingListView)mView.findViewById(R.id.lst_information);
		mListNews.setAdapter(mAdapter = new NewsListAdapter(this)); 
		TextView text = new TextView(getActivity());
		text.setText("载入");
		mListNews.setLoadingView(text);
	}
	
	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
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
			
			
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
}