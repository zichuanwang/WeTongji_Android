package com.wetongji_android.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.wetongji_android.R;
import com.wetongji_android.service.WeNotificationService;
import com.wetongji_android.ui.notification.NotificationFragment;
import com.wetongji_android.ui.now.NowFragment;
import com.wetongji_android.ui.profile.ProfileFragment;
import com.wetongji_android.ui.setting.WTSettingActivity;
import com.wetongji_android.ui.today.TodayFragment;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.version.UpdateBaseActivity;

public class MainActivity extends UpdateBaseActivity {
	private Fragment mContent;
	public static final String PARAM_PREVIEW_WITHOUT_lOGIN = "previewWithoutLogin";
    public static final String PARAM_CHECK_NOTIFICATION = "checknotificaiton";

    public static final int MSG_SHOW_RIGHT_MENU = 993;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NotificationHandler.init(this);

		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        } else {
            mContent = TodayFragment.newInstance();
        }

		// set the above view
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		setSlidingMenu();

        //DO NOT start notification service if the setting is off or there is no account
        SharedPreferences sp = getSharedPreferences(WTSettingActivity.PREFERENCES_FILE_NAME, MODE_PRIVATE);
        int type = sp.getInt(WTSettingActivity.PREFERENCE_INTERVAL, 0);
        if (WTApplication.getInstance().hasAccount && type > 0) {
            startService(new Intent(this, WeNotificationService.class));
        }

        if (getIntent().getBooleanExtra(PARAM_CHECK_NOTIFICATION, false)) {
            // show right menu with a delay
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == MSG_SHOW_RIGHT_MENU) {
                        showRightMenu();
                    }
                }
            };
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(MSG_SHOW_RIGHT_MENU);
                }
            }, 600);

        }
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void finish(){
		super.finish();
		TodayFragment.previousResultStr=null;
	}

	/**
	 * set attributes of the sliding menu
	 */
	private void setSlidingMenu() {
		// set slidingmenu properties
		final SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				if (sm.isSecondaryMenuShowing()) {
					NotificationHandler.getInstance().finish();
				}
			}

		});

		// set left menu
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

		// set right menu
		sm.setSecondaryMenu(R.layout.right_menu);
		sm.setSecondaryShadowDrawable(R.drawable.shadow);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.right_menu, new NotificationFragment()).commit();

		// setTitle();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        } else {
            mContent = TodayFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)  {
			return;
		}
        switch (requestCode)
        {
            case PHOTO_PICKED_WITH_DATA:
            {
            	Uri selectedImage = data.getData();
            	doCropPhoto(selectedImage, mUriTemp);
                break;
            }
            case CAMERA_WITH_DATA:
            {
                doCropPhoto(mUriTemp, mUriTemp);
                break;
            }
            case  PHOTO_CROPED_WITH_DATA: {
            	ProfileFragment fragment = (ProfileFragment) mContent;
            	Bundle bundle = new Bundle();
            	Bitmap bm = decodeUriAsBitmap(mUriTemp);
            	bundle.putParcelable("cropedImage", bm);
                bundle.putString("imagePath", mUriTemp.getPath());
                fragment.setAvatar(bundle);
            	break;
            }
            case ProfileFragment.REQUEST_CODE_PROFILE: {
            	ProfileFragment fragment = (ProfileFragment) mContent;
            	fragment.updateMotto(data.getStringExtra(ProfileFragment.BUNDLE_MOTTO));
            	break;
            }

        }
	}

	public void switchContent(Fragment fragment) {
		if((fragment instanceof NowFragment) || (fragment instanceof ProfileFragment))
		{
			if(WTApplication.getInstance().hasAccount)
			{
				mContent = fragment;
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			}else
			{
				Toast.makeText(this, getResources().getText(R.string.no_account_error), Toast.LENGTH_SHORT).show();
			}
		}else
		{
			mContent = fragment;
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isMenuShowing()) {
				finish();
			} else {
				getSlidingMenu().showMenu(true);
			}
			return true;
		} else {
			return false;
		}
	}

	public void showRightMenu()
	{
		if(getSlidingMenu().isSecondaryMenuShowing())
		{
			getSlidingMenu().showContent();
		}else
		{
			showSecondaryMenu();
		}
	}

	public void showLeftMenu() {
		if (getSlidingMenu().isShown()) {
			getSlidingMenu().showContent();
		} else {
			showMenu();
		}
	}

	public void doClickProfile() {
		ProfileFragment f = (ProfileFragment) mContent;
		startActivityForResult(f.getSeeProfileIntent(this), ProfileFragment.REQUEST_CODE_PROFILE);
		overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);
	}
}
