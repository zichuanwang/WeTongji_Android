package com.wetongji_android.ui.now;

import com.wetongji_android.R;
import com.wetongji_android.util.net.HttpRequestResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link NowFragment#newInstance} factory method to create an instance of this
 * fragment.
 * 
 */
public class NowFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>{
	
	private View view;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment NowFragment.
	 */
	public static NowFragment newInstance() {
		NowFragment fragment = new NowFragment();
		return fragment;
	}

	public NowFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view=inflater.inflate(R.layout.fragment_now, container, false);
		return view;
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
		// TODO Auto-generated method stub
		
	}

}
