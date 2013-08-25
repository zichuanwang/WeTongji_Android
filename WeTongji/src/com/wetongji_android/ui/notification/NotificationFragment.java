package com.wetongji_android.ui.notification;

import java.util.List;

import com.wetongji_android.R;
import com.wetongji_android.data.Activity;
import com.wetongji_android.data.Course;
import com.wetongji_android.data.Notification;
import com.wetongji_android.data.User;
import com.wetongji_android.factory.NotificationFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.ui.course.CourseDetailActivity;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.friend.FriendDetailActivity;
import com.wetongji_android.ui.friend.FriendListFragment;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment implements
		LoaderCallbacks<HttpRequestResult> {
	private static final int MSG_REFRSH_NOTIFICATION = 990;
	
	private static final int REQUEST_DELAY_WIFI = 15000;
	private static final int REQUEST_DELAY_GPRS = 40000;
	private static final int REQUEST_DELAY_NONE = 120000;

	private View mView;
	private ListView mListNotifications;
	private NotificationListAdapter mAdapter;
	private android.app.Activity mActivity;

	private ProgressDialog mProDialog = null;

	private NotificationFactory mFactory;

	private int mIgnorePos = -1;
	private int mAcceptPos = -1;

	private Bundle mBundle;
	private Handler mHandler;
	private Runnable mRunnable;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		} else {
			mView = inflater.inflate(R.layout.notification_frame, container, false);
		}
		return mView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// set list adapter
		mListNotifications = (ListView) mView
				.findViewById(R.id.lst_notification);
		mAdapter = new NotificationListAdapter(this);
		mListNotifications.setAdapter(mAdapter);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == MSG_REFRSH_NOTIFICATION) {
					if (WTApplication.getInstance().hasAccount) {
						mListNotifications.setVisibility(View.VISIBLE);
						mView.findViewById(R.id.notificaion_login_area)
								.setVisibility(View.GONE);
						getLoaderManager().restartLoader(
								WTApplication.NETWORK_LOADER_3, mBundle,
								NotificationFragment.this);
					} else {
						mListNotifications.setVisibility(View.GONE);
						mView.findViewById(R.id.notificaion_login_area)
								.setVisibility(View.VISIBLE);
					}
				}
			}

		};

		// init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		mBundle = apiHelper.getNotifications(true);
		mRunnable = new StoppableRunnable();
		mHandler.postDelayed(mRunnable, 1);
		mView.findViewById(R.id.btn_notification_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						startActivity(new Intent(getActivity(),
								AuthActivity.class));
					}
				});
	}

	@SuppressLint("HandlerLeak")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
		
	private class StoppableRunnable implements Runnable {
		private volatile boolean mIsStopped = false;
		@Override
		public void run() {
			setStopped(false);
			while (!isStopped()) {
				Message msg = new Message();
				msg.what = MSG_REFRSH_NOTIFICATION;
				mHandler.sendMessage(msg);
				mHandler.postDelayed(this, getReqeustDelay());
				
	            stop();
			}
		}
		
		public boolean isStopped() {
	        return mIsStopped;
	    }

	    private void setStopped(boolean isStop) {    
	        if (mIsStopped != isStop)
	            mIsStopped = isStop;
	    }
	    
	    public void stop() {
	        setStopped(true);
	    }
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onPause() {
		if (mRunnable != null) {
			((StoppableRunnable) mRunnable).stop();
			mHandler.removeCallbacks(mRunnable);
		}
		super.onPause();
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loadId, Bundle arg1) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		hideProgressDialog();
		if (loader.getId() == WTApplication.NETWORK_LOADER_ACCEPT_FRIEND) {
			getLoaderManager().destroyLoader(loader.getId());
			mAdapter.acceptNotification(mAcceptPos);
		} else if (loader.getId() == WTApplication.NETWORK_LOADER_IGNORE_FRIEDN) {
			mAdapter.remove(mIgnorePos);
		} else if (loader.getId() == WTApplication.NETWORK_LOADER_3) {
			if (result.getResponseCode() == 0) {
				if (mFactory == null)
					mFactory = new NotificationFactory(this);

				List<Notification> results = mFactory.createObjects(result
						.getStrResponseCon());
				
				if (!results.isEmpty()) {
					NotificationHandler.getInstance().inform();
					mAdapter.addContent(results);
				}
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	public void loadNotifications() {
		// init loader(this loader is used for loading data from network)
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		Bundle bundle = apiHelper.getNotifications(true);
		showProgressDialog();
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				bundle, this);
	}

	private void showProgressDialog() {
		if (mProDialog == null) {
			mProDialog = new ProgressDialog(mActivity);
			mProDialog.setIndeterminate(true);
			mProDialog.show();
		}
	}

	private void hideProgressDialog() {
		if (mProDialog != null) {
			mProDialog.dismiss();
			mProDialog = null;
		}
	}

	public void startAcceptAction(Notification notification, int pos) {
		mAcceptPos = pos;
		int id = notification.getSourceId();
		Bundle bundle = null;
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		switch (notification.getType()) {
		case 1:
			bundle = apiHelper.acceptCourseInvitation(String.valueOf(id));
			break;
		case 2:
			bundle = apiHelper.acceptFriendInvitation(String.valueOf(id));
			break;
		case 3:
			bundle = apiHelper.acceptActivityInvitation(String.valueOf(id));
			break;
		}
		getLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_ACCEPT_FRIEND, bundle, this);
	}

	public void startIngoreAction(Notification notification, int pos) {
		mIgnorePos = pos;
		int id = notification.getSourceId();
		Bundle bundle = null;
		ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
		switch (notification.getType()) {
		case 1:
			bundle = apiHelper.ignoreCourseInvitation(String.valueOf(id));
			break;
		case 2:
			bundle = apiHelper.ignoreFriendInvitation(String.valueOf(id));
			break;
		case 3:
			bundle = apiHelper.ignoreActivityInvitation(String.valueOf(id));
			break;
		}
		getLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_IGNORE_FRIEDN, bundle, this);
	}
	
	public void viewNotificationDetail(int position) {
		Notification notification = (Notification) mAdapter.getItem(position);
		Intent intent = null;
		Bundle bundle = new Bundle();
		switch (notification.getType()) {
		case 1:
			intent = new Intent(getActivity(), CourseDetailActivity.class);
			bundle.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY,
					(Course) notification.getContent());
			break;
		case 2:
			intent = new Intent(getActivity(), FriendDetailActivity.class);
			bundle.putParcelable(FriendListFragment.BUNDLE_KEY_USER,
					(User) notification.getContent());
			break;
		case 3:
			intent = new Intent(getActivity(), EventDetailActivity.class);
			bundle.putParcelable(EventsFragment.BUNDLE_KEY_ACTIVITY,
					(Activity) notification.getContent());
			break;
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private int getReqeustDelay() {
		if (!WTUtility.isConnect(getActivity())) {
			return REQUEST_DELAY_NONE;
		}
		if (WTUtility.isWifi(getActivity())) {
			return REQUEST_DELAY_WIFI;
		}
		return REQUEST_DELAY_GPRS;
	}
}
