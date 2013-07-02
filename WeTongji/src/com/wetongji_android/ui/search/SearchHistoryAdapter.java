package com.wetongji_android.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Search;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.loader.SearchLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchHistoryAdapter extends BaseAdapter implements
		LoaderCallbacks<List<Search>> {

	private static final int MAX_SIZE = 9;
	private Fragment mFragment;
	private List<Search> mData;
	private int hasClearRow = 1;

	public SearchHistoryAdapter(Fragment fragment) {
		mFragment = fragment;
		mData = new ArrayList<Search>();
		mFragment.getLoaderManager().initLoader(WTApplication.SEARCH_LOADER,
				null, this);
	}

	static class ViewHolder {
		TextView tvType;
		TextView tvKeywords;
		LinearLayout llRow;
	}

	@Override
	public Loader<List<Search>> onCreateLoader(int loaderId, Bundle bundle) {
		if (loaderId != WTApplication.SEARCH_LOADER) {
			return null;
		}
		Loader<List<Search>> loader;
		loader = new SearchLoader(mFragment.getActivity());
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<List<Search>> loader, List<Search> result) {
		mData.addAll(result);
		notifyDataSetChanged();
		mFragment.getLoaderManager().destroyLoader(WTApplication.SEARCH_LOADER);
	}

	@Override
	public void onLoaderReset(Loader<List<Search>> result) {
	}

	@Override
	public int getCount() {
		return mData.size() + hasClearRow;
	}

	@Override
	public Object getItem(int arg0) {
		if (arg0 != mData.size()) {
			return mData.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == mData.size()) {
			LayoutInflater layoutnflater1 = LayoutInflater.from(mFragment
					.getActivity());
			View view = layoutnflater1.inflate(R.layout.row_clear_search_history,
					parent, false);
			LinearLayout llRow = (LinearLayout) view
					.findViewById(R.id.layout_search_row);
			if (position % 2 != 0) {
				llRow.setBackgroundColor(mFragment.getActivity().
						getResources().getColor(R.color.layout_event_list_row1));
			} else {
				llRow.setBackgroundColor(mFragment.getActivity().
						getResources().getColor(R.color.layout_event_list_row2));
			}
			return view;
		}
		Search search = (Search) getItem(position);
		ViewHolder holder = null;
		
		holder = new ViewHolder();
		LayoutInflater layoutnflater2 = LayoutInflater.from(mFragment
				.getActivity());
		View view = layoutnflater2.inflate(R.layout.row_search_history,
				parent, false);
		holder.tvType = (TextView) view
				.findViewById(R.id.text_search_type);
		holder.tvKeywords = (TextView) view
				.findViewById(R.id.text_search_keywords);
		holder.llRow = (LinearLayout) view
				.findViewById(R.id.layout_search_row);
		view.setTag(holder);
		
		
		holder.tvKeywords.setText(search.getKeywords());
		String searchType = null;;
		switch (search.getType()) {
		case 0:
			searchType = mFragment.getString(R.string.search_type_all);
			break;
		case 4:
			searchType = mFragment.getString(R.string.search_type_information);
			break;
		case 2:
			searchType = mFragment.getString(R.string.search_type_organizations);
			break;
		case 1:
			searchType = mFragment.getString(R.string.search_type_people);
			break;
		case 3:
			searchType = mFragment.getString(R.string.search_type_activities);
			break;
		case 5:
			searchType = mFragment.getString(R.string.search_type_stars);
			break;
		}
		holder.tvType.setText(searchType);
		
		if (position % 2 != 0) {
			holder.llRow.setBackgroundColor(mFragment.getActivity().
					getResources().getColor(R.color.layout_event_list_row1));
		} else {
			holder.llRow.setBackgroundColor(mFragment.getActivity().
					getResources().getColor(R.color.layout_event_list_row2));
		}
		return view;
	}
	
	public void addObject(Search search) {
		if (mData.contains(search)) {
			return;
		}
		
		if ((mData.size() + 1) > MAX_SIZE) {
			mData.remove(MAX_SIZE - 1);
		}
		hasClearRow = 1;
		mData.add(0, search);
		notifyDataSetChanged();
	}

	public List<Search> getData() {
		return mData;
	}
	
	public void clearHistory() {
		hasClearRow = 0;
		mData.clear();
		notifyDataSetChanged();
	}
}
