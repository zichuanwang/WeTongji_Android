package com.wetongji_android.util.version;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.Version;
import com.wetongji_android.factory.VersionFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;

public class UpdateBaseActivity extends SlidingFragmentActivity 
implements LoaderCallbacks<HttpRequestResult>{
	
	private ApiHelper apiHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		apiHelper=ApiHelper.getInstance(this);
	}
	
	protected void checkUpdate(){
		Bundle args=apiHelper.getSystemVersion();
		getSupportLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT, args, this);
	}
	
	private void showUpdateInfo(Version version) throws NameNotFoundException{
		String current=getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		String latest=version.getLatest();
		if(!TextUtils.isEmpty(latest)&&current.compareTo(latest)<0){
			AlertDialog.Builder builder=new Builder(this);
			builder.setTitle(R.string.title_dialog_update_found);
			String description=version.getDescription();
			String updateInfo=String.format(getString(R.string.msg_dialog_update_found), latest, description);
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
	}
	
	private void startDownloadUpdate(String url){
		Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle bundle) {
		return new NetworkLoader(this, HttpMethod.Get, bundle);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0, HttpRequestResult result) {
		if(result.getResponseCode()==0){
			VersionFactory factory=new VersionFactory();
			try {
				Version version=factory.createObject(result.getStrResponseCon());
				showUpdateInfo(version);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}
	
}
