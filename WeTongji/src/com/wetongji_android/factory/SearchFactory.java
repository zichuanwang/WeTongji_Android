package com.wetongji_android.factory;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.wetongji_android.data.Search;
import com.wetongji_android.util.common.WTApplication;

public class SearchFactory extends BaseFactory<Search, String> {

	public SearchFactory(Fragment fragment, List<Search> data) {
		super(fragment, Search.class, WTApplication.SEARCH_SAVER);
		this.list = data;
	}
	
	public void saveSearch(boolean needToRefresh) {
		Bundle args = new Bundle();
		args.putBoolean(ARG_NEED_TO_REFRESH, needToRefresh);
		fragment.getLoaderManager().restartLoader(loaderId, args, this).forceLoad();
	}

}
