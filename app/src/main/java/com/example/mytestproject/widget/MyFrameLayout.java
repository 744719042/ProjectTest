package com.example.mytestproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int motionY;
    private int lastY;
    private boolean isDragging = false;

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
                isDragging = false;
                motionY = 0;
                lastY = 0;
                break;
            }
        }
        return true;
    }
}
