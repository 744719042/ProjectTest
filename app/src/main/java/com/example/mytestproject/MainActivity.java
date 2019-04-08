package com.example.mytestproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
