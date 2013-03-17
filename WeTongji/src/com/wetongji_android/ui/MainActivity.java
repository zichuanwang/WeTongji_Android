package com.wetongji_android.ui;

import com.wetongji_android.R;
import com.wetongji_android.util.net.WTAsyncTask;

import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends UpdateBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getUpdateTask=new GetUpdateTask();
		getUpdateTask.executeOnExecutor(WTAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
