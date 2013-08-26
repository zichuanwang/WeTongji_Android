package com.wetongji_android.ui.auth;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link IntroFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class IntroFragment extends Fragment {

	private static final int[] BG_IMAGE_IDS = { R.drawable.img_intro_1, R.drawable.img_intro_2,
			R.drawable.img_intro_3, R.drawable.img_intro_4 };

	private View rootView;
	private ViewPager viewPager;
	private UnderlinePageIndicator indicator;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment IntroFragment.
	 */
	public static IntroFragment newInstance() {
		IntroFragment fragment = new IntroFragment();
		return fragment;
	}

	public IntroFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_intro, container, false);
		List<View> viewList = new ArrayList<View>();

		for (int i = 0; i < BG_IMAGE_IDS.length; i++) {
			ImageView image = new ImageView(getActivity());
			image = (ImageView) inflater.inflate(R.layout.page_intro, null);
			image.setBackgroundResource(BG_IMAGE_IDS[i]);
			viewList.add(image);
		}

		viewPager = (ViewPager) rootView.findViewById(R.id.vp_guides);
		viewPager.setAdapter(new IntroImageAdapter(viewList));
		indicator = (UnderlinePageIndicator) rootView.findViewById(R.id.vp_indicator_guides);
		indicator.setViewPager(viewPager, 0);
		indicator.setFades(false);

		return rootView;
	}
}
