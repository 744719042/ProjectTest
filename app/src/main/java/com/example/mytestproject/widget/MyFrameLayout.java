package com.example.mytestproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MyFrameLayout extends FrameLayout {
    private static final String TAG = "MyFrameLayout";
    public MyFrameLayout(Context context) {
        this(context, null);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }
    private Scroller scroller;

    private int motionY;
    private int lastY;
    private boolean isDragging = false;

    private boolean up = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                lastY = y;
                motionY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int distY = y - motionY;
                if (!isDragging && Math.abs(distY) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    isDragging = true;
                    up = distY < 0;
                }

                if (isDragging) {
                    int dy = y - lastY;
                    scrollBy(0, -dy);
                }
                lastY = y;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDragging) {
                    Log.e(TAG, "onTouchEvent() scrollY = " + getScrollY());
                    scroller.startScroll(getScrollX(), getScrollY() - 1,  0, up ? 100 : -100, 1000);
                    postInvalidate();
                }
                up = false;
                isDragging = false;
                motionY = 0;
                lastY = 0;
                break;
            }
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!isDragging && scroller.computeScrollOffset()) {
            Log.e(TAG, "computeScroll() currY = " + scroller.getCurrY());
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
        }
        Log.e(TAG, "computeScroll() scrollY = " + getScrollY());
    }
}
