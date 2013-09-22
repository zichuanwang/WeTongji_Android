package com.wetongji_android.ui.search;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.foound.widget.AmazingListView;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.SearchHistory;
import com.wetongji_android.data.SearchResult;
import com.wetongji_android.data.User;
import com.wetongji_android.factory.SearchFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.account.AccountDetailActivity;
import com.wetongji_android.ui.course.CourseDetailActivity;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.friend.FriendDetailActivity;
import com.wetongji_android.ui.friend.FriendListFragment;
import com.wetongji_android.ui.informations.InformationDetailActivity;
import com.wetongji_android.ui.informations.InformationsFragment;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.people.PeopleListFragment;
import com.wetongji_android.ui.people.PersonDetailActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbHelper;
import com.wetongji_android.util.data.search.SearchUtil;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class SearchFragment extends SherlockFragment implements
		LoaderCallbacks<HttpRequestResult>, OnQueryTextListener {

	private SearchHistoryAdapter mAdapter;
	private SearchTipsAdapter mTipAdapter;
	private SearchResultAdapter mResultAdapter;
	private ListView mLvSearchHistory;
	private ListView mLvSearchTips;
	private AmazingListView mLvSearchResult;
	private ClearHistoryTask mClearTask;
	private TextView mTvHistoryTitle;
	private ProgressBar mProgressBar;
	private TextView mTvNoResult;
	private SearchView mSearchView;

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
		getSherlockActivity().getSupportActionBar().setTitle(
				R.string.title_mainmenu_search);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_search, container, false);

		mTvNoResult = (TextView) view.findViewById(R.id.text_search_no_result);
		mProgressBar = (ProgressBar) view.findViewById(R.id.pb_search);
		mTvHistoryTitle = (TextView) view
				.findViewById(R.id.text_search_history_title);
		mLvSearchHistory = (ListView) view
				.findViewById(R.id.lst_search_history);
		mLvSearchTips = (ListView) view.findViewById(R.id.lst_search_tips);
		mLvSearchResult = (AmazingListView) view
				.findViewById(R.id.lst_search_result);
		mResultAdapter = new SearchResultAdapter(this);
		mLvSearchResult.setAdapter(mResultAdapter);
		mLvSearchResult.setPinnedHeaderView(inflater.inflate(
				R.layout.row_information_header, mLvSearchResult, false));
		mLvSearchResult.setOnItemClickListener(mOnResultClickListener);
		mAdapter = new SearchHistoryAdapter(this);
		mLvSearchHistory.setAdapter(mAdapter);

		mLvSearchHistory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (position == mAdapter.getCount() - 1) {
					startClearTask();
				} else {
					showProgress();
					SearchHistory search = (SearchHistory) arg0
							.getItemAtPosition(position);
					doSearch(search.getType(), search.getKeywords());
					mSearchView.setQuery(search.getKeywords(), false);
				}
			}
		});

		mLvSearchTips.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				int type = 0;
				type = position;

				doSearch(type, mTipAdapter.getmKeywords());
			}
		});

		mTipAdapter = new SearchTipsAdapter(this);
		mLvSearchTips.setAdapter(mTipAdapter);

		/*
		 * InputMethodManager imm = (InputMethodManager) getActivity()
		 * .getSystemService(Context.INPUT_METHOD_SERVICE);
		 * imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
		 * InputMethodManager.HIDE_NOT_ALWAYS);
		 */
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

		final ActionBar ab = getSherlockActivity().getSupportActionBar();
		ab.setDisplayShowCustomEnabled(true);

		inflater.inflate(R.menu.menu_search, menu);
		menu.getItem(0).expandActionView();
		ab.setIcon(R.drawable.ic_home);

		menu.getItem(0).setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				hideKeyboard();
				MainActivity mainActivity = (MainActivity) getActivity();
				if (mainActivity.getSlidingMenu().isMenuShowing()) {
					mainActivity.finish();
				} else {
					mainActivity.getSlidingMenu().toggle(true);
				}
				return false;
			}
		});

		mSearchView = (SearchView) menu.findItem(R.id.menu_search_edit)
				.getActionView();
		mSearchView.setOnQueryTextListener(this);
	}

	private void doSearch(int type, String content) {
		/*
		 * only login user can search user
		 */
		if (type == 1 && !WTApplication.getInstance().hasAccount) {
			Toast.makeText(getActivity(), R.string.text_error_request_login,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		showProgress();
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle b = apiHelper.getSearchResult(type, content);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_SEARCH,
				b, SearchFragment.this);
		saveSearchHistory(type, content);

		hideKeyboard();
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
		// adjust view
		Loader<HttpRequestResult> loader = null;
		if (loaderId == WTApplication.NETWORK_LOADER_SEARCH) {
			loader = new NetworkLoader(getActivity(), HttpMethod.Get, bundle);
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {

		mResultAdapter.notifyMayHaveMorePages();
		showResults();
		if (loader.getId() == WTApplication.NETWORK_LOADER_SEARCH) {
			if (result.getResponseCode() == 0) {
				processSearchResult(result.getStrResponseCon());
			} else {
				ExceptionToast.show(getActivity(), result.getResponseCode());
				showTips();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	private void processSearchResult(String jsonStr) {
		List<Pair<String, List<SearchResult>>> result = SearchUtil
				.generateSearchResults(jsonStr);
		if (resultIsEmpty(result)) {
			showNoResults();
		} else {
			mResultAdapter.setSearchResult(result);
			mResultAdapter.notifyDataSetChanged();
			showResults();
		}
	}

	private boolean resultIsEmpty(List<Pair<String, List<SearchResult>>> result) {
		for (int i = 0; i < result.size(); i++) {
			if (!result.get(i).second.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private void saveSearchHistory(int type, String keywords) {
		SearchHistory search = new SearchHistory();
		search.setType(type);
		search.setKeywords(keywords);

		if (mAdapter != null) {
			mAdapter.addObject(search);
		}

		List<SearchHistory> history = mAdapter.getData();
		SearchFactory searchFactory = new SearchFactory(this, history);
		searchFactory.saveSearch(true);
	}

	private void startClearTask() {
		mClearTask = new ClearHistoryTask();
		mClearTask.execute();
	}

	private class ClearHistoryTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			DbHelper dbHelper = WTApplication.getInstance().getDbHelper();

			try {
				TableUtils.clearTable(dbHelper.getConnectionSource(),
						SearchHistory.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mAdapter.clearHistory();
		}

	}

	private OnItemClickListener mOnResultClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			Intent intent = new Intent();
			Bundle b = new Bundle();
			SearchResult item = (SearchResult) mResultAdapter.getItem(position);

			switch (item.getType()) {
			case 1:
				Information info = (Information) item.getContent();
				intent.setClass(getActivity(), InformationDetailActivity.class);
				b.putParcelable(InformationsFragment.BUNDLE_KEY_INFORMATION,
						info);
				break;
			case 2:
				Account account = (Account) item.getContent();
				intent.setClass(getActivity(), AccountDetailActivity.class);
				b.putParcelable(AccountDetailActivity.BUNDLE_KEY_ACCOUNT,
						account);
				break;
			case 3:
				User user = (User) item.getContent();
				intent.setClass(getActivity(), FriendDetailActivity.class);
				b.putParcelable(FriendListFragment.BUNDLE_KEY_USER, user);
				break;
			case 4:
				Course course = (Course) item.getContent();
				intent.setClass(getActivity(), CourseDetailActivity.class);
				b.putParcelable(CourseDetailActivity.BUNDLE_COURSE, course);
				break;
			case 5:
				Activity activity = (Activity) item.getContent();
				intent.setClass(getActivity(), EventDetailActivity.class);
				b.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY, activity);
				break;
			case 6:
				Person person = (Person) item.getContent();
				intent.setClass(getActivity(), PersonDetailActivity.class);
				b.putParcelable(PeopleListFragment.BUNDLE_KEY_PERSON, person);
				break;
			}

			intent.putExtras(b);
			startActivity(intent);
		}

	};

	private void showHistory() {
		mLvSearchHistory.setVisibility(View.VISIBLE);
		mTvHistoryTitle.setVisibility(View.VISIBLE);
		mLvSearchTips.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mLvSearchResult.setVisibility(View.GONE);
		mTvNoResult.setVisibility(View.GONE);
	}

	private void showProgress() {
		mLvSearchHistory.setVisibility(View.GONE);
		mTvHistoryTitle.setVisibility(View.GONE);
		mLvSearchTips.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
		mLvSearchResult.setVisibility(View.GONE);
		mTvNoResult.setVisibility(View.GONE);
	}

	private void showTips() {
		mLvSearchHistory.setVisibility(View.GONE);
		mTvHistoryTitle.setVisibility(View.GONE);
		mLvSearchTips.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mLvSearchResult.setVisibility(View.GONE);
		mTvNoResult.setVisibility(View.GONE);
	}

	private void showResults() {
		mLvSearchHistory.setVisibility(View.GONE);
		mTvHistoryTitle.setVisibility(View.GONE);
		mLvSearchTips.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mLvSearchResult.setVisibility(View.VISIBLE);
		mTvNoResult.setVisibility(View.GONE);
	}

	private void showNoResults() {
		mLvSearchHistory.setVisibility(View.GONE);
		mTvHistoryTitle.setVisibility(View.GONE);
		mLvSearchTips.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mLvSearchResult.setVisibility(View.GONE);
		mTvNoResult.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (!TextUtils.isEmpty(query)) {
			doSearch(0, query);
		}
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText.length() == 0
				&& mLvSearchHistory.getVisibility() == View.GONE) {
			showHistory();
		}
		if (newText.length() > 0 && mLvSearchTips.getVisibility() == View.GONE) {
			showTips();
		}
		if (mTipAdapter != null) {
			mTipAdapter.setKeywords(newText);
		}
		return true;
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
	}

    @Override
    public void onResume() {
        super.onResume();
    }
}
