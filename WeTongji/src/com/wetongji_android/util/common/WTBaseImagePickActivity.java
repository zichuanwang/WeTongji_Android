package com.wetongji_android.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wetongji_android.R;
import com.wetongji_android.util.image.PickImageIntentWrapper;

public class WTBaseImagePickActivity extends SlidingFragmentActivity {
	
	protected static File PHOTO_DIR;
	protected static File UPLOAD_AVATAR;
	protected Uri mUriTemp;
	protected static final int CAMERA_WITH_DATA = 3023;
	protected static final int PHOTO_PICKED_WITH_DATA = 3021;
	protected static final int PHOTO_CROPED_WITH_DATA = 3024;

	
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		PHOTO_DIR = this.getExternalFilesDir("temp");
		UPLOAD_AVATAR = new File(PHOTO_DIR, "upload_avatar.jpg");
		mUriTemp = Uri.parse("file://" + UPLOAD_AVATAR.getPath());
	}

	public void doPickPhotoAction() {

		final Context dialogContext = new ContextThemeWrapper(this,
				R.style.Theme_Sherlock_Light_Dialog);

		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.text_take_photo);
		choices[1] = getString(R.string.text_pick_photo);
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
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {
								doTakePhoto();
							} else {
								Toast.makeText(WTBaseImagePickActivity.this,
										R.string.text_no_sdcard,
										Toast.LENGTH_SHORT).show();
							}
							break;

						case 1:
							doPickPhotoFromGallery();
							break;
						}
					}
				});

		builder.setNegativeButton(R.string.text_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();

	}

	protected void doTakePhoto() {
		try {

			final Intent intent = PickImageIntentWrapper
					.getTakePickIntent(mUriTemp);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.text_no_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = PickImageIntentWrapper.getPhotoPickIntent(mUriTemp);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {

		}
	}

	
	protected void saveBitmap(Bitmap bm) {
		try {
			UPLOAD_AVATAR.createNewFile();
			FileOutputStream fos = new FileOutputStream(UPLOAD_AVATAR);
			bm.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doCropPhoto(Uri uri, Uri outUri) {
		Intent intent = PickImageIntentWrapper.getCropImageIntent(uri, outUri);
		if (intent != null) {
			startActivityForResult(intent, PHOTO_CROPED_WITH_DATA);
		} else {
		}
	}
	
	protected Bitmap decodeUriAsBitmap(Uri uri){
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

}
