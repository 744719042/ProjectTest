package com.example.mytestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.example.mytestproject.widget.MyScrollView;

public class ScrollViewActivity extends AppCompatActivity {
    private static final String TAG = "ScrollViewActivity";
    private MyScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int left, int top, int oldl, int oldt) {
                Log.e(TAG,"onScroll, left = " + left + " top = " + top);
            }
        });
    }
}
