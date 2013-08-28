package com.wetongji_android.ui.auth;

import com.wetongji_android.R;
import com.wetongji_android.util.common.WTForgetPwdActivity;
import com.wetongji_android.util.exception.ExceptionToast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link LoginFragment.OnWTListClickedListener} interface to handle
 * interaction events. Use the {@link LoginFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class LoginFragment extends Fragment implements OnClickListener{
	private static final String ARG_USERNAME = "username";

	private String mUsername;
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnCreateAccount;
	private TextView tvForgetPassword;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param username
	 *            User name.
	 * @return A new instance of fragment LoginFragment.
	 */
	public static LoginFragment newInstance(String username) {
		LoginFragment fragment = new LoginFragment();
		Bundle args = new Bundle();
		args.putString(ARG_USERNAME, username);
		fragment.setArguments(args);
		return fragment;
	}

	public LoginFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mUsername = getArguments().getString(ARG_USERNAME);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_login, container, false);
		etUsername=(EditText) view.findViewById(R.id.et_username);
		etPassword=(EditText) view.findViewById(R.id.et_password);
		etPassword.setTypeface(Typeface.DEFAULT);
		etPassword.setTransformationMethod(new PasswordTransformationMethod());
		btnLogin=(Button) view.findViewById(R.id.btn_login);
		btnCreateAccount=(Button) view.findViewById(R.id.btn_create_account);
		btnLogin.setOnClickListener(this);
		btnCreateAccount.setOnClickListener(this);
		tvForgetPassword = (TextView)view.findViewById(R.id.tv_forget_password);
		tvForgetPassword.setOnClickListener(this);
		
		if(!TextUtils.isEmpty(mUsername)){
			etUsername.setText(mUsername);
		}
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_login:
			String username=etUsername.getText().toString();
			String password=etPassword.getText().toString();
			if(checkValidation(username, password))
			{
				((BaseAuthActivity)getActivity()).handleLogin(username, password);
			}
			break;
		case R.id.btn_create_account:
			RegisterFragment fragment=RegisterFragment.newInstance();
			getFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
				.add(R.id.auth_content_container, fragment, AuthActivity.TAG_REGISTER_FRAGMENT)
				.commit();
		break;
		case R.id.tv_forget_password:
			Intent intent = new Intent(getActivity(), WTForgetPwdActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		break;
		}
	}

	private boolean checkValidation(String name, String password)
	{
		boolean result = true;
		
		if(TextUtils.isEmpty(name))
		{
			result = false;
			ExceptionToast.show(getActivity(), 16);
		}else if(TextUtils.isEmpty(password))
		{
			result = false;
			ExceptionToast.show(getActivity(), 17);
		}
		
		return result;
	}
}
