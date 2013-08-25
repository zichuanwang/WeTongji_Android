package com.wetongji_android.ui.auth;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.viewpagerindicator.UnderlinePageIndicator;
import com.wetongji_android.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link IntroFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class IntroFragment extends Fragment implements ViewFactory {

	private static final int[] BG_IMAGE_IDS = { R.drawable.img_intro_1, R.drawable.img_intro_2,
			R.drawable.img_intro_3 };

	private ImageSwitcher switcher;
	private final Handler handler = new Handler();
	private int currentIntroImage = 0;
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

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_intro, container, false);
		List<View> viewList = new ArrayList<View>();

		/*switcher = (ImageSwitcher) rootView.findViewById(R.id.is_intro_bg);
		switcher.setFactory(this);
		Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
		fadeIn.setDuration(1000);
		Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
		fadeOut.setDuration(1000);
		switcher.setInAnimation(fadeIn);
		switcher.setOutAnimation(fadeOut);*/

		for (int i = 0; i < 3; i++) {
			ImageView image = new ImageView(getActivity());
			image = (ImageView) inflater.inflate(R.layout.page_intro, null);
			//image.setBackgroundResource(BG_IMAGE_IDS[i]);
			image.setBackgroundDrawable(getResources().getDrawable(BG_IMAGE_IDS[i]));
			viewList.add(image);
		}

		viewPager = (ViewPager) rootView.findViewById(R.id.vp_guides);
		viewPager.setAdapter(new IntroImageAdapter(viewList));
		indicator = (UnderlinePageIndicator) rootView.findViewById(R.id.vp_indicator_guides);
		indicator.setViewPager(viewPager, 0);
		indicator.setFades(false);

		//handler.post(runnable);
		return rootView;
	}

	@Override
	public View makeView() {
		ImageView image = new ImageView(getActivity());
		image.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.FIT_XY);
		return image;
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			switcher.setImageResource(BG_IMAGE_IDS[currentIntroImage]);
			currentIntroImage++;
			if (currentIntroImage == 3) {
				currentIntroImage = 0;
			}
			handler.postDelayed(this, 5000);
		}
	};

}
