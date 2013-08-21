package com.wetongji_android.ui.notification;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.wetongji_android.R;
import com.wetongji_android.data.Notification;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.notification.NotificationsLoader;
import com.wetongji_android.util.date.DateParser;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter implements
		LoaderCallbacks<List<Notification>> {
	private List<Notification> mListNotifications;
	private Fragment mFragment;
	private Context mContext;
	private LayoutInflater mInflater;
	private AQuery mShouldDelayAq;
	private AQuery mListAq;
	private BitmapDrawable mBmDefaultThumbnails;

	public NotificationListAdapter(Fragment fragment) {
		this.mFragment = fragment;
		this.mContext = fragment.getActivity();
		this.mListNotifications = new ArrayList<Notification>();
		this.mInflater = LayoutInflater.from(mContext);
		mListAq = WTApplication.getInstance().getAq(mFragment.getActivity());
		mBmDefaultThumbnails = (BitmapDrawable) mContext.getResources()
				.getDrawable(R.drawable.notification_thumb);
		mFragment.getLoaderManager().initLoader(
				WTApplication.NOTIFICATIONS_LOADER, null, this);
	}

	/**
	 * set ListView content and return if there are new notificaiton
	 * @param notifications
	 * @return
	 */
	public boolean setContentList(List<Notification> notifications) {
		boolean hasNew = false;
		for (int i = 0; i < notifications.size(); i++) {
			if (mListNotifications.contains(notifications.get(i))) {
				hasNew = true;
				break;
			}
		}
		this.mListNotifications.clear();
		this.mListNotifications.addAll(notifications);
		notifyDataSetChanged();
		return hasNew;
	}

	@Override
	public int getCount() {
		return this.mListNotifications.size();
	}

	@Override
	public Object getItem(int position) {
		return this.mListNotifications.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Loader<List<Notification>> onCreateLoader(int arg0, Bundle arg1) {
		return new NotificationsLoader(mContext);
	}

	@Override
	public void onLoadFinished(Loader<List<Notification>> arg0,
			List<Notification> notifications) {
		setContentList(notifications);
	}

	@Override
	public void onLoaderReset(Loader<List<Notification>> arg0) {
	}

	static class InformViewHolder {
		TextView tv_notification_time;
		TextView tv_notification_content;
		ImageView img_notification_thumbnails;
	}

	static class ConsultViewHolder {
		TextView tv_notification_time;
		TextView tv_notification_content;
		ImageView img_notification_thumbnails;
		ImageView img_notification_mark;
		Button btn_notification_ignore;
		Button btn_notification_accept;
		LinearLayout ll_notification_buttonbar;
		RelativeLayout rl_notification_accept;
		LinearLayout ll_notification_row;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup parent) {
		final ConsultViewHolder holder;

		if (view == null) {
			holder = new ConsultViewHolder();
			view = mInflater.inflate(R.layout.row_request_notification, parent,
					false);
			holder.tv_notification_content = (TextView) view
					.findViewById(R.id.tv_notification_content);
			holder.tv_notification_time = (TextView) view
					.findViewById(R.id.tv_notification_time);
			holder.btn_notification_ignore = (Button) view
					.findViewById(R.id.btn_notification_ignore);
			holder.btn_notification_accept = (Button) view
					.findViewById(R.id.btn_notification_yes);
			holder.ll_notification_buttonbar = (LinearLayout) view
					.findViewById(R.id.ll_notification_buttonbar);
			holder.rl_notification_accept = (RelativeLayout) view
					.findViewById(R.id.rl_notification_accept);
			holder.img_notification_thumbnails = (ImageView) view
					.findViewById(R.id.img_notification_thumbnails);
			holder.ll_notification_row = (LinearLayout) view
					.findViewById(R.id.ll_notification_row);
			holder.img_notification_mark = (ImageView) view
					.findViewById(R.id.img_notification_mark);
			view.setTag(holder);
		} else {
			holder = (ConsultViewHolder) view.getTag();
		}

		Notification notification = mListNotifications.get(arg0);
		String title = notification.getTitle();
		String from = notification.getFrom();
		SpannableString spanStr = new SpannableString(title);
		spanStr.setSpan(new TextAppearanceSpan(mContext,
				R.style.NotificationItemFrom), 0, from.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new TextAppearanceSpan(mContext,
				R.style.NotificationItemContent), from.length(),
				title.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.tv_notification_content.setText(spanStr,
				TextView.BufferType.SPANNABLE);
		holder.tv_notification_time.setText(DateParser
				.parseDateForNotification(notification.getSentAt()));
		holder.btn_notification_accept
				.setText(R.string.text_notification_accept);
		holder.btn_notification_ignore
				.setText(R.string.text_notificaiton_ignore);

		// set thumbnail
		String strUrl = notification.getThumbnail();
		mShouldDelayAq = mListAq.recycle(view);
		if (!strUrl.equals(WTApplication.MISSING_IMAGE_URL)) {

			if (mShouldDelayAq.shouldDelay(arg0, view, parent, strUrl)) {
				mShouldDelayAq.id(holder.img_notification_thumbnails).image(
						mBmDefaultThumbnails);
			} else {
				ImageOptions options = new ImageOptions();
				options.round = 15;
				options.fileCache = true;
				options.memCache = true;
				mShouldDelayAq.id(holder.img_notification_thumbnails).image(
						strUrl, options);
			}
		} else {
			mShouldDelayAq.id(holder.img_notification_thumbnails).image(
					mBmDefaultThumbnails);
		}

		// check if the notification is confirmed
		if (notification.isIsConfirmed()) {
			holder.img_notification_mark
					.setImageResource(R.drawable.ic_notification_mark_done);
			holder.ll_notification_buttonbar.setVisibility(View.GONE);
		} else {
			holder.img_notification_mark
					.setImageResource(R.drawable.ic_notification_mark_ask);
			// check if the notification is accepted
			if (notification.isAccepted()) {
				holder.ll_notification_buttonbar.setVisibility(View.GONE);
				holder.rl_notification_accept.setVisibility(View.VISIBLE);
			} else {
				holder.ll_notification_buttonbar.setVisibility(View.VISIBLE);
				holder.rl_notification_accept.setVisibility(View.GONE);
			}
		}

		OnClickListener onClickListener = new OnNotifActionClickListener(
				notification, arg0);
		holder.btn_notification_accept.setOnClickListener(onClickListener);
		holder.btn_notification_ignore.setOnClickListener(onClickListener);
		holder.ll_notification_row.setOnClickListener(onClickListener);

		return view;
	}

	private class OnNotifActionClickListener implements OnClickListener {
		private Notification mNotification;
		private int mPosition;

		public OnNotifActionClickListener(Notification notif, int pos) {
			super();
			mNotification = notif;
			mPosition = pos;
		}

		@Override
		public void onClick(View view) {
			NotificationFragment fragment = (NotificationFragment) mFragment;
			if (view.getId() == R.id.btn_notification_ignore) {
				fragment.startIngoreAction(mNotification, mPosition);
			} else if (view.getId() == R.id.btn_notification_yes) {
				fragment.startAcceptAction(mNotification, mPosition);
			}
		}
	}

	public void remove(int mIgnorePos) {
		mListNotifications.remove(mIgnorePos);
		this.notifyDataSetChanged();
	}

	public void acceptNotification(int mAcceptPos) {
		Notification notif = mListNotifications.get(mAcceptPos);
		notif.setAccepted(true);
		notifyDataSetChanged();
	};
}