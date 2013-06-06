package com.wetongji_android.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.wetongji_android.R;
import com.wetongji_android.util.image.ImageUtil;

public class ProfileFragment extends SherlockFragment {

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
	
}
