package com.example.mytestproject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

public class SyncBarrierActivity extends AppCompatActivity {
    private Handler handler;
    private static final String TAG = "SyncBarrierActivity";
    private int token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_barrier);
        handler = new Handler();
        Message message1 = Message.obtain(handler, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "1000");
            }
        });
        Message message2 = Message.obtain(handler, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "2000");
            }
        });
        Message message3 = Message.obtain(handler, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "3000");
            }
        });

        Message message4 = Message.obtain(handler, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "4000");
                removeSyncBarrier();
            }
        });
        message3.setAsynchronous(true);
        message4.setAsynchronous(true);
        handler.sendMessageDelayed(message1, 1000);
        handler.sendMessageDelayed(message2, 2000);
        handler.sendMessageDelayed(message3, 3000);
        postSyncBarrier();
        handler.sendMessageDelayed(message4, 4000);
    }

    public void postSyncBarrier() {
        try {
            Method method = MessageQueue.class.getDeclaredMethod("postSyncBarrier");
            token = (int) method.invoke(Looper.getMainLooper().getQueue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeSyncBarrier() {
        try {
            Method method = MessageQueue.class.getDeclaredMethod("removeSyncBarrier", int.class);
            method.invoke(Looper.getMainLooper().getQueue(), token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
