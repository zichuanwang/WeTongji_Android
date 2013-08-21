package com.wetongji_android.ui.main;

import com.wetongji_android.R;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class NotificationHandler extends Handler {
	private static NotificationHandler handler = null;
	private Animation anim = null;
	private boolean ifHandled = true;
	private Activity mActivity = null;

	public static NotificationHandler getInstance() {
		return handler;
	}

	public static void init(Activity mActivity) {
		System.out.println("HandlerInitiated");
		handler = new NotificationHandler();
		handler.setAnim(AnimationUtils.loadAnimation(mActivity,
				R.anim.notification_activated));
		handler.setmActivity(mActivity);
	}

	public void inform() {
		ifHandled = false;
		handler.sendEmptyMessage(1);
	}

	public void finish() {
		if (ifHandled == false) {
			ifHandled = true;
			handler.sendEmptyMessage(0);
		}
	}

	public void checkNotification() {
		if (ifHandled == false)
			handler.sendEmptyMessage(1);
	}

	public void handleMessage(Message msg) {
		View notificationBg;
		switch (msg.what) {
		case 1:
			notificationBg = mActivity
					.findViewById(R.id.notification_button_today_bg);
			if (notificationBg == null)
				break;
			if (anim == null)
				break;
			View notificationImage = mActivity
					.findViewById(R.id.notification_button_today);
			if (notificationImage == null)
				break;
			notificationBg.setVisibility(View.VISIBLE);
			notificationBg.startAnimation(anim);
			break;
		case 0:
			notificationBg = mActivity
					.findViewById(R.id.notification_button_today_bg);
			if (notificationBg == null)
				break;
			notificationBg.setVisibility(View.GONE);
			if (anim == null)
				break;
			anim.cancel();
			anim.reset();
			Animation animEmpty = AnimationUtils.loadAnimation(mActivity,
					R.anim.notification_empty);
			notificationBg.startAnimation(animEmpty);
			animEmpty.cancel();
			animEmpty.reset();
			break;
		}
		super.handleMessage(msg);
	}

	private void setAnim(Animation anim) {
		this.anim = anim;
	}

	private void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

}
