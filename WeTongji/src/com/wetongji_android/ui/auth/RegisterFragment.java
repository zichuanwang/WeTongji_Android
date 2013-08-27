package com.wetongji_android.ui.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wetongji_android.R;
import com.wetongji_android.net.NetworkLoader;
import com.wetongji_android.net.http.HttpMethod;
import com.wetongji_android.ui.setting.WTTermsOfUseActivity;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.exception.ExceptionToast;
import com.wetongji_android.util.net.ApiHelper;
import com.wetongji_android.util.net.HttpRequestResult;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link RegisterFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class RegisterFragment extends Fragment implements OnClickListener,
		LoaderCallbacks<HttpRequestResult> {

	private Button btnBack;
	private Button btnNext;
	private EditText etUserNO;
	private EditText etName;
	private EditText etPassword;
	private ImageView ivAvatar;
	private View view;
	private ProgressDialog progressDialog;

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
		view = inflater.inflate(R.layout.fragment_register, container, false);
		etUserNO = (EditText) view.findViewById(R.id.et_register_username);
		etPassword = (EditText) view.findViewById(R.id.et_register_password);
		etName = (EditText) view.findViewById(R.id.et_register_name);
		etPassword.setTypeface(Typeface.DEFAULT);
		etPassword.setTransformationMethod(new PasswordTransformationMethod());
		btnBack = (Button) view.findViewById(R.id.btn_register_back);
		btnBack.setOnClickListener(this);
		btnNext = (Button) view.findViewById(R.id.btn_register_next);
		btnNext.setOnClickListener(this);
		ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar_picker);
		ivAvatar.setOnClickListener(this);
		view.findViewById(R.id.tv_register_note).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		switch (v.getId()) {
		case R.id.btn_register_back:
			transaction.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
			transaction.replace(R.id.auth_content_container,
					LoginFragment.newInstance(null));
			transaction.commit();
			break;
		case R.id.iv_avatar_picker:
			// Can't upload image when register
			// ((AuthActivity) getActivity()).doPickPhotoAction();
			break;
		case R.id.btn_register_next:
			regitster();
			break;
		case R.id.tv_register_note:
			startActivity(new Intent(getActivity(), WTTermsOfUseActivity.class));
			break;
		}
	}

	private void regitster() {
		// TODO check field
		String no = etUserNO.getText().toString();
		String name = etName.getText().toString();
		String password = etPassword.getText().toString();
		Bundle args = ApiHelper.getInstance(getActivity()).getUserActive(no,
				password, name);
		getLoaderManager().initLoader(WTApplication.NETWORK_LOADER_DEFAULT,
				args, this).forceLoad();

		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog
					.setMessage(getString(R.string.msg_progress_registering));
			progressDialog.show();
		} else {
			progressDialog.show();
		}
	}

	public void setAvatar(Bundle args) {
		ivAvatar.setImageBitmap((Bitmap) args.getParcelable("cropedImage"));
	}

	@Override
	public Loader<HttpRequestResult> onCreateLoader(int arg0, Bundle arg1) {
		return new NetworkLoader(getActivity(), HttpMethod.Get, arg1);
	}

	@Override
	public void onLoadFinished(Loader<HttpRequestResult> arg0,
			HttpRequestResult result) {
		progressDialog.dismiss();
		if (result.getResponseCode() == 0) {
			// jump to login
			new AlertDialog.Builder(getActivity())
					.setTitle(
							getString(R.string.dialog_title_regitster_success))
					.setMessage(getString(R.string.dialog_msg_need_to_active))
					.setPositiveButton(
							R.string.text_confirm,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									arg0.dismiss();
									hander.sendEmptyMessage(2);
								}
							}).show();
		} else {
			ExceptionToast.show(getActivity(), result.getResponseCode());
		}
		getLoaderManager().destroyLoader(arg0.getId());
	}

	@Override
	public void onLoaderReset(Loader<HttpRequestResult> arg0) {
	}

	private void jumpToLogin() {
		LoginFragment fragment = LoginFragment.newInstance(etUserNO.getText()
				.toString());
		getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_left_in,
						R.anim.slide_left_out)
				.add(R.id.auth_content_container, fragment,
						AuthActivity.TAG_LOGIN_FRAGMENT).commit();
	}

	private Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				jumpToLogin();
			}
		}

	};

}
