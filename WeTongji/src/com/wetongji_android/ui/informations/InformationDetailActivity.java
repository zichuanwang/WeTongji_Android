package com.wetongji_android.ui.informations;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.date.DateParser;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class InformationDetailActivity extends FragmentActivity 
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
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_information_detail);
		
		receiveInformation();
		
		initWidget();
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void initWidget()
	{
		mAq = WTApplication.getInstance().getAq(this);
		//Set the organization avatar
		mAq.id(R.id.info_detail_avatar).image(mInfo.getOrganizerAvatar(), false, true, 0, R.drawable.image_place_holder,
				null, AQuery.FADE_IN, 1.0f);
		
		Drawable drawable = getResources().getDrawable(R.drawable.image_place_holder);
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		if(!mInfo.getImages().get(0).equals(WTApplication.MISSING_IMAGE_URL))
		{
			Log.v("here", "not missing");
			
			mAq.id(R.id.info_detail_image).image(mInfo.getImages().get(0), false, true, 0, R.drawable.image_place_holder,
					bitmap, AQuery.FADE_IN, 1.0f);
		}else
		{
			mAq.id(R.id.info_detail_image).visibility(View.GONE);
		}
		
		setLikeCheckbox();
		
		TextView tvTitle = (TextView)findViewById(R.id.info_detail_title);
		TextView tvLocation = (TextView)findViewById(R.id.info_detail_location);
		TextView tvTime = (TextView)findViewById(R.id.info_detail_time);
		TextView tvContent = (TextView)findViewById(R.id.info_detail_content);
		
		tvTitle.setText(mInfo.getTitle());
		tvLocation.setText(mInfo.getSummary());
		tvTime.setText(DateParser.parseDateForInformation(mInfo.getCreatedAt()));
		tvContent.setText(mInfo.getContext());
		
		
	}
	
	private void receiveInformation()
	{
		Intent intent = getIntent();
		mInfo = intent.getParcelableExtra(InformationsFragment.BUNDLE_KEY_INFORMATION);
	}
	
	private void setLikeCheckbox()
	{
		mCbLike = (CheckBox)findViewById(R.id.cb_info_like);
		mTvLikeNum = (TextView)findViewById(R.id.tv_info_like_number);
		
		mCbLike.setChecked(!mInfo.isCanLike());
		mTvLikeNum.setText(String.valueOf(mInfo.getLike()));
		
		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) 
			{
				// TODO Auto-generated method stub
				if(isRestCheckBox)
				{
					return;
				}
				
				int delat = isChecked ? 1 : -1;
				mTvLikeNum.setText(String.valueOf(mInfo.getLike() + delat));
				
				likeInfo(isChecked);
			}
		});
	}
	
	private void likeInfo(boolean isLike)
	{
		ApiHelper apihelper = ApiHelper.getInstance(InformationDetailActivity.this);
		Bundle bundle = isLike ? apihelper.likeInfo(mInfo.getId()) : apihelper.unLikeInfo(mInfo.getId());
		getSupportLoaderManager().restartLoader(WTApplication.INFORMATION_LIKE_LOADER, bundle, this);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) 
	{
		// TODO Auto-generated method stub
		if(arg1.getResponseCode() == 0)
		{
			updateInfoInDB();
		}else
		{
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
		// TODO Auto-generated method stub
		
	}
	
	private void updateInfoInDB()
	{
		
	}
}
