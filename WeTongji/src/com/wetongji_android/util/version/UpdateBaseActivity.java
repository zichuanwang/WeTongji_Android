package com.wetongji_android.util.version;

import org.json.JSONException;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.Version;
import com.wetongji_android.factory.VersionFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;

public class UpdateBaseActivity extends SlidingFragmentActivity implements LoaderCallbacks<String>{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkUpdate();
	}
	
	private void checkUpdate(){
		Bundle params=new Bundle();
		params.putString(WTApplication.API_ARGS_METHOD, WTApplication.API_METHOD_SYSTEM_VERSION);
		getSupportLoaderManager().initLoader(WTApplication.NETWORK_LOADER, params, this);
	}
	
	private void showUpdateInfo(Version version){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle(R.string.title_dialog_update_found);
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
		Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	@Override
	public Loader<String> onCreateLoader(int arg0, Bundle bundle) {
		return new NetworkLoader(this, HttpMethod.Get, bundle);
	}

	@Override
	public void onLoadFinished(Loader<String> arg0, String result) {
		Log.v("The result is : ", result);
		try {
			Version version=VersionFactory.create(result);
			showUpdateInfo(version);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) {
	}
	
}
