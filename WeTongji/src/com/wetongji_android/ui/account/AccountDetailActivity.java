package com.wetongji_android.ui.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.wetongji_android.R;
import com.wetongji_android.data.Account;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.image.ImageUtil;

public class AccountDetailActivity extends WTBaseDetailActivity {

	private Account mAccount;
	private AQuery mAq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		recieveData();
		setContentView(R.layout.activity_account_detail);

		mAq = WTApplication.getInstance().getAq(this);
		setUpUI();
	}

	private void setUpUI() {
		Button button = (Button) findViewById(R.id.btn_profile_action);
		button.setText("Contact");
		TextView tvAccountActivityNum = (TextView) findViewById(R.id.text_account_activity_num);
		TextView tvClubNewsNum = (TextView) findViewById(R.id.text_club_news_num);
		TextView tvClubAbout = (TextView) findViewById(R.id.text_account_detail_about);
		TextView tvAccountName = (TextView) findViewById(R.id.text_profile_words);
		// set the gender text invisible
		((TextView) findViewById(R.id.text_profile_gender))
				.setVisibility(View.GONE);
		tvAccountActivityNum
				.setText(String.format(getString(R.string.format_items),
						mAccount.getActivitiesCount()));
		tvClubNewsNum.setText(String.format(getString(R.string.format_items),
				mAccount.getInformationCount()));
		tvClubAbout.setText(mAccount.getDescription());
		tvAccountName.setText(mAccount.getDisplay());

		mAq.id(R.id.img_profile_avatar).image(mAccount.getImage(), true, true,
				200, 0);
		new FetchBgTask().execute();
	}

	private void recieveData() {
		Intent intent = getIntent();
		mAccount = intent.getParcelableExtra(BUNDLE_KEY_ACCOUNT);
		setiChildId(String.valueOf(mAccount.getId()));
		setCanLike(mAccount.isCanLike());
		setLike(mAccount.getLike());
		setModelType("Account");
	}

	private void fetchBackground() {
		if (!TextUtils.isEmpty(mAccount.getBackground())) {
			mAq.id(R.id.img_account_background_temp).image(
					mAccount.getBackground(), true, true, 0, 0,
					new BitmapAjaxCallback() {
						@Override
						protected void callback(String url, ImageView iv,
								Bitmap bm, AjaxStatus status) {
							setHeadBluredBg(bm);
						}
					});
		}
	}

	@SuppressWarnings("deprecation")
	private void setHeadBluredBg(Bitmap resource) {
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_profile_header);
		int tH = (496 * 200 / 1080);
		Bitmap bm = Bitmap.createBitmap(resource, 0, (100 - tH / 2), 200, tH);
		Bitmap bg = ImageUtil.fastblur(bm, 10);
		rl.setBackgroundDrawable(new BitmapDrawable(getResources(), bg));
	}

	@Override
	protected void updateObjectInDB() {
		// Here we need not store data in DB
	}

	private class FetchBgTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			fetchBackground();
			return null;
		}
	}
}
