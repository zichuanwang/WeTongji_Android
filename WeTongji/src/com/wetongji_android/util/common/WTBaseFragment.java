package com.wetongji_android.util.common;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class WTBaseFragment extends SherlockFragment {
	public static final String BUNDLE_KEY_UID = "bundle_key_uid";
	public static final String BUNDLE_KEY_LIKE = "bundle_key_like";
	public static final String BUNDLE_KEY_MODEL_TYPE = "bundle_key_model";
	public static final String BUNDLE_KEY_START_MODE = "bundle_key_start_mode";
	public static final String BUNDLE_KEY_ACCOUNT = "bundle_key_account";
	
	protected boolean isFirstTimeStartFlag = true;
	public static final int FIRST_TIME_START = 0; //when activity is first time start
	public static final int SCREEN_ROTATE = 1;    //when activity is destroyed and recreated because a configuration change, see setRetainInstance(boolean retain)
	public static final int ACTIVITY_DESTROY_AND_CREATE = 2; 
	
	public StartMode mStartMode;
	
	protected int getCurrentState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            isFirstTimeStartFlag = false;
            return ACTIVITY_DESTROY_AND_CREATE;
        }

        if (!isFirstTimeStartFlag) {
            return SCREEN_ROTATE;
        }

        isFirstTimeStartFlag = false;
        return FIRST_TIME_START;
    }
	
	public static enum StartMode {
		BASIC, USERS, LIKE, FRIENDS, ATTEND
	}
}
