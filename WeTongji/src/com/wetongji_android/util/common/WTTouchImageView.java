package com.wetongji_android.util.common;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Context;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class WTTouchImageView extends ImageView{
    static final int NONE = 0;//

    static final int DRAG = 1;

    static final int ZOOM = 2; //

    static final int BIGGER = 3; //

    static final int SMALLER = 4; //

    private int mode = NONE; //

    private float beforeLenght; //

    private float afterLenght; //

    private float scale = 0.04f; //

    private int screenW;//

    private int screenH;

    private int start_x;//

    private int start_y;

    private int stop_x;

    private int stop_y;

    private TranslateAnimation trans;
    
    final SherlockActivity SherlockActivity;
    
    long touchDownTime = 0;

    public WTTouchImageView(Context context, int w, int h, SherlockActivity activity)
    {
        super(context);
        this.setPadding(0, 0, 0, 0);
        screenW = w;
        screenH = h;
        
        SherlockActivity = activity;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
        
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            mode = DRAG;
            stop_x = (int) event.getRawX();
            stop_y = (int) event.getRawY();
            start_x = stop_x - this.getLeft();

            start_y = stop_y - this.getTop();


            if (event.getPointerCount() == 2)
                beforeLenght = spacing(event);
            touchDownTime = System.currentTimeMillis();
            
            
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            if (spacing(event) > 10f) {
                mode = ZOOM;
                beforeLenght = spacing(event);
            }
            break;
        case MotionEvent.ACTION_UP:
            
            if(System.currentTimeMillis() - touchDownTime < 300) {
                if(SherlockActivity.getSupportActionBar().isShowing()) {
                    SherlockActivity.getSupportActionBar().hide();
                }else {
                    SherlockActivity.getSupportActionBar().show();
                }
            }

            int disX = 0;
            int disY = 0;
            if (getHeight() <= screenH)//
            {
                if (this.getTop() < 0) {
                    disY = getTop();
                    this.layout(this.getLeft(), 0, this.getRight(),
                            0 + this.getHeight());

                } else if (this.getBottom() >= screenH) {
                    disY = getHeight() - screenH + getTop();
                    this.layout(this.getLeft(), screenH - getHeight(),
                            this.getRight(), screenH);
                }
            } else {
                int Y1 = getTop();
                int Y2 = getHeight() - screenH + getTop();
                if (Y1 > 0) {
                    disY = Y1;
                    this.layout(this.getLeft(), 0, this.getRight(),
                            0 + this.getHeight());
                } else if (Y2 < 0) {
                    disY = Y2;
                    this.layout(this.getLeft(), screenH - getHeight(),
                            this.getRight(), screenH);
                }
            }
            if (getWidth() <= screenW) {
                if (this.getLeft() < 0) {
                    disX = getLeft();
                    this.layout(0, this.getTop(), 0 + getWidth(),
                            this.getBottom());
                } else if (this.getRight() > screenW) {
                    disX = getWidth() - screenW + getLeft();
                    this.layout(screenW - getWidth(), this.getTop(), screenW,
                            this.getBottom());
                }
            } else {
                int X1 = getLeft();
                int X2 = getWidth() - screenW + getLeft();
                if (X1 > 0) {
                    disX = X1;
                    this.layout(0, this.getTop(), 0 + getWidth(),
                            this.getBottom());
                } else if (X2 < 0) {
                    disX = X2;
                    this.layout(screenW - getWidth(), this.getTop(), screenW,
                            this.getBottom());
                }

            }
            while (getHeight() < 100 || getWidth() < 100) {

                setScale(scale, BIGGER);
            }
            if (disX != 0 || disY != 0) {
                trans = new TranslateAnimation(disX, 0, disY, 0);
                trans.setDuration(500);
                this.startAnimation(trans);
            }
            mode = NONE;
            break;
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            
            break;
        case MotionEvent.ACTION_MOVE:

            if (mode == DRAG) {
                this.setPosition(stop_x - start_x, stop_y - start_y, stop_x
                        + this.getWidth() - start_x,
                        stop_y - start_y + this.getHeight());
                stop_x = (int) event.getRawX();
                stop_y = (int) event.getRawY();

            } else if (mode == ZOOM) {
                if (spacing(event) > 10f) {
                    afterLenght = spacing(event);
                    float gapLenght = afterLenght - beforeLenght;
                    if (gapLenght == 0) {
                        break;
                    }
                    else if (Math.abs(gapLenght) > 5f && getWidth() > 70) {
                        if (gapLenght > 0) {
                            this.setScale(scale, BIGGER);
                        } else {
                            this.setScale(scale, SMALLER);
                        }
                        beforeLenght = afterLenght;
                    }
                }
            }
            break;
        }
        return true;
    }

    private void setScale(float temp, int flag) {

        if (flag == BIGGER) {
            this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
                    this.getTop() - (int) (temp * this.getHeight()),
                    this.getRight() + (int) (temp * this.getWidth()),
                    this.getBottom() + (int) (temp * this.getHeight()));
        } else if (flag == SMALLER) {
            this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
                    this.getTop() + (int) (temp * this.getHeight()),
                    this.getRight() - (int) (temp * this.getWidth()),
                    this.getBottom() - (int) (temp * this.getHeight()));
        }
    }

    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }  
}