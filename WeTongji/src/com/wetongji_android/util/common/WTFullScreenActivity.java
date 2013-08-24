package com.wetongji_android.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.androidquery.AQuery;
import com.wetongji_android.R;

public class WTFullScreenActivity extends SherlockActivity 
{
	private String imgUrl;
	private File imgFile;
	
	private AQuery mAq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_full_screen_pic);
		getSupportActionBar().setIcon(R.drawable.ic_home);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_transparent_black));
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		imgUrl = bundle.getString(WTBaseDetailActivity.IMAGE_URL);
		int width = bundle.getInt(WTBaseDetailActivity.IMAGE_WIDTH);
		int height = bundle.getInt(WTBaseDetailActivity.IMAGE_HEIGHT);
		
		mAq = WTApplication.getInstance().getAq(this);
		
		RelativeLayout container = (RelativeLayout)findViewById(R.id.full_screen_layout);
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		parms.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		WTFullScreenPicArea area = new WTFullScreenPicArea(this, imgUrl, mAq, width, height, WTFullScreenActivity.this);
		area.setGravity(Gravity.CENTER);
		container.addView(area, parms);
		
		container.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
				long touchDownTime = 0;
				
				switch((event.getAction() & MotionEvent.ACTION_MASK))
				{
				case MotionEvent.ACTION_DOWN: 
				{
					touchDownTime = System.currentTimeMillis();
					return false;
				}
				case MotionEvent.ACTION_UP: 
				{
					if(System.currentTimeMillis() - touchDownTime < 200) 
					{
						if(WTFullScreenActivity.this.getSupportActionBar().isShowing())
						{
							WTFullScreenActivity.this.getSupportActionBar().hide();
						}else
						{
							WTFullScreenActivity.this.getSupportActionBar().show();
						}
					}
				}
				}
				
				return true;
			}
		});	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, R.id.pic_save, Menu.NONE, R.string.save_image)
			.setIcon(R.drawable.ic_action_save)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			break;
		case R.id.pic_save:
			imgFile = mAq.getCachedFile(imgUrl);
			File ext = Environment.getExternalStorageDirectory();
			File downloadFileDir = new File(ext, "wetongji/download");
			if(!downloadFileDir.exists())
			{
				downloadFileDir.mkdir();
			}
			File toFile = new File(ext, "wetongji/download" + imgFile.getName() + ".png");
			
			String strToast = getResources().getString(R.string.image_stored_at);
			try
			{
				copyFile(imgFile, toFile);
				Toast.makeText(this, strToast + downloadFileDir.getPath(), Toast.LENGTH_SHORT)
				.show();
			}catch(IOException e)
			{
				e.printStackTrace();
				Toast.makeText(this, R.string.image_save_failed, Toast.LENGTH_SHORT)
				.show();
			}
			
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
			this.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	public static void copyFile(File sourceFile,File targetFile)    
            throws IOException
     {   
        // 新建文件输入流并对它进行缓冲    
        FileInputStream input = new FileInputStream(sourceFile);   
        BufferedInputStream inBuff=new BufferedInputStream(input);   
  
        // 新建文件输出流并对它进行缓冲    
        FileOutputStream output = new FileOutputStream(targetFile);   
        BufferedOutputStream outBuff=new BufferedOutputStream(output);   
           
        // 缓冲数组    
        byte[] b = new byte[1024 * 5];   
        int len;   
        while ((len =inBuff.read(b)) != -1) {   
            outBuff.write(b, 0, len);   
        }   
        // 刷新此缓冲的输出流    
        outBuff.flush();   
           
        //关闭流    
        inBuff.close();   
        outBuff.close();   
        output.close();   
        input.close();   
    }
}
