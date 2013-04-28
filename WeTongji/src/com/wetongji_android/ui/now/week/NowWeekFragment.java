package com.wetongji_android.ui.now.week;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foound.widget.AmazingListView;
import com.wetongji_android.R;
import com.wetongji_android.data.Event;
import com.wetongji_android.factory.EventFactory;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link NowWeekFragment.OnWTListClickedListener} interface to handle
 * interaction events. Use the {@link NowWeekFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class NowWeekFragment extends Fragment implements LoaderCallbacks<HttpRequestResult>{
	
	private static final String ARG_BEGIN = "begin";

	private Calendar begin;
	private NowWeekListAdapter adapter;
	private EventFactory factory;
	private View view;
	private AmazingListView lvEvent; 
	//private OnWTListClickedListener listClickedListener;
	//private OnNowWeekListScrolledListener weekListScrollListener;

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
		adapter=new NowWeekListAdapter(this, begin);
		factory=new EventFactory(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view=inflater.inflate(R.layout.fragment_now_week, container, false);

		
		lvEvent=(AmazingListView) view.findViewById(R.id.lv_now_week);
		lvEvent.setAdapter(adapter);
		adapter.notifyNoMorePages();
		
		return view;
	}

	
	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle args) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, args);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0, HttpRequestResult result) {
		if(result.getResponseCode()==0){
			List<Event> events=factory.createObjects(result.getStrResponseCon());
			adapter.setData(events);
			adapter.notifyDataSetChanged();
		}
		else{
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	/*
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
	*/

}
