package com.wetongji_android.ui.profile;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.wetongji_android.R;
import com.wetongji_android.data.User;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.data.DbListLoader;
import com.wetongji_android.util.data.user.UserLoader;
import com.wetongji_android.util.image.ImageUtil;

public class ProfileFragment extends SherlockFragment implements LoaderCallbacks<List<User>>{

	private User mUser;
	
	private TextView mTvWords;
	private TextView mTvCollege;
	private TextView mTvFriendsNum;
	private TextView mTvEventsLikes;
	private TextView mTvNewsLikes;
	private TextView mTvPeopleLikes;
	private TextView mTvOrgsLikes;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, null);
		
		setHeadBluredBg(view);
		initWidgets(view);
		
		this.getLoaderManager().initLoader(WTApplication.USER_LOADER, null, this);
		
		return view;
	}
	
	@SuppressWarnings("deprecation")
	private void setHeadBluredBg(View view) {
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.layout_profile_header);
		Bitmap resource = BitmapFactory.decodeResource(getResources(), R.drawable.test_avatar);
		int tH =  (496 * 200 / 1080);
		Bitmap bm = Bitmap.createBitmap(resource, 0, (100 - tH / 2), 200, tH);
		Bitmap bg = ImageUtil.fastblur(bm, 10);
		rl.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), bg));
	}
	
	private void initWidgets(View v) {
		mTvWords = (TextView) v.findViewById(R.id.text_profile_words);
		mTvCollege = (TextView) v.findViewById(R.id.text_profile_gender);
		mTvFriendsNum = (TextView) v.findViewById(R.id.text_profile_friend_num);
		mTvEventsLikes = (TextView) v.findViewById(R.id.text_profile_events_num);
		mTvNewsLikes = (TextView) v.findViewById(R.id.text_profile_news_num);
		mTvPeopleLikes = (TextView) v.findViewById(R.id.text_profile_people_num);
		mTvOrgsLikes = (TextView) v.findViewById(R.id.text_profile_orgs_num);
	}
	
	private void setWidgets(User user) {
		if (!TextUtils.isEmpty(user.getWords())) {
			mTvWords.setText("\"" + user.getWords() + "\"");
		}
		mTvCollege.setText(user.getDepartment());
		//mTvFriendsNum.setText(text)
	}

	@Override
	public Loader<List<User>> onCreateLoader(int arg0, Bundle arg1) {
		
		return new UserLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<User>> arg0, List<User> result) {
		if (result != null && result.size() != 0) {
			mUser = result.get(0);
			setWidgets(mUser);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<User>> arg0) {
	}
	
}
