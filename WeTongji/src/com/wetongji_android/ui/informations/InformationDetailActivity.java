package com.wetongji_android.ui.informations;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTFullScreenActivity;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpUtil;

public class InformationDetailActivity extends WTBaseDetailActivity 
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
					mInfo.getImages().get(0), false, true,
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
	}

	private void receiveInformation()
	{
		Intent intent = getIntent();
		mInfo = intent
				.getParcelableExtra(InformationsFragment.BUNDLE_KEY_INFORMATION);
	}

	private void setLikeCheckbox() 
	{
		mCbLike = (CheckBox) findViewById(R.id.cb_like);
		mTvLikeNum = (TextView) findViewById(R.id.tv_like_number);

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
		//getSupportLoaderManager().restartLoader(
				//WTApplication.INFORMATION_LIKE_LOADER, bundle, this);
	}

	private void updateInfoInDB() 
	{
		InformationFactory infoFactory = new InformationFactory(null);
		List<Information> infos = new ArrayList<Information>();
		Information info = mInfo;
		info.setLike(info.getLike() + (mCbLike.isChecked() ? 1 : -1));
		info.setCanLike(!mCbLike.isChecked());
		infos.add(info);
		//infoFactory.saveObjects(InformationDetailActivity.this, infos);
	}
	
	private class OnPicClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			Intent intent = new Intent(InformationDetailActivity.this, WTFullScreenActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(IMAGE_URL, mInfo.getImages().get(0));
			
			Bitmap bitmapTemp = mAq.getCachedImage(mInfo.getImages().get(0));
			if(bitmapTemp != null)
			{
				bundle.putInt(IMAGE_WIDTH, bitmapTemp.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmapTemp.getHeight());
			}else
			{
				Drawable drawable = getResources().getDrawable(R.drawable.image_place_holder);
				Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
				bundle.putInt(IMAGE_WIDTH, bitmap.getWidth());
				bundle.putInt(IMAGE_HEIGHT, bitmap.getHeight());
			}
			
			intent.putExtras(bundle);
			startActivity(intent);
			InformationDetailActivity.this.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		}
	}
}
