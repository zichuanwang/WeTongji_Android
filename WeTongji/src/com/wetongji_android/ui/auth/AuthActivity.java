package com.wetongji_android.ui.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.flurry.android.FlurryAgent;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTApplication;
import com.wetongji_android.util.image.PickImageIntentWrapper;

public class AuthActivity extends BaseAuthActivity implements OnClickListener, OnCheckedChangeListener{
	
	public static final String TAG_LOGIN_FRAGMENT="loginFragment";
	public static final String TAG_REGISTER_FRAGMENT="registerFragment";
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() 
            + "/WeTongji/temp");
    private static File UPLOAD_AVATAR=new File(Environment.getExternalStorageDirectory()
            + "/WeTongji/upload_avatar.jpg");;
    private static final int CAMERA_WITH_DATA = 3023;  
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
	
	private ToggleButton btnOnLogin;
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_authenticator);
		getSupportFragmentManager().beginTransaction()
			.add(R.id.auth_content_container, LoginFragment.newInstance(mUsername), TAG_LOGIN_FRAGMENT)
			.commit();
		setupActionBar();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, WTApplication.FLURRY_API_KEY);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		ActionBar ab=getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayShowHomeEnabled(false);
		View v=getLayoutInflater().inflate(R.layout.actionbar_authenticator, null);
		ab.setCustomView(v);
		btnOnLogin=(ToggleButton) v.findViewById(R.id.btn_on_login);
		btnOnLogin.setChecked(true);
		btnOnLogin.setOnCheckedChangeListener(this);
		Button btnNotNow=(Button) v.findViewById(R.id.btn_not_now);
		btnNotNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Intent intent=new Intent(this, IntroActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.pop_in, R.anim.slide_down_out);
		finish();
	}
	
	public void doPickPhotoAction() 
	{
        
        final Context dialogContext = new ContextThemeWrapper(this,  
                R.style.Theme_Sherlock_Light_Dialog); 
        
        String[] choices;  
        choices = new String[2];  
        choices[0] = getString(R.string.text_take_photo);  //拍照  
        choices[1] = getString(R.string.text_pick_photo);  //从相册中选择  
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,  
                android.R.layout.simple_list_item_1, choices);  
      
        final AlertDialog.Builder builder = new AlertDialog.Builder(  
                dialogContext);  
        builder.setTitle(R.string.text_set_avatar);  
        builder.setSingleChoiceItems(adapter, -1,  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                        switch (which) {  
                        case 0: 
                            String status = Environment.getExternalStorageState();  
                            if(status.equals(Environment.MEDIA_MOUNTED)){//判断是否有SD卡  
                                // 用户点击了从照相机获取 
                                doTakePhoto();  
                            }  
                            else{  
                                Toast.makeText(AuthActivity.this, 
                                		R.string.text_no_sdcard, Toast.LENGTH_SHORT)
                                		.show();
                            }  
                            break;  
                              
                        case 1:  
                            // 从相册中去获取  
                            doPickPhotoFromGallery();
                            break;  
                        }
                    }  
                }); 
        
        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() 
        {  
      
            @Override  
            public void onClick(DialogInterface dialog, int which) 
            {  
                dialog.dismiss();  
            }  
              
        });  
        builder.create().show();  
     
    }
    
    /** 
     * 拍照获取图片 
     *  
     */  
     private void doTakePhoto() 
     {  
         try 
         {  
             // Launch camera to take photo for selected contact  
              
             final Intent intent = PickImageIntentWrapper.getTakePickIntent(PHOTO_DIR);  
             startActivityForResult(intent, CAMERA_WITH_DATA);
         } catch (ActivityNotFoundException e) 
         {   Toast.makeText(AuthActivity.this, 
         		R.string.text_no_camera, Toast.LENGTH_SHORT)
         		.show();
         }  
     }  
     
     // 请求Gallery程序  
     private void doPickPhotoFromGallery() 
     {  
         try 
         {  
             // Launch picker to choose photo for selected contact  
             final Intent intent = PickImageIntentWrapper.getPhotoPickIntent();  
             startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);  
         } catch (ActivityNotFoundException e) {  
             
         }  
     }  
     
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) 
     {  
         if (resultCode != RESULT_OK)  
             return;  
         switch (requestCode) 
         {  
             case PHOTO_PICKED_WITH_DATA: 
             {    
            	 // 调用Gallery返回的  
                 final Bitmap bm = data.getParcelableExtra("data");
                 saveBitmap(bm);
                 
                 RegisterFragment fragment=(RegisterFragment) getSupportFragmentManager().findFragmentByTag(TAG_REGISTER_FRAGMENT);
                 Bundle bundle = new Bundle();
                 bundle.putParcelable("cropedImage", bm);
                 bundle.putString("imagePath", UPLOAD_AVATAR.getPath());
                 fragment.setAvatar(bundle);
                 break;  
             }
             case CAMERA_WITH_DATA:
             {
                 final Bitmap bm = data.getParcelableExtra("data");
                 doCropPhoto(bm);
                 break;
             }
                          
         }  
     }
     
     private void saveBitmap(Bitmap bm) 
     {
      // 保存图片
         try 
         {
            UPLOAD_AVATAR.createNewFile();
            FileOutputStream fos = new FileOutputStream(UPLOAD_AVATAR);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
     
     private void doCropPhoto(Bitmap bm)
     {
         Intent intent = PickImageIntentWrapper.getCropImageIntent(bm);
         if(intent != null) 
         {
             startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
         }
         else 
         { 
        	 Toast.makeText(AuthActivity.this, 
         		R.string.text_no_image_editor, Toast.LENGTH_SHORT)
         		.show();
         }
     }

}
