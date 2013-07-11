package com.wetongji_android.ui.account;

import android.os.Bundle;

import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseDetailActivity;

public class AccountDetailActivity extends WTBaseDetailActivity {

	public static final String BUNDLE_KEY_ACCOUNT = "BUNDLE_KEY_ACCOUNT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_account_detail);
	}
	
}
