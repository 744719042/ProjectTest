package com.example.mytestproject;

import com.example.mytestproject.utils.ThreadUtils;

public class DeadLockTest {
    // 第一个所对象
    private static final Object lock1 = new Object();
    // 第二个锁对象
    private static final Object lock2 = new Object();

    private static final Runnable task1 = new Runnable() {
        @Override
        public void run() {
            synchronized (lock1) {
                ThreadUtils.sleep(200);
                synchronized (lock2) {
                    System.out.println("Task1 got two locks");
                }
            }
        }
    };

    private static final Runnable task2 = new Runnable() {
        @Override
        public void run() {
            synchronized (lock1) {
                ThreadUtils.sleep(200);
                synchronized (lock2) {
                    System.out.println("Task2 got two locks");
                }
            }
        }
    };

    public static void main(String[] args) {
        new Thread(task1).start();
        new Thread(task2).start();
    }
}

