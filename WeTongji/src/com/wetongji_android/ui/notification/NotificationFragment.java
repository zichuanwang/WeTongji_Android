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
import com.wetongji_android.ui.course.CourseDetailActivity;
import com.wetongji_android.ui.event.EventDetailActivity;
import com.wetongji_android.ui.event.EventsFragment;
import com.wetongji_android.ui.friend.FriendDetailActivity;
import com.wetongji_android.ui.friend.FriendListFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationFragment extends Fragment implements
		LoaderCallbacks<HttpRequestResult>, OnItemClickListener {
	private static final String TAG = "NotificationFragment";

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
			mView = inflater.inflate(R.layout.notification_frame, container,
					false);
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// set list adapter
		mListNotifications = (ListView) mView
				.findViewById(R.id.lst_notification);
		mListNotifications.setOnItemClickListener(this);
		mAdapter = new NotificationListAdapter(this);
		mListNotifications.setAdapter(mAdapter);

		if (WTApplication.getInstance().hasAccount) {
			// init loader(this loader is used for loading data from network)
			ApiHelper apiHelper = ApiHelper.getInstance(getActivity());
			mBundle = apiHelper.getNotifications(false);
			// showProgressDialog();
			// TODO startLoader
			// getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_3,
			// mBundle, this);
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 990) {
					Toast.makeText(getActivity(), "Notification refreshed", Toast.LENGTH_SHORT)
							.show();
					getLoaderManager().restartLoader(WTApplication.NETWORK_LOADER_3,
							mBundle, NotificationFragment.this);
				}
			}

		};
		
		mRunnable = new StoppableRunnable();
		mHandler.postDelayed(mRunnable, 1);
			
	}
		
	private class StoppableRunnable implements Runnable {
		private volatile boolean mIsStopped = false;
		@Override
		public void run() {
			setStopped(false);
			while (!mIsStopped) {
				Message msg = new Message();
				msg.what = 990;
				mHandler.sendMessage(msg);
				mHandler.postDelayed(this, 1000 * 15);
				
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
		((StoppableRunnable) mRunnable).stop();
		mHandler.removeCallbacks(mRunnable);
//		mRunnable
		super.onPause();
	}

	
	@Override
	public void onDestroy() {
		//mHandler.removeCallbacks(mRunnable);
		super.onDestroy();
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
			mAdapter.acceptNotification(mAcceptPos);
		} else if (loader.getId() == WTApplication.NETWORK_LOADER_IGNORE_FRIEDN) {
			mAdapter.remove(mIgnorePos);
		} else if (loader.getId() == WTApplication.NETWORK_LOADER_3) {
			if (result.getResponseCode() == 0) {
				if (mFactory == null)
					mFactory = new NotificationFactory();

				List<Notification> results = mFactory.createObjects(result
						.getStrResponseCon());
				mAdapter.setContentList(results);
				Log.v(TAG, "" + results.size());
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
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
}
