package com.wetongji3;

import org.json.JSONObject;

import com.wetongji.R;
import com.wetongji3.data.Version;
import com.wetongji3.net.WTClient;
import com.wetongji3.util.factory.WTVersionFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

public class WTUpdateService extends IntentService {
	private static final String TAG=WTUpdateService.class.getSimpleName();
	
	public WTUpdateService() {
		super("WTUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		WTClient client=WTClient.getInstance();
		try {
			client.checkVersion();
			if(!client.isHasError()){
				JSONObject result=new JSONObject(client.getResponseStr());
				Version version=WTVersionFactory.create(result);
				Log.d(TAG, version.toString());
				if(version.hasUpdate()){
					showStartUpdateDialog(version);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Unknown exception");
			return;
		}
	}
	
	private void showStartUpdateDialog(final Version version){
		AlertDialog.Builder builder=new Builder(this);
		builder.setMessage(R.string.ask_for_update);
		builder.setPositiveButton(R.string.yes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startUpdate(version);
			}
		});
		builder.setNegativeButton(R.string.not_now, null);
		AlertDialog dialog=builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
	
	private void startUpdate(Version version){
		DownloadManager manager=(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(version.getUrl()));
		manager.enqueue(request);
	}
	
}
