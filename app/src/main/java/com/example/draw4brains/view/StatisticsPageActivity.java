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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        String Name = getIntent().getStringExtra("Name");
        String Score = getIntent().getStringExtra("Score");
        int number_played = getIntent().getIntExtra("number_played",0);
//        int average_score=0;
//        int i=Integer.parseInt(Score);
//        if (number_played != 0) {
//           average_score = i/number_played;
//        }

        TextView t_score = (TextView) findViewById(R.id.tv_stats);
        TextView t_name = (TextView) findViewById(R.id.tv_name);
        TextView t_games_played = (TextView) findViewById(R.id.no_games_played);



        t_name.setText(Name);
        t_score.setText(Score);
        t_games_played.setText(Integer.toString(number_played));
    }
}