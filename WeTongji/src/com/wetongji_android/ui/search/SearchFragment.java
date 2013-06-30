package com.wetongji_android.ui.search;

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
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class SearchFragment extends SherlockFragment 
	implements LoaderCallbacks<HttpRequestResult>{
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
			
			//TODO init widgets
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
		
		inflater.inflate(R.menu.menu_search, menu);
		menu.getItem(0).expandActionView();
		
		final EditText etSearch = (EditText) menu.findItem(
				R.id.menu_search_edit).getActionView();
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String content = etSearch.getText().toString();
					if (!TextUtils.isEmpty(content)) {
						ApiHelper apiHelper = ApiHelper
								.getInstance(getActivity());
						Bundle b = apiHelper.getSearchResult(0, content);
						getLoaderManager().initLoader(
								WTApplication.NETWORK_LOADER_SEARCH, b,
								SearchFragment.this);
					}
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.notification_button_profile) {
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
	
	
}
