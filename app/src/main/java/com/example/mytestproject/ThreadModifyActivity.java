package com.example.mytestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mytestproject.utils.ThreadUtils;

public class ThreadModifyActivity extends AppCompatActivity {
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
