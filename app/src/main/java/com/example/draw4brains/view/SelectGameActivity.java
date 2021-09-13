package com.example.draw4brains.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.ChronometerMgr;
import com.example.draw4brains.controller.LoginMgr;

public class SelectGameActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamestart_page);
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChronometerMgr timerMgr = new ChronometerMgr();
                timerMgr.startChronometer(SelectGameActivity.this);
            }
        });
    }
}
