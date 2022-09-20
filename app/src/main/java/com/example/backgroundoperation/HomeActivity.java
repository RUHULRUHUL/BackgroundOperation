package com.example.backgroundoperation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private String LogTag = "OnCreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

/*        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 1");
            }
        });*/

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Executors Thread 1");
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Executors Thread 2");
            }
        });

/*        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 3");
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 4");
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 5");
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 6");
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(LogTag, "Runnable Thread 7");
            }
        });*/


    }
}