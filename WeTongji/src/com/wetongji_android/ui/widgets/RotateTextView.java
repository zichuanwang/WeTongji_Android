package com.wetongji_android.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class RotateTextView extends android.widget.TextView {

	private static final String NAMESPACE = "http://we.tongji.edu.cn/apk/res/wetongji";
	private static final String ATTR_ROTATE = "rotate";
	private static final int DEFAULTVALUE_DEGREES = -25;
	private int degrees;

	public RotateTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		degrees = attrs.getAttributeIntValue(NAMESPACE, ATTR_ROTATE, DEFAULTVALUE_DEGREES);
	}

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		degrees = attrs.getAttributeIntValue(NAMESPACE, ATTR_ROTATE, DEFAULTVALUE_DEGREES);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(degrees, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		super.onDraw(canvas);
	}

}
