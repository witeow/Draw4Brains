package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.ScoreMgr;

public class EndGameActivity extends AppCompatActivity {

    private Button btnMainMenu;
    private TextView dotScore, guessScore;
    Intent intent;
    ScoreMgr scoreMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_end_game);

        intent = getIntent();
        scoreMgr = (ScoreMgr) intent.getSerializableExtra("score");
        dotScore = findViewById(R.id.connectDotScoreText);
        guessScore = findViewById(R.id.guessScoreText);

        dotScore.setText("TESTING");
        guessScore.setText("TESTING");

        dotScore.setText(String.valueOf(scoreMgr.getDotScore()));
        guessScore.setText(String.valueOf(scoreMgr.getGuessScore()));

        btnMainMenu = findViewById(R.id.btn_main_menu);
        btnMainMenu = findViewById(R.id.btn_main_menu);
        btnMainMenu = findViewById(R.id.btn_main_menu);



        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EndGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}