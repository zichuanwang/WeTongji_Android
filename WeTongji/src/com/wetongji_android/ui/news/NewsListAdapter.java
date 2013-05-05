package com.wetongji_android.ui.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.foound.widget.AmazingAdapter;
import com.wetongji_android.data.Information;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.information.InformationLoader;

public class NewsListAdapter extends AmazingAdapter implements
		LoaderCallbacks<List<Information>> 
{
	private Fragment mFragment;
	private List<Pair<Date, List<Information>>> mListInfos;
	
	public NewsListAdapter(Fragment fragment)
	{
		this.mFragment = fragment;
		this.setNews(new ArrayList<Pair<Date, List<Information>>>());
		this.mFragment.getLoaderManager().initLoader(WTApplication.INFORMATION_LOADER, null, this);
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onNextPageRequested(int page) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindSectionHeader(View view, int position,
			boolean displaySectionHeader) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public View getAmazingView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getPositionForSection(int section) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Loader<List<Information>> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new InformationLoader(this.mFragment.getActivity(), new Bundle());
	}

	@Override
	public void onLoadFinished(Loader<List<Information>> arg0, List<Information> arg1) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<List<Information>> arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	public List<Pair<Date, List<Information>>> getNews() 
	{
		return mListInfos;
	}

	public void setNews(List<Pair<Date, List<Information>>> mListNews) 
	{
		this.mListInfos.clear();
		this.mListInfos = mListNews;
		notifyDataSetChanged();
	}
}