package com.wetongji_android.ui.people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;
import com.wetongji_android.factory.PersonFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class PersonDetailActivity extends SherlockFragmentActivity implements
		LoaderCallbacks<HttpRequestResult>, OnClickListener {

	private Person mPerson;
	private boolean mIsCurrent;

	private CheckBox mCbLike;
	private boolean isRestCheckBox = false;
	private TextView mTvLikeNum;

	private List<String> urls;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recieveData();
		setUpUI();
	}

	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_detail);

		setBackButton();

		setLikeCheckbox();

		setImages();

		setTextViews();
	}

	private void setLikeCheckbox() {
		mCbLike = (CheckBox) findViewById(R.id.cb_like);
		mTvLikeNum = (TextView) findViewById(R.id.tv_like_number);

		mCbLike.setChecked(!mPerson.isCanLike());
		mTvLikeNum.setText(String.valueOf(mPerson.getLike()));

		mCbLike.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (WTApplication.getInstance().hasAccount) {
					if (isRestCheckBox) {
						return;
					}

					int delat = isChecked ? 1 : -1;
					mTvLikeNum.setText(String.valueOf(mPerson.getLike() + delat));

					likePerson(isChecked);
				} else {
					mCbLike.setChecked(false);
					Toast.makeText(
							PersonDetailActivity.this,
							getResources().getString(
									R.string.need_account_login),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void recieveData() {
		Intent intent = getIntent();
		mPerson = intent
				.getParcelableExtra(PeopleListFragment.BUNDLE_KEY_PERSON);
		mIsCurrent = intent.getBooleanExtra(
				PeopleListFragment.BUNDLE_KEY_IS_CURRENT, false);
	}

	private void setTextViews() {
		TextView tvPersonName = (TextView) findViewById(R.id.tv_person_name);
		TextView tvPersonVol = (TextView) findViewById(R.id.tv_person_detail_vol);
		TextView tvPersonWords = (TextView) findViewById(R.id.tv_person_words);
		TextView tvPersonTitle = (TextView) findViewById(R.id.text_person_detail_title_name);
		TextView tvPersonContent = (TextView) findViewById(R.id.tv_person_detail_content);

		tvPersonName.setText(mPerson.getName());
		if (mIsCurrent) {
			tvPersonVol.setTextColor(getResources().getColor(
					R.color.tv_people_current_vol));
		} else {
			tvPersonVol.setText(String.format(getString(R.string.people_vol),
					mPerson.getNO()));
		}
		tvPersonWords.setText(mPerson.getWords());
		tvPersonTitle.setText(mPerson.getJobTitle());
		tvPersonContent.setText(mPerson.getDescription());
	}

	private void setImages() {
		ViewPager vpPics = (ViewPager) findViewById(R.id.vp_person_images);
		PersonDetailPicPagerAdapter adapter = new PersonDetailPicPagerAdapter(
				mPerson.getImages(), this);
		vpPics.setAdapter(adapter);
		Set<String> keys = mPerson.getImages().keySet();
		List<String> lstKeys = new ArrayList<String>();
		for(String key : keys) {
			lstKeys.add(key);
		}
		Collections.reverse(lstKeys);
		urls = lstKeys;
		List<View> lstImageViews = new ArrayList<View>();
		for(int i = 0; i < urls.size(); i++) {
			lstImageViews.add(this.getLayoutInflater().inflate(R.layout.page_person_pic, null));
			lstImageViews.get(i).setOnClickListener(this);
		}
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.vp_indicator_person);
		indicator.setViewPager(vpPics);
		indicator.setFades(false);

		AQuery aq = WTApplication.getInstance().getAq(this);
		aq.id(R.id.img_people_detail_avatar).image(mPerson.getAvatar(), true,
				true, 300, R.drawable.event_list_thumbnail_place_holder, null,
				AQuery.FADE_IN_NETWORK, 1.0f);
	}

	private void setBackButton() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.detail_back);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				PersonDetailActivity.this.finish();
				PersonDetailActivity.this.overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});

		ImageButton btnShare = (ImageButton) findViewById(R.id.action_person_detail_share);
		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showShareDialog("Tongji University Every Week Person--"
						+ mPerson.getTitle());
			}
		});
	}

	private void showShareDialog(String content) {
		String sourceDesc = getResources().getString(R.string.share_from_we);
		String share = getResources().getString(R.string.test_share);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Intent.EXTRA_TEXT, content + sourceDesc);
		intent.setType("text/*");
		intent.setType("image/*");
		startActivity(Intent.createChooser(intent, share));
	}

	private void likePerson(boolean isLike) {
		ApiHelper apiHelper = ApiHelper.getInstance(this);
		getSupportLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_LIKE,
				apiHelper.setObjectLikedWithModelType(isLike,
						String.valueOf(mPerson.getId()), "Person"), this);
	}

	private void updatePersonInDB() {
		PersonFactory personFactory = new PersonFactory(null);
		List<Person> people = new ArrayList<Person>();
		Person person = mPerson;
		person.setLike(mPerson.getLike() + (mCbLike.isChecked() ? 1 : -1));
		person.setCanLike(!mCbLike.isChecked());
		people.add(person);
		personFactory.saveObjects(PersonDetailActivity.this, people, false);
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		if (result.getResponseCode() == 0) {
			updatePersonInDB();
			if (mCbLike.isChecked()) {
				Toast.makeText(this, "Like Success", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "DisLike Success", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {

	}

	@Override
	public void finish() {
		Intent intent = getIntent();
		intent.putExtra(WTBaseDetailActivity.KEY_CAN_LIKE, !mCbLike.isChecked());
		intent.putExtra(WTBaseDetailActivity.KEY_OBJECT_ID, mPerson.getId());
		intent.putExtra(WTBaseDetailActivity.KEY_LIKE_NUMBER,
				Integer.valueOf(mTvLikeNum.getText().toString()));
		this.setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onClick(View v) {
		Log.v("shit", "fuck");
	}
}
