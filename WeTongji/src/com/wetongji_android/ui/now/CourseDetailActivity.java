package com.wetongji_android.ui.now;

import android.os.Bundle;

import com.wetongji_android.R;
import com.wetongji_android.util.common.WTBaseDetailActivity;

public class CourseDetailActivity extends WTBaseDetailActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mVsContent.setLayoutResource(R.layout.activity_course_detail);
		mVsContent.inflate();
	}

	
}
