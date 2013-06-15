package com.wetongji_android.ui.people;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.data.Person;
import com.wetongji_android.ui.event.EventDetailActivity;

public class PersonDetailActivity extends SherlockFragmentActivity {

	private Person mPerson;
	private boolean mIsCurrent;
	
	
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
		
		//TODO set the viewpager
		
		setTextViews();
		
	}


	private void recieveData() {
		Intent intent = getIntent();
		mPerson = intent.getParcelableExtra(PeopleListFragment.BUNDLE_KEY_PERSON);
		mIsCurrent = intent.getBooleanExtra(PeopleListFragment.BUNDLE_KEY_IS_CURRENT, false);
	}
	
	private void setBackButton() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.event_detail_back);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				PersonDetailActivity.this.finish();
				PersonDetailActivity.this.overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		
		ImageButton btnShare = (ImageButton) findViewById(R.id.action_event_detail_share);
		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String vol = String.format(getString(R.string.share_person), mPerson.getNO());
				String content = vol + mPerson.getTitle() + "¡ª¡ª" + mPerson.getName();
				showShareDialog(content);
			}
		});
	}
	
	private void setTextViews() {
		TextView tvPersonName = (TextView) findViewById(R.id.tv_person_name);
		TextView tvPersonVol = (TextView) findViewById(R.id.tv_person_detail_vol);
		TextView tvPersonWords = (TextView) findViewById(R.id.tv_person_words);
		TextView tvPersonTitle = (TextView) findViewById(R.id.text_person_detail_title_name);
		TextView tvPersonContent = (TextView) findViewById(R.id.tv_person_detail_content);
		
		tvPersonName.setText(mPerson.getName());
		if (mIsCurrent) {
			tvPersonVol.setTextColor(getResources().getColor(R.color.tv_people_current_vol));
		} else {
			tvPersonVol.setText(String.format(getString(R.string.people_vol), mPerson.getNO()));
		}
		tvPersonWords.setText(mPerson.getWords());
		tvPersonTitle.setText(mPerson.getJobTitle());
		tvPersonContent.setText(mPerson.getDescription());
	}
	
	public void showShareDialog(String content) {
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
}
