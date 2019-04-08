package com.example.mytestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StackActivity extends AppCompatActivity {
    MyStack myStack = new MyStack(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);

        myStack.push(new Object());
        myStack.push(new Object());
        myStack.push(new Object());
        myStack.push(new Object());

        myStack.pop();
        myStack.pop();
        myStack.pop();
    }
}
