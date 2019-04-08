package com.example.mytestproject;

public class MyStack {
    private int mCapacity;
    private int mSize;
    private int mTop;
    private Object[] mData;

    public MyStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.mCapacity = capacity;
        mData = new Object[capacity];
        mSize = 0;
        mTop = 0;
    }

    public void push(Object obj) {
        if (mTop >= mCapacity) {
            throw new RuntimeException("Stack full");
        }

        mData[mTop++] = obj;
        mSize++;
    }

    public Object pop() {
        if (mTop <= 0) {
            throw new RuntimeException("Stack empty");
        }

        Object tmp = mData[--mTop];
        // mData[mTop] = null;
        mSize--;
        return tmp;
    }

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return mSize == 0;
    }
}
