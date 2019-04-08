package com.example.mytestproject;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mytestproject.utils.HttpNetUtils;

import java.lang.ref.WeakReference;

public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        HttpNetUtils.get("http://www.baidu.com", new HttpNetUtils.Callback() {
            @Override
            public void onSuccess(final String text) {
                getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });

//        HttpNetUtils.get("http://www.baidu.com", new MyCallback(this));
    }

    private static class MyCallback implements HttpNetUtils.Callback {
        private WeakReference<Activity> mActivity;

        public MyCallback(Activity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(final String text) {
            final Activity activity = mActivity.get();
            if (activity != null && !activity.isFinishing()) {
                final Application application = activity.getApplication();
                activity.getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void onFailure() {

        }
    }
}
