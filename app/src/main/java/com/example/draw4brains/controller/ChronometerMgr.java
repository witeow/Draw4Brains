package com.example.draw4brains.controller;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;


public class ChronometerMgr extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean isRunning;
//    Button startBtn = findViewById(R.id.startBtn);
//    Button pauseBtn = findViewById(R.id.pauseBtn);
//    Button resetBtn = findViewById(R.id.resetBtn);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_dots);
        chronometer = findViewById(R.id.chronometer);

//        startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isRunning){
//                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
//                    chronometer.start();
//                    isRunning = true;
//                }
//                Log.d("start clicked", "yes");
//            }
//        });
//
//        pauseBtn.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isRunning){
//                    chronometer.stop();
//                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
//                    isRunning = false;
//                }
//                Log.d("pause clicked", "yes");
//
//            }
//        }));
//
//        resetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                pauseOffset = 0;
//                Log.d("reset clicked", "yes");
//            }
//        });

    }


}