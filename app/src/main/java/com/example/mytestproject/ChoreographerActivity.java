package com.example.mytestproject;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;

public class ChoreographerActivity extends AppCompatActivity {
    private static final String TAG = "ChoreographerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choreographer);
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            private long lastUpdateTime = 0;
            @Override
            public void doFrame(long frameTimeNanos) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = SystemClock.uptimeMillis();
                } else {
                    Log.e(TAG, "frame cost time = " + (SystemClock.uptimeMillis() - lastUpdateTime));
                    lastUpdateTime = SystemClock.uptimeMillis();
                }
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }
}
