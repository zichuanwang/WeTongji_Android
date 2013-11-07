package com.wetongji_android.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.wetongji_android.R;
import com.wetongji_android.data.Notification;
import com.wetongji_android.factory.NotificationFactory;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTUtility;
import com.wetongji_android.util.exception.WTException;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import java.util.List;

/**
 * Created by martian on 13-11-7.
 */
public class WeNotificationService extends Service{
    public static final int MSG_REFRSH_NOTIFICATION = 991;

    private static final int REQUEST_DELAY_WIFI = 60000;
    private static final int REQUEST_DELAY_GPRS = 60000 * 2;
    private static final int REQUEST_DELAY_NONE = 60000 * 10;

    int notifyId = 001;

    private Handler mHandler;
    private Runnable mRunnable;
    private NotificationTask mTask;
    private NotificationFactory mFactory;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_REFRSH_NOTIFICATION && WTApplication.getInstance().hasAccount) {
                    Bundle bundle = ApiHelper.getInstance(WeNotificationService.this).getNotifications(true);
                    mTask = new NotificationTask(WeNotificationService.this);
                    WTUtility.executeAsyncTask(mTask, bundle);
                }
            }
        };

        mRunnable = new StoppableRunnable();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Start fetch notifications
        mHandler.postDelayed(mRunnable, 1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    private int getReqeustDelay() {
        if (!WTUtility.isConnect(this)) {
            return REQUEST_DELAY_NONE;
        }
        if (WTUtility.isWifi(this)) {
            return REQUEST_DELAY_WIFI;
        }
        return REQUEST_DELAY_GPRS;
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

    private class NotificationTask extends AsyncTask<Bundle, Void, HttpRequestResult> {
        private HttpMethod mMethod = HttpMethod.Get;
        private WTClient mClient;
        private Context mContext;

        public NotificationTask(Context context) {
            mClient = WTClient.getInstance();
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!WTUtility.isConnect(mContext)) {
                cancel(true);
            }
        }

        @Override
        protected HttpRequestResult doInBackground(Bundle... bundles) {
            try {
                HttpRequestResult result = mClient.execute(mMethod, bundles[0]);
                return result;
            } catch (WTException e) {
                e.printStackTrace();
                return new HttpRequestResult(408, "");
            }
        }

        @Override
        protected void onPostExecute(HttpRequestResult result) {
            //TODO Save notifications and tell create a notification
            NotificationManager notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            List<Notification> notifications;
            if (result.getResponseCode() == 0) {
                notifications = NotificationFactory.parseObjects(result.getStrResponseCon());

                for (int i = 0; i < notifications.size(); i ++) {
                    // Notification
                    Notification notify = notifications.get(i);

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(WeNotificationService.this)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(getString(R.string.new_notification))
                                    .setContentText(notify.getTitle());
                    notifyMgr.notify(notifyId, builder.build());
                    notifyId ++;
                }
            }
        }
    }
}
