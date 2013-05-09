package com.wetongji_android.ui.auth;

import com.wetongji_android.R;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link RegisterFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class RegisterFragment extends Fragment implements OnClickListener{
	
	private Button btnBack;
	private Button btnNext;
	private EditText etUsername;
	private EditText etPassword;
	private ImageView ivAvatar;
	private View view;
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment RegisterFragment.
	 */
	public static RegisterFragment newInstance() {
		RegisterFragment fragment = new RegisterFragment();
		return fragment;
	}

	public RegisterFragment() {
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
		view=inflater.inflate(R.layout.fragment_register, container, false);
		etUsername=(EditText) view.findViewById(R.id.et_register_username);
		etPassword=(EditText) view.findViewById(R.id.et_register_password);
		etPassword.setTypeface(Typeface.DEFAULT);
		etPassword.setTransformationMethod(new PasswordTransformationMethod());
		btnBack=(Button) view.findViewById(R.id.btn_register_back);
		btnBack.setOnClickListener(this);
		btnNext=(Button) view.findViewById(R.id.btn_register_next);
		btnNext.setOnClickListener(this);
		ivAvatar=(ImageView) view.findViewById(R.id.iv_avatar_picker);
		ivAvatar.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction=getFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.btn_register_back:
			transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
			transaction.replace(R.id.auth_content_container, LoginFragment.newInstance(null));
			transaction.commit();
			break;
		case R.id.iv_avatar_picker:
			((AuthActivity) getActivity()).doPickPhotoAction();
			break;
		case R.id.btn_register_next:
			break;
		}
	}
	
	public void setAvatar(Bundle args){
		ivAvatar.setImageBitmap((Bitmap) args.getParcelable("cropedImage"));
	}

}
