package com.wetongji_android.util.common;

import com.actionbarsherlock.app.SherlockActivity;
import com.androidquery.AQuery;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class WTFullScreenPicArea extends LinearLayout {
    private int imgDisplayW;

    private int imgDisplayH;

    private int imgW;

    private int imgH;

    private WTTouchImageView touchView;

    final SherlockActivity mSherlockActivity;
    
    //private DisplayMetrics dm;

    // resId为图片资源id
    @SuppressWarnings("deprecation")
	public WTFullScreenPicArea(Context context, String url, AQuery aq, int width, int height,
            SherlockActivity sherlockActivity) { // 第二个参数是图片的资源ID，当然也可以用别的方式获取图片
        /*
         * dm = new DisplayMetrics();
         * ((Activity)context).getWindowManager().getDefaultDisplay
         * ().getMetrics(dm); imgDisplayW = dm.widthPixels; imgDisplayH =
         * dm.heightPixels;
         */
        super(context);
        
        setOnClickListener(new LayoutClickListener());
        this.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        
        mSherlockActivity = sherlockActivity;

        imgDisplayW = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();

        imgDisplayH = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getHeight();

        touchView = new WTTouchImageView(context, imgDisplayW, imgDisplayH, mSherlockActivity);// 这句就是自定义ImageView
        //touchView.setImageDrawable(drawable);
        
        touchView.setImageBitmap(aq.getCachedImage(url));// 给自定义imageView设置要显示的图片
        
        touchView.setFocusable(false);
        
        imgW = width;
        imgH = height;
        
        // 图片第一次加载进来，判断图片大小从而确定第一次图片的显示方式。
        int layout_w = imgW > imgDisplayW ? imgDisplayW : imgW;
        int layout_h = imgH > imgDisplayH ? imgDisplayH : imgH;

        if (imgW >= imgH) {
            if (layout_w == imgDisplayW) {
                layout_h = (int) (imgH * ((float) imgDisplayW / imgW));
            }
        } else {
            if (layout_h == imgDisplayH) {
                layout_w = (int) (imgW * ((float) imgDisplayH / imgH));
            }
        }
        
        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams(layout_w,
                layout_h);
        parm.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        touchView.setLayoutParams(parm);

        this.addView(touchView);
        
    }
    
    public class LayoutClickListener implements OnClickListener {

        /* (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {
          if(mSherlockActivity.getSupportActionBar().isShowing()) {
                mSherlockActivity.getSupportActionBar().hide();
          }else {
              mSherlockActivity.getSupportActionBar().show();
          }
        }
        
    }

}
