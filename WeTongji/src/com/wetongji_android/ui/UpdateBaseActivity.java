package com.wetongji_android.ui;

import com.wetongji_android.R;
import com.wetongji_android.data.Version;
import com.wetongji_android.factory.WTVersionFactory;
import com.wetongji_android.net.WTClient;
import com.wetongji_android.util.net.WTAsyncTask;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class UpdateBaseActivity extends Activity {
	GetUpdateTask getUpdateTask;
	private DownloadEventReceiver receiver;
	private long id;
	private DownloadManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver=new DownloadEventReceiver();
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}
	
	public class GetUpdateTask extends WTAsyncTask<Void, Void, Version>{
		private WTClient client;
		private String TAG=this.getClass().getSimpleName();
		
		@Override
		protected Version doInBackground(Void... params) {
			client=WTClient.getInstance();
			try {
				client.checkVersion();
				if(!client.isHasError()){
					return WTVersionFactory.create(client.getResponseStr());
				}
			} catch (Exception e) {
				Log.w(TAG, "unknown error");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Version result) {
			super.onPostExecute(result);
			if(result.hasUpdate()){
				showUpdateInfo(result);
			}
		}
		
	}
	
	public void showUpdateInfo(Version version){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle(R.string.title_dialog_update_found);
		//String current=version.getCurrent().substring(7);
		String latest=version.getLatest().substring(7);
		String updateInfo=String.format(getString(R.string.msg_dialog_update_found), latest, "");
		builder.setMessage(updateInfo);
		final String url=version.getUrl();
		builder.setPositiveButton(R.string.text_btn_update, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startDownloadUpdate(url);
			}
		});
		builder.setNegativeButton(R.string.text_btn_not_now, null);
		AlertDialog dialog=builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void startDownloadUpdate(String url){
		manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		DownloadManager.Request request=new Request(Uri.parse(url));
		request.setDestinationInExternalFilesDir(this, null, "WeTongji.apk");
		id=manager.enqueue(request);
	}
	
	private class DownloadEventReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
				installUpdate();
			}
		}
		
		private void installUpdate(){
			Intent install = new Intent(Intent.ACTION_VIEW);
			install.setDataAndType(manager.getUriForDownloadedFile(id), "application/vnd.android.package-archive");
			startActivity(install);
		}

	}
	
}
