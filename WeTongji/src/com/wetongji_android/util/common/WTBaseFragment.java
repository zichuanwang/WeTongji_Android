package com.wetongji_android.util.common;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;

public class WTBaseFragment extends SherlockFragment {
	
	protected boolean isFirstTimeStartFlag = true;
	public static final int FIRST_TIME_START = 0; //when activity is first time start
	public static final int SCREEN_ROTATE = 1;    //when activity is destroyed and recreated because a configuration change, see setRetainInstance(boolean retain)
	public static final int ACTIVITY_DESTROY_AND_CREATE = 2; 
	
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
}
