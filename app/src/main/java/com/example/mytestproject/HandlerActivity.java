package com.example.mytestproject;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HandlerActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Hello world!", Toast.LENGTH_SHORT).show();
            }
        };
        mHandler.postDelayed(mRunnable, 50000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHandler.removeCallbacks(mRunnable);
    }
}
