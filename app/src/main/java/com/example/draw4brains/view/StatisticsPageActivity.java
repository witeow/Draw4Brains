package com.example.draw4brains.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.example.draw4brains.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String Name = getIntent().getStringExtra("Name");
        String Score = getIntent().getStringExtra("Score");

        TextView t_score = (TextView) findViewById(R.id.statistic);
        TextView t_name = (TextView) findViewById(R.id.name);

        t_name.setText(Name);
        t_score.setText(Score);
    }
}