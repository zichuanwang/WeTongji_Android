package com.wetongji_android.ui.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.flurry.android.FlurryAgent;
import com.wetongji_android.R;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.util.common.WTApplication;

public class AuthActivity extends BaseAuthActivity implements OnClickListener, OnCheckedChangeListener{
	
	public static final String TAG_LOGIN_FRAGMENT="loginFragment";
	public static final String TAG_REGISTER_FRAGMENT="registerFragment";
	
	private ToggleButton btnOnLogin;
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_authenticator);
		getSupportFragmentManager().beginTransaction()
			.add(R.id.auth_content_container, LoginFragment.newInstance(mUsername), TAG_LOGIN_FRAGMENT)
			.commit();
		setupActionBar();
		
		/** setBehindContentView make no sense but cleanging
		 * error caused by SlidingFragmentActivity
		 */
		setBehindContentView(R.layout.menu_frame);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		ActionBar ab=getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		View v=getLayoutInflater().inflate(R.layout.actionbar_authenticator, null);
		ab.setCustomView(v);
		btnOnLogin=(ToggleButton) v.findViewById(R.id.btn_on_login);
		btnOnLogin.setChecked(true);
		btnOnLogin.setOnCheckedChangeListener(this);
		Button btnNotNow=(Button) v.findViewById(R.id.btn_not_now);
		btnNotNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		Intent intent = new Intent(AuthActivity.this, MainActivity.class);
		intent.putExtra(MainActivity.PARAM_PREVIEW_WITHOUT_lOGIN, true);
		startActivity(intent);
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent intent=new Intent(this, IntroActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.pop_in, R.anim.slide_down_out);
		finish();
	}
	
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) 
     {  
         if (resultCode != RESULT_OK)  
             return;  
         switch (requestCode) 
         {  
             case PHOTO_PICKED_WITH_DATA: 
             {    
                 final Bitmap bm = data.getParcelableExtra("data");
                 saveBitmap(bm);
                 
                 RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager()
					.findFragmentByTag(TAG_REGISTER_FRAGMENT);
                 Bundle bundle = new Bundle();
                 bundle.putParcelable("cropedImage", bm);
                 bundle.putString("imagePath", UPLOAD_AVATAR.getPath());
                 fragment.setAvatar(bundle);
                 break;  
             }
             case CAMERA_WITH_DATA: {
                 doCropPhoto();
                 break;
             }
             case PHOTO_CROPED_WITH_DATA: {
					RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager()
							.findFragmentByTag(TAG_REGISTER_FRAGMENT);
					Bundle bundle = new Bundle();
					Bitmap bm = decodeUriAsBitmap(mUriTemp);
	            	bundle.putParcelable("cropedImage", bm);
	                bundle.putString("imagePath", mUriTemp.getPath());
					fragment.setAvatar(bundle);
             }
         }  
     }
}
