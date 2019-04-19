package com.example.mytestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mytestproject.utils.ThreadUtils;

public class ThreadModifyActivity extends AppCompatActivity {
    private static final String TAG = "ThreadModifyActivity";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_modify);
        textView = findViewById(R.id.text);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText("Good Morning");
//            }
//        }).start();
        Log.d(TAG, "onCreate(): width = " + textView.getWidth() + ", height = " + textView.getHeight());
        textView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run(): width = " + textView.getWidth() + ", height = " + textView.getHeight());
            }
        });

        TextView myTextView = new TextView(this);
        myTextView.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run(): Non-attach textView");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtils.sleep(100);
                textView.setText("Good Morning");
            }
        }).start();
    }
}
