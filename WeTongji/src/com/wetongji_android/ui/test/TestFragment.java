/**
 * 
 */
package com.wetongji_android.ui.test;

import com.wetongji_android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author nankonami
 *
 */
public class TestFragment extends Fragment
{	
	public TestFragment()
	{
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		return inflater.inflate(R.layout.test_frame, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		
	}
}
