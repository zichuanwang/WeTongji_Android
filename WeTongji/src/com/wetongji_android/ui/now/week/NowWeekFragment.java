package com.wetongji_android.ui.now.week;

import java.util.Calendar;

import com.wetongji_android.R;
import com.wetongji_android.ui.main.OnWTListClickedListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NowWeekFragment.OnWTListClickedListener} interface to handle
 * interaction events. Use the {@link NowWeekFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class NowWeekFragment extends Fragment {
	private static final String ARG_BEGIN = "begin";

	private Calendar begin;

	private OnWTListClickedListener listClickedListener;
	private OnNowWeekListScrolledListener weekListScrollListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param weekStartTime
	 *            The start time of the week
	 * @return A new instance of fragment NowWeekFragment.
	 */
	public static NowWeekFragment newInstance(Calendar begin) {
		NowWeekFragment fragment = new NowWeekFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_BEGIN, begin);
		fragment.setArguments(args);
		return fragment;
	}

	public NowWeekFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			begin = (Calendar) getArguments().getSerializable(ARG_BEGIN);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_now_week, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listClickedListener = (OnWTListClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnWTListClickedListener");
		}
		try{
			weekListScrollListener=(OnNowWeekListScrolledListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnNowWeekListScrolledListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listClickedListener = null;
		weekListScrollListener=null;
	}

}
