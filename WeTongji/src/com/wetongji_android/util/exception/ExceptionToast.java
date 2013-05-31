package com.wetongji_android.util.exception;

import com.wetongji_android.R;
import com.wetongji_android.ui.auth.AuthActivity;
import com.wetongji_android.util.common.WTApplication;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExceptionToast {
	
	private static boolean showLogin=true;
	
	public static void show(Context context,int code){
		switch(code){
		case 1:
		case 2:
		case 3:
		case 4:
		case 7:
		case 999:
			Toast.makeText(context, R.string.text_error_service_down, Toast.LENGTH_LONG).show();
			break;
		case 200:
			Toast.makeText(context, R.string.text_error_check_network, Toast.LENGTH_LONG).show();
			break;
		case 5:
			Toast.makeText(context, R.string.text_error_request_login, Toast.LENGTH_LONG).show();
			invalidAuthToken(context);
			break;
		case 6:
			Toast.makeText(context, R.string.text_error_request_login_again, Toast.LENGTH_LONG).show();
			invalidAuthToken(context);
			break;
		case 8:
			Toast.makeText(context, R.string.text_error_already_registried, Toast.LENGTH_LONG).show();
			break;
		case 9:
			Toast.makeText(context, R.string.text_error_student_id_and_name_not_match, Toast.LENGTH_LONG).show();
			break;
		case 10:
			Toast.makeText(context, R.string.text_error_password_not_meet_specifications, Toast.LENGTH_LONG).show();
			break;
		case 11:
			Toast.makeText(context, R.string.text_error_account_not_actived, Toast.LENGTH_LONG).show();
			break;
		case 12:
			Toast.makeText(context, R.string.text_error_student_id_not_exist, Toast.LENGTH_LONG).show();
			break;
		case 13:
			Toast.makeText(context, R.string.text_error_wrong_username_password, Toast.LENGTH_LONG).show();
			break;
		case 14:
			Toast.makeText(context, R.string.text_error_student_id_not_registried, Toast.LENGTH_LONG).show();
			break;
		case 15:
			Toast.makeText(context, R.string.text_error_friends_not_allowed_due_to_privacy, Toast.LENGTH_LONG).show();
			break;
		case 16:
			Toast.makeText(context, R.string.text_error_student_id_required, Toast.LENGTH_SHORT).show();
			break;
		case 17:
			Toast.makeText(context, R.string.text_error_password_requried, Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	private static void invalidAuthToken(Context context){
		AccountManager am=AccountManager.get(context);
		am.invalidateAuthToken(WTApplication.ACCOUNT_TYPE, null);
		if(showLogin){
			Intent intent=new Intent(context, AuthActivity.class);
			context.startActivity(intent);
			showLogin=false;
		}
	}

}
