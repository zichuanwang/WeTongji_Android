package com.wetongji_android.util.image;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

public class PickImageIntentWrapper {

	// ·â×°ÇëÇóGalleryµÄintent  
    public static Intent getPhotoPickIntent() 
    {  
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        intent.putExtra("outputX", 200);  
        intent.putExtra("outputY", 200);  
        intent.putExtra("return-data", true);  
        return intent;  
    }  
    
    public static Intent getTakePickIntent(File f) 
    {  
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
        return intent;  
    }  
    
    public static Intent getCropImageIntent(Bitmap bm) 
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", bm);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;
        
    }
    
}
