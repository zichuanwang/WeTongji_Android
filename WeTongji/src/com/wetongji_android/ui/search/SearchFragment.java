package com.wetongji_android.ui.search;

import java.sql.SQLException;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.R;
import com.wetongji_android.data.Search;
import com.wetongji_android.factory.SearchFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class SearchFragment extends SherlockFragment 
	implements LoaderCallbacks<HttpRequestResult>{
	
	private SearchHistoryAdapter mAdapter;
	private ListView mLvSearchHistory;
	private ClearHistoryTask mClearTask;
	private EditText mEtSearch;
	
	public static SearchFragment newInstance() {
		SearchFragment f = new SearchFragment();
		return f;
	}
	
	public SearchFragment() {
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, container, false);
		
		mLvSearchHistory = (ListView) view.findViewById(R.id.lst_search_history);
		mAdapter = new SearchHistoryAdapter(this);
		mLvSearchHistory.setAdapter(mAdapter);
		
		mClearTask = new ClearHistoryTask();
		
		return view;
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (mClearTask != null) {
			mClearTask.cancel(true);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
		
		inflater.inflate(R.menu.menu_search, menu);
		menu.getItem(0).expandActionView();
		
		mEtSearch = (EditText) menu.findItem(
				R.id.menu_search_edit).getActionView();
		mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String content = mEtSearch.getText().toString();
					if (!TextUtils.isEmpty(content)) {
						ApiHelper apiHelper = ApiHelper
								.getInstance(getActivity());
						Bundle b = apiHelper.getSearchResult(0, content);
						getLoaderManager().initLoader(
								WTApplication.NETWORK_LOADER_SEARCH, b,
								SearchFragment.this);
						//TODO change type
						saveSearchHistory(0, content);
					}
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.notification_button_search) {
			if (WTApplication.getInstance().hasAccount) {
				((MainActivity) getActivity()).showRightMenu();
			} else {
				Toast.makeText(getActivity(),
						getResources().getText(R.string.no_account_error),
						Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loaderId, Bundle bundle) {
		Loader<HttpRequestResult> loader = null;
		if (loaderId == WTApplication.NETWORK_LOADER_SEARCH) {
			loader = new NetworkLoader(getActivity(), HttpMethod.Get, bundle);
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		if (loader.getId() == WTApplication.NETWORK_LOADER_SEARCH) {
			Log.d("data", result.getStrResponseCon());
		}
		
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
	private void saveSearchHistory(int type, String keywords) {
		Search search = new Search();
		search.setType(type);
		search.setKeywords(keywords);
		
		if (mAdapter != null) {
			mAdapter.addObject(search);
		}
		
		List<Search> history = mAdapter.getData();
		SearchFactory searchFactory = new SearchFactory(this, history);
		searchFactory.saveSearch(true);
	}
	
	private class ClearHistoryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			DbHelper dbHelper = WTApplication.getInstance().getDbHelper();
			
			try {
				TableUtils.clearTable(dbHelper.getConnectionSource(), Search.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			//TODO clear data in search history listView
		}
		
		
		
	}
	
}
