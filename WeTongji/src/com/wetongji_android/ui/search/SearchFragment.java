package com.wetongji_android.ui.search;

import java.sql.SQLException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.foound.widget.AmazingListView;
import com.j256.ormlite.table.TableUtils;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.Person;
import com.wetongji_android.data.Search;
import com.wetongji_android.data.User;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
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
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class SearchFragment extends SherlockFragment implements
		LoaderCallbacks<HttpRequestResult> {

	private SearchHistoryAdapter mAdapter;
	private SearchTipsAdapter mTipAdapter;
	private SearchResultAdapter mResultAdapter;
	private ListView mLvSearchHistory;
	private ListView mLvSearchTips;
	private AmazingListView mLvSearchResult;
	private ClearHistoryTask mClearTask;
	private EditText mEtSearch;
	private TextView mTvHistoryTitle;

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
		View view = inflater
				.inflate(R.layout.fragment_search, container, false);

		mTvHistoryTitle = (TextView) view
				.findViewById(R.id.text_search_history_title);
		mLvSearchHistory = (ListView) view
				.findViewById(R.id.lst_search_history);
		mLvSearchTips = (ListView) view.findViewById(R.id.lst_search_tips);
		mLvSearchResult = (AmazingListView) view.findViewById(R.id.lst_search_result);
		mResultAdapter = new SearchResultAdapter(this);
		mLvSearchResult.setAdapter(mResultAdapter);
		mLvSearchResult.setPinnedHeaderView(
				inflater.inflate(R.layout.information_list_header,
				mLvSearchResult, false));
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
					Search search = (Search) arg0.getItemAtPosition(position);
					doSearch(search.getType(), search.getKeywords());
					mEtSearch.setText(search.getKeywords());
					mEtSearch.setSelection(mEtSearch.getText().length());
				}
			}
		});
		
		mLvSearchTips.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				int type = 0;
				
				switch (position) {
				case 1:
					type = 1;
					break;
				case 2:
					type = 2;
					break;
				case 3:
					type = 3;
					break;
				case 4:
					type = 4;
					break;
				case 5:
					type = 5;
					break;
				}
				doSearch(type, mTipAdapter.getmKeywords());
			}
		});

		mTipAdapter = new SearchTipsAdapter(this);
		mLvSearchTips.setAdapter(mTipAdapter);
		
		// show software keyboards
		/*InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
				InputMethodManager.HIDE_NOT_ALWAYS);*/
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

		getSherlockActivity().getSupportActionBar()
				.setDisplayShowCustomEnabled(true);

		inflater.inflate(R.menu.menu_search, menu);
		mEtSearch = (EditText) menu.findItem(R.id.menu_search_edit)
				.getActionView();
		menu.getItem(0).expandActionView();
		menu.getItem(0).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				mEtSearch.forceLayout();
				return true;
			}
		});

		mEtSearch.setText(null);
		mEtSearch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView arg0, int actionId,
							KeyEvent arg2) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							String content = mEtSearch.getText().toString();
							if (!TextUtils.isEmpty(content)) {
								doSearch(0, content);
							}
							return true;
						}
						return false;
					}
				});

		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mEtSearch.getText().length() == 0 &&
						mLvSearchHistory.getVisibility() == View.GONE) {
					mLvSearchTips.setVisibility(View.GONE);
					mLvSearchHistory.setVisibility(View.VISIBLE);
					mTvHistoryTitle.setVisibility(View.VISIBLE);
					mLvSearchResult.setVisibility(View.GONE);
				}
				if (mEtSearch.getText().length() > 0 && 
						mLvSearchTips.getVisibility() == View.GONE) {
					mLvSearchTips.setVisibility(View.VISIBLE);
					mLvSearchHistory.setVisibility(View.GONE);
					mTvHistoryTitle.setVisibility(View.GONE);
					mLvSearchResult.setVisibility(View.GONE);
				}
				if (mTipAdapter != null) {
					mTipAdapter.setKeywords(mEtSearch.getText().toString());
				}

			}
		});

	}
	
	private void doSearch(int type, String content) {
		ApiHelper apiHelper = ApiHelper
				.getInstance(getActivity());
		Bundle b = apiHelper
				.getSearchResult(type, content);
		getLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_SEARCH, b,
				SearchFragment.this);
		//TODO
		//saveSearchHistory(type, content);
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
		mLvSearchResult.setVisibility(View.VISIBLE);
		mLvSearchTips.setVisibility(View.GONE);
		if (loader.getId() == WTApplication.NETWORK_LOADER_SEARCH) {
			if (result.getResponseCode() == 0) {
				processSearchResult(result.getStrResponseCon());
			} else {
				// Network Error
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
	private void processSearchResult(String jsonStr) {
		mResultAdapter.setSearchResult(SearchUtil.generateSearchResults(jsonStr));
		mResultAdapter.notifyDataSetChanged();
	}

	//TODO
	/*private void saveSearchHistory(int type, String keywords) {
		Search search = new Search();
		search.setType(type);
		search.setKeywords(keywords);

		if (mAdapter != null) {
			mAdapter.addObject(search);
		}

		List<Search> history = mAdapter.getData();
		SearchFactory searchFactory = new SearchFactory(this, history);
		searchFactory.saveSearch(true);
	}*/

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
						Search.class);
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
			Log.d("data", "click");
			Intent intent = new Intent();
			Bundle b = new Bundle();
			Object item = mResultAdapter.getItem(position);
			if (item instanceof User) {
				User user = (User) item;
				intent.setClass(getActivity(), FriendDetailActivity.class);
				b.putParcelable(FriendListFragment.BUNDLE_KEY_USER, user);
			} else if (item instanceof Account) {
				// TODO enter account detail
			} else if (item instanceof Activity) {
				Activity activity = (Activity) item;
				intent.setClass(getActivity(), EventDetailActivity.class);
				b.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY, activity);
			} else if (item instanceof Information) {
				Information info = (Information) item;
				intent.setClass(getActivity(), InformationDetailActivity.class);
				b.putParcelable(InformationsFragment.BUNDLE_KEY_INFORMATION, info);
			} else if (item instanceof Person) {
				Person person = (Person) item;
				intent.setClass(getActivity(), PersonDetailActivity.class);
				b.putParcelable(PeopleListFragment.BUNDLE_KEY_PERSON, person);
			}
			
			intent.putExtras(b);
			startActivity(intent);
		}
		
	};

}
