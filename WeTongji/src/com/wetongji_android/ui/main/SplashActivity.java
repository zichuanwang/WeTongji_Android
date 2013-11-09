package com.wetongji_android.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wetongji_android.R;
import com.wetongji_android.ui.welcome.WelcomeActivity;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.version.UpdateBaseActivity;

/**
 * Created by leapoahead on 10/16/13.
 */
public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // IMPORTANT Getting auth
        ApiHelper.getInstance(this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
