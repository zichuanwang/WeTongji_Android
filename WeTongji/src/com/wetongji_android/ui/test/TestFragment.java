/**
 * 
 */
package com.wetongji_android.ui.test;

import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.util.auth.RSAEncrypter;
import com.wetongji_android.util.common.WTApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
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
public class TestFragment extends Fragment implements LoaderCallbacks<String>
{	
	private Button mBtnLogin;
	private EditText mEdtName;
	private EditText mEdtPwd;
	
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
		mBtnLogin = (Button)getActivity().findViewById(R.id.button_ok);
		mEdtName = (EditText)getActivity().findViewById(R.id.et_username);
		mEdtPwd = (EditText)getActivity().findViewById(R.id.et_password);
		
		mBtnLogin.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "nihaoa", Toast.LENGTH_SHORT).show();
				login(v);
			}	
		});
	}

	private void login(View view)
	{
		String strName = mEdtName.getText().toString();
		String strPwd = mEdtPwd.getText().toString();
		
		//Log.i("name", strName);
		Log.i("pwd", strPwd);
		
		if(!TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strPwd))
		{
			Bundle params = new Bundle();
			params.putString(WTApplication.API_ARGS_METHOD, WTApplication.API_METHOD_USER_LOGON);
			params.putString(WTApplication.API_METHOD_ARGS_NO, strName);
			params.putString(WTApplication.API_METHOD_ARGS_PASSWORD, RSAEncrypter.encrypt(strPwd, getActivity()));
			
			getLoaderManager().initLoader(WTApplication.NETWORK_LOADER, params, this);
		}
	}
	
	@Override
	public Loader<String> onCreateLoader(int arg0, Bundle arg1) 
	{
		// TODO Auto-generated method stub
		if(arg1 != null)
		{
			return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
		}
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<String> arg0, String str) 
	{
		// TODO Auto-generated method stub
		Log.v("The result is: ", str);
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
}
