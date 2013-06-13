package com.wetongji_android.ui.informations;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;
import com.wetongji_android.util.net.HttpUtil;

public class InformationDetailActivity extends SherlockFragmentActivity
		implements LoaderCallbacks<HttpRequestResult> 
{
	private Information mInfo;

	private AQuery mAq;

	private CheckBox mCbLike;
	private TextView mTvLikeNum;

	private boolean isRestCheckBox = false;

	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_information_detail);

		receiveInformation();

		initWidget();
	}

	private void initWidget() 
	{
		//getSupportActionBar().setDisplayShowTitleEnabled(false);

		mAq = WTApplication.getInstance().getAq(this);
		// Set the organization avatar
		mAq.id(R.id.info_detail_avatar).image(
				HttpUtil.replaceURL(mInfo.getOrganizerAvatar()), false, true,
				0, R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);

		Drawable drawable = getResources().getDrawable(
				R.drawable.image_place_holder);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (!mInfo.getImages().get(0).equals(WTApplication.MISSING_IMAGE_URL)) 
		{
			mAq.id(R.id.info_detail_image).image(
					HttpUtil.replaceURL(mInfo.getImages().get(0)), false, true,
					0, R.drawable.image_place_holder, bitmap, AQuery.FADE_IN,
					1.0f);
		} else 
		{
			mAq.id(R.id.info_detail_image).visibility(View.GONE);
		}

		ImageView detailImage = (ImageView)findViewById(R.id.info_detail_image);
		detailImage.setOnClickListener(new OnPicClickListener());
		
		setLikeCheckbox();

		TextView tvTitle = (TextView) findViewById(R.id.info_detail_title);
		TextView tvLocation = (TextView) findViewById(R.id.info_detail_location);
		TextView tvTime = (TextView) findViewById(R.id.info_detail_time);
		TextView tvContent = (TextView) findViewById(R.id.info_detail_content);

		tvTitle.setText(mInfo.getTitle());
		tvLocation.setText(mInfo.getSummary());
		tvTime.setText(DateParser.parseDateForInformation(mInfo.getCreatedAt()));
		tvContent.setText(mInfo.getContext());

		LinearLayout ll = (LinearLayout) findViewById(R.id.info_detail_back);
		ll.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				InformationDetailActivity.this.finish();
				InformationDetailActivity.this.overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});

		ImageButton btnShare = (ImageButton) findViewById(R.id.action_info_detail_share);
		btnShare.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				showShareDialog(mInfo.getTitle());
			}
		});
	}

	private void receiveInformation()
	{
		Intent intent = getIntent();
		mInfo = intent
				.getParcelableExtra(InformationsFragment.BUNDLE_KEY_INFORMATION);
	}

	private void setLikeCheckbox() 
	{
		mCbLike = (CheckBox) findViewById(R.id.cb_info_like);
		mTvLikeNum = (TextView) findViewById(R.id.tv_info_like_number);

		mCbLike.setChecked(!mInfo.isCanLike());
		mTvLikeNum.setText(String.valueOf(mInfo.getLike()));

		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) 
			{
				if (WTApplication.getInstance().hasAccount)
				{
					if (isRestCheckBox) 
					{
						return;
					}

					int delat = isChecked ? 1 : -1;
					mTvLikeNum.setText(String.valueOf(mInfo.getLike() + delat));

					likeInfo(isChecked);
				} else 
				{
					mCbLike.setChecked(false);
					Toast.makeText(
							InformationDetailActivity.this,
							getResources().getString(
									R.string.need_account_login),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void likeInfo(boolean isLike) 
	{
		ApiHelper apihelper = ApiHelper
				.getInstance(InformationDetailActivity.this);
		Bundle bundle = isLike ? apihelper.likeInfo(mInfo.getId()) : apihelper
				.unLikeInfo(mInfo.getId());
		getSupportLoaderManager().restartLoader(
				WTApplication.INFORMATION_LIKE_LOADER, bundle, this);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1)
	{
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) 
	{
		if (arg1.getResponseCode() == 0) 
		{
			Toast.makeText(
					this,
					getResources().getString(
							R.string.text_u_like_this_information),
					Toast.LENGTH_SHORT).show();
			updateInfoInDB();
		} else 
		{
			// When network error occurs, the checkbox state should be reset
			ExceptionToast.show(this, arg1.getResponseCode());
			mTvLikeNum.setText(String.valueOf(mInfo.getLike()));
			isRestCheckBox = true;
			mCbLike.setChecked(!mCbLike.isChecked());
			isRestCheckBox = false;
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) 
	{
	}

	private void updateInfoInDB() 
	{
		InformationFactory infoFactory = new InformationFactory(null);
		List<Information> infos = new ArrayList<Information>();
		Information info = mInfo;
		info.setLike(info.getLike() + (mCbLike.isChecked() ? 1 : -1));
		info.setCanLike(!mCbLike.isChecked());
		infos.add(info);
		infoFactory.saveObjects(InformationDetailActivity.this, infos);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			finish();
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_right_out);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void showShareDialog(String content) 
	{
		String sourceDesc = getResources().getString(R.string.share_from_we);
		String share = getResources().getString(R.string.test_share);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.putExtra(Intent.EXTRA_TEXT, mEvent.getDescription());
		intent.putExtra(Intent.EXTRA_TEXT, content + sourceDesc);
		// intent.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(mAq.getCachedFile(mEvent.getImage())));
		intent.setType("text/*");
		intent.setType("image/*");
		startActivity(Intent.createChooser(intent, share));
	}
	
	private class OnPicClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			
		}
	}
}
