package com.wetongji_android.ui.informations;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTFullScreenActivity;
import com.wetongji_android.util.date.DateParser;

public class InformationDetailActivity extends WTBaseDetailActivity {
	
	private Information mInfo;
	private AQuery mAq;
	
	@Override
	protected void onCreate(Bundle arg0) 
	{
		super.onCreate(arg0);
		
		receiveInformation();
		
		setContentView(R.layout.activity_information_detail);

		initWidget();
	}

	private void initWidget() 
	{
		mAq = WTApplication.getInstance().getAq(this);
		// Set the organization avatar
		mAq.id(R.id.info_detail_avatar).image(mInfo.getOrganizerAvatar(), false, true,
				0, R.drawable.image_place_holder, null, AQuery.FADE_IN, 1.0f);

		Drawable drawable = getResources().getDrawable(
				R.drawable.image_place_holder);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (mInfo.getImages().size() != 0 && !mInfo.getImages().get(0).equals(WTApplication.MISSING_IMAGE_URL)) 
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

		TextView tvTitle = (TextView) findViewById(R.id.info_detail_title);
		TextView tvLocation = (TextView) findViewById(R.id.info_detail_location);
		TextView tvTime = (TextView) findViewById(R.id.info_detail_time);
		TextView tvContent = (TextView) findViewById(R.id.info_detail_content);

		tvTitle.setText(mInfo.getTitle());
		tvLocation.setText(mInfo.getCategory());
		tvTime.setText(DateParser.parseDateForInformation(mInfo.getCreatedAt()));
		tvContent.setText(mInfo.getContext());
	}

	private void receiveInformation()
	{
		Intent intent = getIntent();
		mInfo = intent.getParcelableExtra(InformationsFragment.BUNDLE_KEY_INFORMATION);
		setiChildId(String.valueOf(mInfo.getId()));
		setModelType("Information");
		setShareContent(mInfo.getTitle());
		setLike(mInfo.getLike());
		setCanLike(mInfo.isCanLike());
		String image = mInfo.getImages().isEmpty() ? null : mInfo.getImages().get(0);
		setImagePath(image);
	}
	
	private class OnPicClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
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

	@Override
	protected void updateObjectInDB() {
		ArrayList<Information> data = new ArrayList<Information>(1);
		mInfo.setLike(getLike());
		mInfo.setCanLike(!isCanLike());
		data.add(mInfo);
		InformationFactory factory = new InformationFactory(null);
		factory.saveObjects(this, data, false);
	}

	@Override
	protected void updateDB() {
		ArrayList<Information> data = new ArrayList<Information>(1);
		mInfo.setLike(getSchedule());
		mInfo.setCanLike(isbSchedule());
		data.add(mInfo);
		InformationFactory factory = new InformationFactory(null);
		factory.saveObjects(this, data, false);
	}
}
