package com.wetongji_android.ui.auth;

import com.wetongji_android.R;
import android.app.Activity;
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

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface to handle
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
		btnLogin.setOnClickListener(this);
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
		String username=etUsername.getText().toString();
		String password=etPassword.getText().toString();
		((AuthenticatorActivity)getActivity()).handleLogin(username, password);
	}

}
