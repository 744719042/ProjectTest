package com.example.mytestproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WaterFlowLayout extends ViewGroup {
    public WaterFlowLayout(Context context) {
        super(context);
    }

    public WaterFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<List<View>> viewList = new ArrayList<>();
    private List<Integer> lineHeights = new ArrayList<>();
    private int line = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidth = 0;
        int measureHeight = 0;

        viewList.clear();
        lineHeights.clear();
        line = 0;

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {
            int iCurLineW = 0;
            int iCurLineH = 0;
            viewList.add(new ArrayList<View>());
            int iChildWidth = 0;
            int iChildHeight = 0;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                iChildWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                iChildHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                if (iChildWidth + iCurLineW > widthSize) {
                    line++;
                    viewList.add(new ArrayList<View>());
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;
                    lineHeights.add(iChildHeight);
                    iCurLineW = iChildWidth;
                    iCurLineH = iChildHeight;
                } else {
                    iCurLineW += iChildWidth;
                    iCurLineH = Math.max(iChildHeight, iCurLineH);

                    if (i == childCount - 1) {
                        measureWidth = Math.max(measureWidth, iCurLineW);
                        measureHeight += iCurLineH;
                        lineHeights.add(iCurLineH);
                    }
                }
                List<View> views = viewList.get(line);
                views.add(child);
            }
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int top = 0;
            for (int i = 0; i <= line; i++) {
                List<View> views = viewList.get(i);
                int left = 0;
                for (int j = 0; j < views.size(); j++) {
                    View child = views.get(j);
                    MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                    child.layout(left + layoutParams.leftMargin, top + layoutParams.topMargin,
                            left + layoutParams.leftMargin + child.getMeasuredWidth(),
                            top + layoutParams.topMargin + child.getMeasuredHeight());

                    left += layoutParams.leftMargin + child.getMeasuredWidth() + layoutParams.rightMargin;
                }
                top += lineHeights.get(i);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
