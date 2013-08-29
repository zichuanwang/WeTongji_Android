package com.wetongji_android.ui.informations;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.data.Information;
import com.wetongji_android.factory.InformationFactory;
import com.wetongji_android.ui.friend.FriendDetailActivity;
import com.wetongji_android.ui.people.PersonDetailActivity;
import com.wetongji_android.ui.people.PersonDetailPicPagerAdapter;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTFullScreenActivity;
import com.wetongji_android.util.date.DateParser;

public class InformationDetailActivity extends WTBaseDetailActivity implements
		OnClickListener {

	private Information mInfo;
	private AQuery mAq;
	private ViewPager vpPics;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		receiveInformation();

		setContentView(R.layout.activity_information_detail);

		initWidget();
	}

	private void initWidget() {
		setTitle(getResources().getString(R.string.profile_section_new));
		mAq = WTApplication.getInstance().getAq(this);
		if (mInfo.getCategory().equals("社团通告")) {
			// Set the organization avatar
			mAq.id(R.id.info_detail_avatar).image(mInfo.getOrganizerAvatar(),
					false, true, 0, R.drawable.image_place_holder, null,
					AQuery.FADE_IN, 1.0f);
		} else {
			ImageView ivAvatar = (ImageView) findViewById(R.id.info_detail_avatar);
			ivAvatar.setVisibility(View.GONE);
		}

		TextView tvTitle = (TextView) findViewById(R.id.info_detail_title);
		TextView tvLocation = (TextView) findViewById(R.id.info_detail_location);

		TextView tvTime = (TextView) findViewById(R.id.info_detail_time);
		TextView tvContent = (TextView) findViewById(R.id.info_detail_content);
		TextView tvTicketInfo = (TextView) findViewById(R.id.information_ticket_info);

		setImages();

		if (!TextUtils.isEmpty(mInfo.getTicketService())) {
			tvTicketInfo.setText(mInfo.getTicketService());
			findViewById(R.id.icon_ticket).setVisibility(View.VISIBLE);
			TextView contact = (TextView) findViewById(R.id.info_detail_ticket_contact);
			if (!TextUtils.isEmpty(mInfo.getContact())) {
				contact.setText(mInfo.getContact());
				contact.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.local_contact_icon, 0, 0, 0);
				contact.setOnClickListener(new OnPicClickListener());
			} else {
				contact.setVisibility(View.GONE);
			}
		} else {
			findViewById(R.id.title_info_bar).setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(mInfo.getLocation())) {
			((TextView) findViewById(R.id.ticket_detail_location))
					.setText(mInfo.getLocation());
		} else {
			findViewById(R.id.ticket_detail_location).setVisibility(View.GONE);
		}

		tvTitle.setText(mInfo.getTitle());
		if (mInfo.getCategory().equals("社团通告")) {
			tvLocation.setText(mInfo.getOrganizer());
		} else {
			tvLocation.setText(mInfo.getSource());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.setMargins((int) 1.5, 15, 0, 0);
			tvLocation.setLayoutParams(params);
		}
		tvTime.setText(DateParser.parseDateForInformation(mInfo.getCreatedAt()));
		tvContent.setText(mInfo.getContext());
	}

	private void setImages() {
		vpPics = (ViewPager) findViewById(R.id.vp_information_images);
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.vp_indicator_infor);
		List<String> urls = mInfo.getImages();
		if (urls == null || urls.size() == 0) {

			vpPics.setVisibility(View.GONE);
			indicator.setVisibility(View.GONE);
			return;
		}
		List<View> lstImageViews = new ArrayList<View>();
		for (int i = 0; i < urls.size(); i++) {
			lstImageViews.add(this.getLayoutInflater().inflate(
					R.layout.page_information_pic, null));
			lstImageViews.get(i).setOnClickListener(this);
		}
		InfoDetailPicPagerAdapter adapter = new InfoDetailPicPagerAdapter(urls,
				this, lstImageViews);
		vpPics.setAdapter(adapter);

		indicator.setViewPager(vpPics);
		indicator.setFades(false);
	}

	private void receiveInformation() {
		Intent intent = getIntent();
		mInfo = intent
				.getParcelableExtra(InformationsFragment.BUNDLE_KEY_INFORMATION);
		setiChildId(String.valueOf(mInfo.getId()));
		setModelType("Information");
		setShareContent(mInfo.getTitle());
		setLike(mInfo.getLike());
		setCanLike(mInfo.isCanLike());
		String image = mInfo.getImages().isEmpty() ? null : mInfo.getImages()
				.get(0);
		setImagePath(image);
	}

	private class OnPicClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Uri phoneUri = Uri.parse("tel:" + mInfo.getContact());
			Intent phoneIntent = new Intent(Intent.ACTION_CALL, phoneUri);
			try {
				startActivity(phoneIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(InformationDetailActivity.this,
						R.string.toast_no_email_app, Toast.LENGTH_LONG).show();
			}
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

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(InformationDetailActivity.this,
				WTFullScreenActivity.class);
		Bundle bundle = new Bundle();
		String url = mInfo.getImages().get(vpPics.getCurrentItem());
		bundle.putString(WTBaseDetailActivity.IMAGE_URL, url);
		Bitmap bitmapTemp = mAq.getCachedImage(url);
		if (bitmapTemp != null) {
			bundle.putInt(WTBaseDetailActivity.IMAGE_WIDTH,
					bitmapTemp.getWidth());
			bundle.putInt(WTBaseDetailActivity.IMAGE_HEIGHT,
					bitmapTemp.getHeight());
		} else {
			Drawable drawable = getResources().getDrawable(
					R.drawable.image_place_holder);
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			bundle.putInt(WTBaseDetailActivity.IMAGE_WIDTH, bitmap.getWidth());
			bundle.putInt(WTBaseDetailActivity.IMAGE_HEIGHT, bitmap.getHeight());
		}

		intent.putExtras(bundle);
		startActivity(intent);
		this.overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_left_out);

	}
}
