package com.wetongji.service;

import org.json.JSONObject;

import com.wetongji_android.data.Version;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.factory.VersionFactory;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
				//JSONObject result=new JSONObject(client.getResponseStr());
				Version version=VersionFactory.create(client.getResponseStr());
				if(version.hasUpdate()){
					showStartUpdateDialog();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Unknown exception");
			return;
		}
	}
	
	private void showStartUpdateDialog(){
		
	}
	
	private void startUpdate(Version version){
		DownloadManager manager=(DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Request request=new DownloadManager.Request(Uri.parse(version.getUrl()));
		manager.enqueue(request);
	}
}
