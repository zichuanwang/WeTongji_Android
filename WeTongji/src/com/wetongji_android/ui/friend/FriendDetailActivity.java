package com.wetongji_android.ui.friend;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.event.EventsListActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseDetailActivity;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.image.ImageUtil;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class FriendDetailActivity extends WTBaseDetailActivity implements
		LoaderCallbacks<HttpRequestResult> {
	private TextView tvFriendWords;
	private TextView tvFriendDepartment;
	private Button ibFriend;
	private RelativeLayout rlFriendList;
	private TextView tvFriendNum;
	private RelativeLayout rlPartEvents;
	private TextView tvEventsNum;
	private TextView tvMajor;
	private TextView tvGrade;
	private TextView tvEmail;
	private TextView tvPhone;
	
	private User mUser;
	private AQuery mAq;

	private boolean bIsFriend;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		receiveData();

		setContentView(R.layout.activity_friend_detail);

		initWidget();
	}

	@Override
	protected void setShareContent(String shareContent) {
		super.setShareContent(shareContent);
	}

	private void receiveData() {
		Intent intent = getIntent();
		mUser = intent.getExtras().getParcelable(
				FriendListFragment.BUNDLE_KEY_USER);
		setShareContent("My friend--" + mUser.getName());
		setiChildId(mUser.getUID());
		setModelType("User");
		setCanLike(mUser.isCanLike());
		setLike(mUser.getLike());
		String avatar = mUser.getAvatar();
		setImagePath(avatar);
	}

	private void initWidget() {
		setTitle(mUser.getName());
		mAq = WTApplication.getInstance().getAq(this);
		tvFriendWords = (TextView) findViewById(R.id.text_profile_words);
		tvFriendWords.setText(mUser.getWords());
		tvFriendDepartment = (TextView) findViewById(R.id.text_profile_gender);
		int gendarRid = mUser.getGender().equals("男") ? R.drawable.ic_gender_male
				: R.drawable.ic_gender_female;
		Drawable gendarDrawable = getResources().getDrawable(gendarRid);
		tvFriendDepartment.setCompoundDrawablesWithIntrinsicBounds(
				gendarDrawable, null, null, null);
		tvFriendDepartment.setText(mUser.getDepartment());
		ibFriend = (Button) findViewById(R.id.btn_profile_action);
		bIsFriend = mUser.isIsFriend();
		if (bIsFriend) {
			ibFriend.setText(R.string.button_unfriend);
		} else {
			ibFriend.setText(R.string.button_friend);
		}
		ibFriend.setOnClickListener(new ClickListener());
		// Set Avatar
		mAq.id(R.id.img_profile_avatar).image(mUser.getAvatar(), true, true, 0,
				0, new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv,
							Bitmap bm, AjaxStatus status) {
						iv.setImageBitmap(bm);
						setHeadBluredBg(bm);
					}
				});
		rlFriendList = (RelativeLayout) findViewById(R.id.ll_friend_detail_list);
		rlFriendList.setOnClickListener(new ClickListener());
		tvFriendNum = (TextView) findViewById(R.id.tv_detail_friend_num);
		StringBuilder sb = new StringBuilder();
		sb.append(mUser.getFriendCount()).append(" Friends");
		tvFriendNum.setText(sb.toString());
		rlPartEvents = (RelativeLayout) findViewById(R.id.ll_friend_detail_part_events);
		rlPartEvents.setOnClickListener(new ClickListener());
		tvEventsNum = (TextView) findViewById(R.id.tv_detail_friend_part_events_num);
		sb.delete(0, sb.length());
		sb.append(mUser.getScheduleCount().getActivity()).append(" Events");
		tvEventsNum.setText(sb.toString());
		tvMajor = (TextView) findViewById(R.id.tv_friend_detail_major);
		tvMajor.setText(mUser.getMajor());
		tvGrade = (TextView) findViewById(R.id.tv_friend_detail_grade);
		tvGrade.setText(mUser.getYear());
		tvEmail = (TextView) findViewById(R.id.tv_friend_detail_email);
		tvEmail.setText(mUser.getEmail());
		tvPhone = (TextView)findViewById(R.id.tv_friend_detail_phone);
		tvPhone.setText(mUser.getPhone());
		ImageView ivEmail = (ImageView) findViewById(R.id.img_send_email);
		ivEmail.setOnClickListener(new ClickListener());
		ImageView ivMsg = (ImageView) findViewById(R.id.img_send_msg);
		ivMsg.setOnClickListener(new ClickListener());
		ImageView ivPhone = (ImageView) findViewById(R.id.img_make_call);
		ivPhone.setOnClickListener(new ClickListener());
	}

	private void showToast() {
		if (bIsFriend) {
			Toast.makeText(this,
					this.getResources().getString(R.string.add_friend_request),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					this,
					this.getResources().getString(
							R.string.remove_friend_request), Toast.LENGTH_SHORT)
					.show();
			ibFriend.setText(R.string.button_unfriend);
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
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(this, HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		if (result.getResponseCode() == 0) {
			getSupportLoaderManager().destroyLoader(loader.getId());
			if (bIsFriend) {
				bIsFriend = false;
			} else {
				bIsFriend = true;
			}
			showToast();
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {

	}

	private void addFriend(String id) {
		ApiHelper apiHelper = ApiHelper.getInstance(this);
		Bundle bundle = apiHelper.friendOpWithStatus(id, bIsFriend);
		getSupportLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
	}

	private void removeFriend(String id) {
		ApiHelper apiHelper = ApiHelper.getInstance(this);
		Bundle bundle = apiHelper.friendOpWithStatus(id, bIsFriend);
		getSupportLoaderManager().restartLoader(
				WTApplication.NETWORK_LOADER_DEFAULT, bundle, this);
	}

	class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ll_friend_detail_list) {
				Intent intent = new Intent(FriendDetailActivity.this,
						FriendListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(WTBaseFragment.BUNDLE_KEY_UID, mUser.getUID());
				bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
						FriendDetailActivity.this.getClass().getSimpleName());
				intent.putExtras(bundle);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in,
						R.anim.slide_left_out);
			} else if (v.getId() == R.id.btn_profile_action) {
				if (bIsFriend) {
					removeFriend(mUser.getUID());
				} else {
					addFriend(mUser.getUID());
				}
			} else if(v.getId() == R.id.ll_friend_detail_part_events) {
				if(mUser.getScheduleCount().getActivity() == 0) {
					Toast.makeText(FriendDetailActivity.this, getResources().getString(R.string.profile_no_attend_events), 
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(FriendDetailActivity.this, EventsListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE, "Friend");
					intent.putExtras(bundle);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.img_send_email) {
				Uri emailUri = Uri.parse("mailto:" + mUser.getEmail());
		        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);                                   
		        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WeTongji Android Feedbacks");
		        try {
		        	startActivity(Intent.createChooser(emailIntent, 
							getString(R.string.title_choose_app_send_email)));
		        } catch (ActivityNotFoundException e) {
		        	Toast.makeText(FriendDetailActivity.this, R.string.toast_no_email_app, Toast.LENGTH_LONG).show();
		        }
			} else if (v.getId() == R.id.img_send_msg) {
				Uri msgUri = Uri.parse("sms:" + mUser.getPhone());
				Intent msgIntent = new Intent(Intent.ACTION_VIEW, msgUri);
				msgIntent.putExtra("sms_body", "I am using WeTongji");
				try {
					startActivity(msgIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(FriendDetailActivity.this, R.string.toast_no_email_app, Toast.LENGTH_LONG).show();
				}
			} else if (v.getId() == R.id.img_make_call) {
				Uri phoneUri = Uri.parse("tel:" + mUser.getPhone());
				Intent phoneIntent = new Intent(Intent.ACTION_CALL, phoneUri);
				try {
					startActivity(phoneIntent);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(FriendDetailActivity.this, R.string.toast_no_email_app, Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	protected void updateObjectInDB() {

	}

	@Override
	protected void updateDB() {
		
	}	
}
