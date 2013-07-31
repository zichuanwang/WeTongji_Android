package com.wetongji_android.factory;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.SearchHistory;
import com.wetongji_android.util.common.WTApplication;

public class SearchFactory extends BaseFactory<SearchHistory, String> {

	public SearchFactory(Fragment fragment, List<SearchHistory> data) {
		super(fragment, SearchHistory.class, WTApplication.SEARCH_SAVER);
		this.list = data;
	}
	
	public void saveSearch(boolean needToRefresh) {
		Bundle args = new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().restartLoader(loaderId, args, this).forceLoad();
	}

}
