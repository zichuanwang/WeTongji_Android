package com.wetongji_android.ui.event;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.actionbarsherlock.app.SherlockActivity;
import com.wetongji_android.R;

public class EventDetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpUI();
		
	}
	
	private void setUpUI() {
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_event);
		
		View layoutLike = LayoutInflater.from(this).inflate(R.layout.ab_event_like_layout, null);
		
		ViewGroup.LayoutParams layoutParams = 
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
		
//		setContentView(layoutLike, layoutParams);
//		getSupportActionBar().setCustomView(layoutLike);
//      getSupportActionBar().setDisplayShowCustomEnabled(true);
		
		ViewGroup mDecor = (ViewGroup)this.getWindow().getDecorView().findViewById(android.R.id.content);
		
		List<View> views = null;
        if (mDecor.getChildCount() > 0) {
            views = new ArrayList<View>(1); //Usually there's only one child
            for (int i = 0, children = mDecor.getChildCount(); i < children; i++) {
                View child = mDecor.getChildAt(0);
                mDecor.removeView(child);
                Log.d("removeView", "" + i);
                views.add(child);
            }
        }
		setContentView(views.get(0));
	}

	
	
	
}
