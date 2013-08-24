package com.wetongji_android.ui.informations;

import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.data.InformationList;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.data.QueryHelper;
import com.wetongji_android.util.data.information.InformationUtil;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InformationsFragment extends WTBaseFragment implements
		LoaderCallbacks<HttpRequestResult> {

	private static final String TAG = "InformationsFragment";
	public static final String BUNDLE_KEY_INFORMATION = "bundle_key_information";

	private String mUID;
	private View mView;
	private AmazingListView mListNews;
	private InformationsListAdapter mAdapter;
	private InformationFactory mFactory;
	private LayoutInflater mInflater;
	private Activity mActivity;

	private static final String INFORMATION_LIST = "INFORMATIONS";
	public static final String SHARED_PREFERENCE_INFORMATION = "InfoSetting";
	public static final String PREFERENCE_INFO_TYPE = "InfoType";

	private int mSelectType = 15;
	private int currentPage = 0;

	public static InformationsFragment newInstance(StartMode startMode,
			Bundle args) {
		InformationsFragment fragment = new InformationsFragment();
		Bundle bundle;

		if (args != null) {
			bundle = args;
		} else {
			bundle = new Bundle();
		}

		switch (startMode) {
		case BASIC:
			bundle.putInt(BUNDLE_KEY_START_MODE, 1);
			break;
		case USERS:
			bundle.putInt(BUNDLE_KEY_START_MODE, 2);
			break;
		case LIKE:
			bundle.putInt(BUNDLE_KEY_START_MODE, 3);
			break;
		case FRIENDS:
			break;
		case ATTEND:
			bundle.putInt(BUNDLE_KEY_START_MODE, 4);
			break;
		case TODAY:
			bundle.putInt(BUNDLE_KEY_START_MODE, 5);
		}

		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			int modeCode = bundle.getInt(BUNDLE_KEY_START_MODE);
			switch (modeCode) {
			case 1:
				mStartMode = StartMode.BASIC;
				break;
			case 2:
				mStartMode = StartMode.USERS;
				break;
			case 3:
				mStartMode = StartMode.LIKE;
				break;
			case 4:
				mStartMode = StartMode.ATTEND;
				break;
			case 5:
				mStartMode = StartMode.TODAY;
				break;
			}

			mUID = bundle.getString(BUNDLE_KEY_UID);
			mSelectType = bundle.getInt(BUNDLE_KEY_SELECT_TYPE, 15);
			Log.v("select", "" + mSelectType);
		}

		setRetainInstance(true);

		setHasOptionsMenu(true);
		getSherlockActivity().getSupportActionBar().setTitle(
				R.string.search_type_information);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		} else {
			mView = inflater.inflate(R.layout.fragment_information, container,
					false);
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mInflater = LayoutInflater.from(mActivity);
		mListNews = (AmazingListView) mView.findViewById(R.id.lst_information);
		mListNews.setPinnedHeaderView(mInflater.inflate(
				R.layout.information_list_header, mListNews, false));
		mAdapter = new InformationsListAdapter(this, mListNews);
		mListNews.setAdapter(mAdapter);
		mListNews.setLoadingView(mInflater.inflate(
				R.layout.amazing_lst_view_loading_view, mListNews, false));
		mListNews.setOnItemClickListener(onItemClickListener);

		switch (getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			if (mStartMode == StartMode.BASIC) {
				mAdapter.loadDataFromDB(getQueryArgs());
			} else if (mStartMode == StartMode.LIKE) {
				loadDataLiked(1);
			} else if (mStartMode == StartMode.ATTEND) {
				loadDataByAccount(1);
			} else if(mStartMode == StartMode.TODAY) {
				mAdapter.loadDataFromDB(getQueryArgs());
			}
		case SCREEN_ROTATE:
			break;
		case ACTIVITY_DESTROY_AND_CREATE:
			WTUtility.log(TAG, "ACTIVITY DESTROY AND RECREATE");
			InformationList informations = (InformationList) savedInstanceState
					.getSerializable(INFORMATION_LIST);
			mAdapter.setOriginList(informations.getInformations());
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		InformationList informations = new InformationList();
		informations.setInformations(mAdapter.getOriginList());
		outState.putSerializable(INFORMATION_LIST, informations);
	}

	@Override
	public void onPause() {
		super.onPause();
		getLoaderManager().destroyLoader(WTApplication.NETWORK_LOADER_DEFAULT);
		getLoaderManager().destroyLoader(WTApplication.INFORMATION_LOADER);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String id = data.getStringExtra(WTBaseDetailActivity.KEY_OBJECT_ID);
		int like = data.getIntExtra(WTBaseDetailActivity.KEY_LIKE_NUMBER, 0);
		boolean canLike = data.getBooleanExtra(
				WTBaseDetailActivity.KEY_CAN_LIKE, true);
		
		List<Information> lstInfo = mAdapter.getOriginList();
		for (int i = 0; i < lstInfo.size(); i++) {
			Information info = lstInfo.get(i);
			if (info.getId() == Integer.valueOf(id)) {
				info.setLike(like);
				info.setCanLike(canLike);
			}
			lstInfo.set(i, info);
		}
		mAdapter.setInformations(InformationUtil
				.getSectionedInformationList(lstInfo));
		mAdapter.setOriginList(lstInfo);
	}

	private void loadDataLiked(int page) {
		mAdapter.setLoadingData(true);
		mAdapter.notifyMayHaveMorePages();
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getLikedObjectsListWithModelType(page,
				"Information");
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				args, this);
	}

	private void loadDataByAccount(int page) {
		mAdapter.setLoadingData(true);
		mAdapter.notifyMayHaveMorePages();
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle args = apiHelper.getInformationsByAccount(mUID, page, 15);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				args, this);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(mActivity,
					InformationDetailActivity.class);
			Information information = (Information) mAdapter.getItem(arg2);
			Bundle bundle = new Bundle();
			bundle.putParcelable(BUNDLE_KEY_INFORMATION, information);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			mActivity.overridePendingTransition(R.anim.slide_right_in,
					R.anim.slide_left_out);
		}
	};

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if (result.getResponseCode() != 0) {
			ExceptionToast.show(mActivity, result.getResponseCode());
		} else {
			if (mFactory == null)
				mFactory = new InformationFactory(this);

			if (currentPage == 0) {
				mAdapter.clear();
			}

			currentPage++;
			Pair<Integer, List<Information>> informations = mFactory
					.createObjects(result.getStrResponseCon(), currentPage);
			List<Information> lists = informations.second;

			mAdapter.setLoadingData(false);
			if (mFactory.getNextPage() == 0) {
				mAdapter.notifyNoMorePages();
			}
			mAdapter.setInformations(InformationUtil
					.getSectionedInformationList(lists));
			mAdapter.setOriginList(lists);
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {

	}

	public void refreshData() {
		mAdapter.clear();
		currentPage = 0;
		mAdapter.setLoadingData(true);
		mAdapter.notifyMayHaveMorePages();
		mListNews.setSelection(0);

		ApiHelper apiHelper = ApiHelper.getInstance(mActivity);
		// By default we fetch all kind of informations from the server
		Bundle args = apiHelper.getInformations(1, mSelectType);
		getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				args, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		if (mStartMode == StartMode.BASIC) {
			getSherlockActivity().getSupportActionBar()
					.setDisplayShowCustomEnabled(true);
			getSherlockActivity().getSupportActionBar().setCustomView(
					R.layout.actionbar_informationlist);
			getActivity().findViewById(R.id.notification_button)
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							NotificationHandler.getInstance().finish();
							if (WTApplication.getInstance().hasAccount) {
								((MainActivity) getActivity()).showRightMenu();
							} else {
								Toast.makeText(
										getActivity(),
										getResources().getText(
												R.string.no_account_error),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			getActivity().findViewById(R.id.informationlist_reload_button)
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							refreshData();
						}
					});
			getActivity().findViewById(R.id.informationlist_type_button)
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							openTypeDialog();
						}
					});
			readPreference();
		} else {
			getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			inflater.inflate(R.menu.menu_informationlist_nonotification, menu);
		}
	}

	private void readPreference() {
		SharedPreferences sp = getActivity().getSharedPreferences(
				SHARED_PREFERENCE_INFORMATION, Context.MODE_PRIVATE);
		mSelectType = sp.getInt(PREFERENCE_INFO_TYPE, 15);
	}

	private void writePreference() {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(
				SHARED_PREFERENCE_INFORMATION, Context.MODE_PRIVATE).edit();
		editor.putInt(PREFERENCE_INFO_TYPE, mSelectType);
		editor.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().finish();
			break;
		default:
			return super.onOptionsItemSelected(item);

		}

		return true;
	}

	private void openTypeDialog() {
		final Dialog dialog = new Dialog(mActivity);
		dialog.setTitle(R.string.events_type_dialog_title);
		dialog.setContentView(R.layout.dialog_news_type);
		dialog.setCanceledOnTouchOutside(true);
		LinearLayout llCancel = (LinearLayout) dialog
				.findViewById(R.id.btn_sort_dialog_cancel);
		llCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				refreshData();
			}
		});

		CheckBox ckCampus = (CheckBox) dialog.findViewById(R.id.info_menu_cat1);
		CheckBox ckAdmin = (CheckBox) dialog.findViewById(R.id.info_menu_cat2);
		CheckBox ckClub = (CheckBox) dialog.findViewById(R.id.info_menu_cat3);
		CheckBox ckLocal = (CheckBox) dialog.findViewById(R.id.info_menu_cat4);
		ckCampus.setChecked((mSelectType & ApiHelper.API_ARGS_INFO_CAMPUS) != 0);
		ckAdmin.setChecked((mSelectType & ApiHelper.API_ARGS_INFO_ADMINISTRATIVE) != 0);
		ckClub.setChecked((mSelectType & ApiHelper.API_ARGS_INFO_CLUB) != 0);
		ckLocal.setChecked((mSelectType & ApiHelper.API_ARGS_INFO_LOCAL) != 0);
		OnTypeChangedListener typeChangedListener = new OnTypeChangedListener();
		ckCampus.setOnCheckedChangeListener(typeChangedListener);
		ckAdmin.setOnCheckedChangeListener(typeChangedListener);
		ckClub.setOnCheckedChangeListener(typeChangedListener);
		ckLocal.setOnCheckedChangeListener(typeChangedListener);
		dialog.show();
	}

	private class OnTypeChangedListener implements
			android.widget.CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.info_menu_cat1:
				if (buttonView.isChecked()) {
					mSelectType += ApiHelper.API_ARGS_INFO_CAMPUS;
				} else {
					mSelectType -= ApiHelper.API_ARGS_INFO_CAMPUS;
				}
				break;
			case R.id.info_menu_cat2:
				if (buttonView.isChecked()) {
					mSelectType += ApiHelper.API_ARGS_INFO_ADMINISTRATIVE;
				} else {
					mSelectType -= ApiHelper.API_ARGS_INFO_ADMINISTRATIVE;
				}
				break;
			case R.id.info_menu_cat3:
				if (buttonView.isChecked()) {
					mSelectType += ApiHelper.API_ARGS_INFO_CLUB;
				} else {
					mSelectType -= ApiHelper.API_ARGS_INFO_CLUB;
				}
				break;
			case R.id.info_menu_cat4:
				if (buttonView.isChecked()) {
					mSelectType += ApiHelper.API_ARGS_INFO_LOCAL;
				} else {
					mSelectType -= ApiHelper.API_ARGS_INFO_LOCAL;
				}
				break;
			}

			writePreference();
		}
	}

	private Bundle getQueryArgs() {
		boolean hasCampus = (mSelectType & ApiHelper.API_ARGS_INFO_CAMPUS) != 0;
		boolean hasAdmin = (mSelectType & ApiHelper.API_ARGS_INFO_ADMINISTRATIVE) != 0;
		boolean hasClub = (mSelectType & ApiHelper.API_ARGS_INFO_CLUB) != 0;
		boolean hasLocal = (mSelectType & ApiHelper.API_ARGS_INFO_LOCAL) != 0;

		return QueryHelper.getInformationsQueryArgs(hasCampus, hasAdmin,
				hasClub, hasLocal);
	}

	public AmazingListView getmListNews() {
		return mListNews;
	}

	public void setmListNews(AmazingListView mListNews) {
		this.mListNews = mListNews;
	}

	public void onResume() {
		super.onResume();
		NotificationHandler.getInstance().checkNotification();
	}
}