/**
 * 
 */
package com.wetongji_android.ui.test;

import com.wetongji_android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.test_frame, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Button button = (Button)getActivity().findViewById(R.id.button_ok);
		EditText name = (EditText)getActivity().findViewById(R.id.et_username);
		EditText pwd = (EditText)getActivity().findViewById(R.id.et_password);
		
		button.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "nihaoa", Toast.LENGTH_SHORT).show();
			}	
		});
	}
}
