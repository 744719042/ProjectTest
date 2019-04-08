package com.example.mytestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingletonActivity extends AppCompatActivity implements UserManager.UserLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton);
        UserManager.getInstance().registerListener(this);
    }

    @Override
    public void onLogin() {
        // do something
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UserManager.getInstance().unregisterListener(this);
    }
}
