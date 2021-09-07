package com.example.draw4brains.view;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.draw4brains.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class StatisticsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String ClinicID = getIntent().getStringExtra("Clinic ID");
        String clinicName= getIntent().getStringExtra("Clinic Name");
    }
}
