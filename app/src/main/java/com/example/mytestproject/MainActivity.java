package com.example.mytestproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mytestproject.widget.WaterFlowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void restart(View view) {
        throw new RuntimeException("Restart Exception");
    }

    public void startNetwork(View view) {
        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);
    }

    public void startHandler(View view) {
        Intent intent = new Intent(this, HandlerActivity.class);
        startActivity(intent);
    }

    public void startSingleton(View view) {
        Intent intent = new Intent(this, SingletonActivity.class);
        startActivity(intent);
    }

    public void startStack(View view) {
        Intent intent = new Intent(this, StackActivity.class);
        startActivity(intent);
    }

    public void threadModify(View view) {
        Intent intent = new Intent(this, ThreadModifyActivity.class);
        startActivity(intent);
    }

    public void testAsyncMessage(View view) {
        Intent intent = new Intent(this, SyncBarrierActivity.class);
        startActivity(intent);
    }

    public void choreographer(View view) {
        Intent intent = new Intent(this, ChoreographerActivity.class);
        startActivity(intent);
    }

    public void testScroll(View view) {
        Intent intent = new Intent(this, ScrollActivity.class);
        startActivity(intent);
    }

    public void testFlow(View view) {
        Intent intent = new Intent(this, WaterFlowActivity.class);
        startActivity(intent);
    }

    public void testScrollView(View view) {
        Intent intent = new Intent(this, ScrollViewActivity.class);
        startActivity(intent);
    }
}
