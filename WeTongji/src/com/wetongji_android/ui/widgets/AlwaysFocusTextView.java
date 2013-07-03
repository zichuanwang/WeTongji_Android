package com.wetongji_android.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;


public class AlwaysFocusTextView extends android.widget.TextView {

	public AlwaysFocusTextView(Context context) {
		super(context);
	}
	
	public AlwaysFocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysFocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
	
	

}
