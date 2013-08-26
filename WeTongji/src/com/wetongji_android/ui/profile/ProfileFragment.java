package com.wetongji_android.ui.profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.factory.UserFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.course.CourseListActivity;
import com.wetongji_android.ui.event.EventsListActivity;
import com.wetongji_android.ui.friend.FriendListActivity;
import com.wetongji_android.ui.main.MainActivity;
import com.wetongji_android.ui.main.NotificationHandler;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.common.WTBaseFragment;
import com.wetongji_android.util.common.WTLikeListActivity;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.image.ImageUtil;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

public class ProfileFragment extends WTBaseFragment implements
		LoaderCallbacks<HttpRequestResult> {

	public static final String BUNDLE_USER = "BUNDLE_USER";

	public static final int REQUEST_CODE_PROFILE = 9912;

	public static final String BUNDLE_MOTTO = "BUNDLE_MOTTO";

	private User mUser;
	private String mStrImgLocalPath;
	private View mContentView;

	private TextView mTvWords;
	private TextView mTvCollege;
	private TextView mTvFriendsNum;
	private TextView mTvEventsLikes;
	private TextView mTvNewsLikes;
	private TextView mTvPeopleLikes;
	private TextView mTvOrgsLikes;
	private TextView mTvUserLikes;
	private TextView mTvParActivities;
	private TextView mTvParCourse;

	private RelativeLayout rlFriendsList;
	private RelativeLayout rlMyProfile;
	private RelativeLayout rlParActivities;
	private RelativeLayout rlParCourses;
	private RelativeLayout rlLikeEvents;
	private RelativeLayout rlLikeInfos;
	private RelativeLayout rlLikePeople;
	private RelativeLayout rlLikeOrganizations;
	private RelativeLayout rlLikeUsers;

	private ImageView mIvAvatar;
	private Button mBtnChangeAvatar;

	private Activity mActivity;

	private OnClickListener mClickListener = new ClickListener();

	public static ProfileFragment newInstance() {
		ProfileFragment fragment = new ProfileFragment();

		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		switch (getCurrentState(savedInstanceState)) {
		case FIRST_TIME_START:
			getLoaderManager()
					.initLoader(WTApplication.USER_LOADER, null, this);
			break;
		case SCREEN_ROTATE:
		case ACTIVITY_DESTROY_AND_CREATE:
			mUser = savedInstanceState.getParcelable(BUNDLE_USER);
			setWidgets();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_profile, null);
		mIvAvatar = (ImageView) mContentView
				.findViewById(R.id.img_profile_avatar);
		initWidgets(mContentView);

		return mContentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BUNDLE_USER, mUser);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = activity;
		((MainActivity) mActivity).getSupportActionBar()
				.setDisplayShowTitleEnabled(true);
	}

	@SuppressWarnings("deprecation")
	private void setHeadBluredBg(Bitmap source) {
		RelativeLayout rl = (RelativeLayout) mContentView
				.findViewById(R.id.layout_profile_header);
		int tH = (496 * 200 / 1080);
		/*int originWidth = source.getWidth();
		int originHeight = source.getHeight();
		int width = 200;
		
		Log.v("source height + width", "" + originWidth + " " + originHeight);
		if(width > originWidth) {
			width = originWidth;
		}
		
		if((100 + tH / 2) > originHeight) {
			tH = 2 * originHeight - 200;
		}*/
		Bitmap bm = Bitmap.createBitmap(source, 0, (100 - tH / 2), 200, tH);
		Bitmap bg = ImageUtil.fastblur(bm, 10);
		rl.setBackgroundDrawable(new BitmapDrawable(getActivity()
				.getResources(), bg));
	}

	private void initWidgets(View v) {
		mTvWords = (TextView) v.findViewById(R.id.text_profile_words);
		mTvCollege = (TextView) v.findViewById(R.id.text_profile_gender);
		mTvFriendsNum = (TextView) v.findViewById(R.id.text_profile_friend_num);
		mTvEventsLikes = (TextView) v
				.findViewById(R.id.text_profile_events_num);
		mTvNewsLikes = (TextView) v.findViewById(R.id.text_profile_news_num);
		mTvPeopleLikes = (TextView) v
				.findViewById(R.id.text_profile_people_num);
		mTvOrgsLikes = (TextView) v.findViewById(R.id.text_profile_orgs_num);
		mTvUserLikes = (TextView) v.findViewById(R.id.text_profile_user_num);
		mTvParActivities = (TextView) v
				.findViewById(R.id.text_profile_par_activities_num);
		mTvParCourse = (TextView) v
				.findViewById(R.id.text_profile_par_course_num);

		rlFriendsList = (RelativeLayout) v
				.findViewById(R.id.ll_profile_friend_list);
		rlFriendsList.setOnClickListener(new ClickListener());

		rlMyProfile = (RelativeLayout) v
				.findViewById(R.id.layout_profile_my_profile);
		rlMyProfile.setOnClickListener(mClickListener);

		mBtnChangeAvatar = (Button) v.findViewById(R.id.btn_profile_action);
		mBtnChangeAvatar.setOnClickListener(mClickListener);

		rlParActivities = (RelativeLayout) v
				.findViewById(R.id.ll_profile_activity_list);
		rlParActivities.setOnClickListener(mClickListener);

		rlParCourses = (RelativeLayout) v
				.findViewById(R.id.ll_profile_course_list);
		rlParCourses.setOnClickListener(mClickListener);

		rlLikeEvents = (RelativeLayout) v
				.findViewById(R.id.ll_profile_events_like);
		rlLikeEvents.setOnClickListener(mClickListener);

		rlLikeInfos = (RelativeLayout) v
				.findViewById(R.id.ll_profile_news_like);
		rlLikeInfos.setOnClickListener(mClickListener);

		rlLikePeople = (RelativeLayout) v
				.findViewById(R.id.ll_profile_people_like);
		rlLikePeople.setOnClickListener(mClickListener);

		rlLikeOrganizations = (RelativeLayout) v
				.findViewById(R.id.ll_profile_org_like);
		rlLikeOrganizations.setOnClickListener(mClickListener);

		rlLikeUsers = (RelativeLayout) v
				.findViewById(R.id.ll_profile_user_like);
		rlLikeUsers.setOnClickListener(mClickListener);
	}

	private void setWidgets() {
		if (!TextUtils.isEmpty(mUser.getWords())) {
			mTvWords.setText("\"" + mUser.getWords() + "\"");
		}
		int gendarRid = mUser.getGender().equals("男") ? R.drawable.ic_profile_gender_male
				: R.drawable.ic_profile_gender_female;
		Drawable gendarDrawable = getResources().getDrawable(gendarRid);
		mTvCollege.setCompoundDrawablesWithIntrinsicBounds(gendarDrawable,
				null, null, null);
		mTvCollege.setText(mUser.getDepartment());
		String fmt = getResources().getString(R.string.text_friends_counter);
		mTvFriendsNum.setText(String.format(fmt, mUser.getFriendCount()));

		String format = getResources().getString(R.string.format_likes);
		String fmtCourse = getResources().getString(R.string.format_attend_course);
		String fmtActivity = getResources().getString(R.string.format_attend_activity);
		
		mTvEventsLikes.setText(String.format(format, mUser.getLikeCount()
				.getActivity()));
		mTvNewsLikes.setText(String.format(format, mUser.getLikeCount()
				.getInformation()));
		mTvPeopleLikes.setText(String.format(format, mUser.getLikeCount()
				.getPerson()));
		mTvOrgsLikes.setText(String.format(format, mUser.getLikeCount()
				.getAccount()));
		mTvUserLikes.setText(String.format(format, mUser.getLikeCount()
				.getUser()));
		mTvParActivities.setText(String.format(fmtActivity, mUser.getScheduleCount()
				.getActivity()));
		mTvParCourse.setText(String.format(fmtCourse, mUser.getScheduleCount()
				.getCourse()));

		((MainActivity) mActivity).getSupportActionBar().setTitle(
				mUser.getName());
	}

	private void setAvatarFromUrl() {
		String strUrl = mUser.getAvatar() != null ? mUser.getAvatar() : "";
		AQuery aq = WTApplication.getInstance().getAq(getActivity());
		aq.id(R.id.img_profile_avatar).image(strUrl, true, true, 0, 0,
				new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv,
							Bitmap bm, AjaxStatus status) {
						iv.setImageBitmap(bm);
						setHeadBluredBg(bm);
					}
				});
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int loaderId, Bundle arg1) {
		NetworkLoader loader = null;
		if (loaderId == WTApplication.USER_LOADER) {
			Bundle bundle = ApiHelper.getInstance(getActivity()).getUserGet();
			loader = new NetworkLoader(getActivity(), HttpMethod.Get, bundle);
		} else if (loaderId == WTApplication.UPLOAD_AVATAR_LOADER) {
			Bundle bundle = ApiHelper.getInstance(getActivity())
					.postUserUpdateAvatar(mStrImgLocalPath);
			loader = new NetworkLoader(getActivity(), HttpMethod.Post, bundle);
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> loader,
			HttpRequestResult result) {
		if (loader.getId() == WTApplication.USER_LOADER) {
			JSONObject json = null;
			String strUser = null;
			if (result.getResponseCode() == 0) {
				try {
					json = new JSONObject(result.getStrResponseCon());
					strUser = json.getString("User");
				} catch (JSONException e) {
				}
				UserFactory factory = new UserFactory(this);
				mUser = factory.createSingleObject(strUser);

				setWidgets();
				setAvatarFromUrl();
			}
			getLoaderManager().destroyLoader(WTApplication.USER_LOADER);
		} else if (loader.getId() == WTApplication.UPLOAD_AVATAR_LOADER) {
			if (result.getResponseCode() == 0
					|| result.getResponseCode() == 200) {
				Toast.makeText(getActivity(), R.string.text_save_success,
						Toast.LENGTH_SHORT).show();
			}
			getLoaderManager()
					.destroyLoader(WTApplication.UPLOAD_AVATAR_LOADER);
			return;
		}
		if (result.getResponseCode() != 0) {
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		getSherlockActivity().getSupportActionBar()
				.setDisplayShowCustomEnabled(true);
		getSherlockActivity().getSupportActionBar().setCustomView(
				R.layout.actionbar_profile);
		getActivity().findViewById(R.id.notification_button)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						NotificationHandler.getInstance().finish();
						if (WTApplication.getInstance().hasAccount) {
							((MainActivity) getActivity()).showRightMenu();
						} else {
							Toast.makeText(
									getActivity(),
									getResources().getText(
											R.string.no_account_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public void updateMotto(String strNewMotto) {
		if (strNewMotto != null && !strNewMotto.equals(mUser.getWords())) {
			mTvWords.setText(strNewMotto);
			mUser.setWords(strNewMotto);
		}
	}

	public void setAvatar(Bundle bundle) {
		Bitmap bmAvatar = bundle.getParcelable("cropedImage");
		mStrImgLocalPath = bundle.getString("imagePath");
		mIvAvatar.setImageBitmap(bmAvatar);
		setHeadBluredBg(bmAvatar);

		getLoaderManager().restartLoader(WTApplication.UPLOAD_AVATAR_LOADER,
				bundle, this);
	}

	public Intent getSeeProfileIntent(Context context) {
		Intent intent = new Intent(context, ProfileInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(BUNDLE_USER, mUser);
		intent.putExtras(bundle);
		return intent;
	}

	class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.ll_profile_friend_list) {
				if (mUser.getFriendCount() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_events),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							FriendListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"ProfileFragment");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.layout_profile_my_profile) {
				if (mUser != null) {
					((MainActivity) getActivity()).doClickProfile();
				} else {
					// TODO toast message: no data
				}
			} else if (v.getId() == R.id.btn_profile_action) {
				((MainActivity) getActivity()).doPickPhotoAction();
			} else if (v.getId() == R.id.ll_profile_activity_list) {
				if (mUser.getScheduleCount().getActivity() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_attend_events),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							EventsListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"Activity");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_course_list) {
				if (mUser.getScheduleCount().getCourse() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_attend_courses),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							CourseListActivity.class);
					intent.putExtra(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_events_like) {
				if (mUser.getLikeCount().getActivity() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_events),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							WTLikeListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, true);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"Activity");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_news_like) {
				if (mUser.getLikeCount().getInformation() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_information),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							WTLikeListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, true);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"Information");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_people_like) {
				if (mUser.getLikeCount().getPerson() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_people),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							WTLikeListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, true);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"People");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_org_like) {
				if (mUser.getLikeCount().getAccount() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_accounts),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							WTLikeListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, true);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"Account");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			} else if (v.getId() == R.id.ll_profile_user_like) {
				if (mUser.getLikeCount().getUser() == 0) {
					Toast.makeText(
							mActivity,
							mActivity.getResources().getString(
									R.string.profile_no_like_accounts),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(mActivity,
							FriendListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(WTBaseFragment.BUNDLE_KEY_UID,
							mUser.getUID());
					bundle.putBoolean(WTBaseFragment.BUNDLE_KEY_LIKE, true);
					bundle.putString(WTBaseFragment.BUNDLE_KEY_MODEL_TYPE,
							"User");
					intent.putExtras(bundle);
					startActivity(intent);
					mActivity.overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
				}
			}
		}
	}
}
