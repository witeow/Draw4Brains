package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.ScoreMgr;
import com.example.draw4brains.model.Score;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

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
        Log.d("scoreMgrDots", String.valueOf(scoreMgr.getDotScore()));
        Log.d("scoreMgrDots", String.valueOf(scoreMgr.getGuessScore()));
        dotScore = findViewById(R.id.connectDotScoreText);
        guessScore = findViewById(R.id.guessScoreText);

//        dotScore.setText("TESTING");
//        guessScore.setText("TESTING");

        String currentDotScore = String.valueOf(scoreMgr.getDotScore());
        String currentGuessScore = String.valueOf(scoreMgr.getGuessScore());
        Log.d("currentDotScore", currentDotScore);
        Log.d("currentGuessScore", currentGuessScore);

        dotScore.setText(currentDotScore);
        guessScore.setText(currentGuessScore);

        btnMainMenu = findViewById(R.id.btn_main_menu);
        btnMainMenu = findViewById(R.id.btn_main_menu);
        btnMainMenu = findViewById(R.id.btn_main_menu);



        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference userRef = userDb.getReference("Score").child(LoginActivity.userScore.getUserId())
                        .child(GameLevelActivity.gameType)
                        .child(GameLevelActivity.gameDifficulty);

                Integer diffucultyIndex = LoginActivity.userScore.getGameDifficulty().indexOf(GameLevelActivity.gameDifficulty);

                ArrayList<Integer> newNumPlayed = LoginActivity.userScore.getGamesPlayed();
                ArrayList<ArrayList<String>> newDots = LoginActivity.userScore.getDots();
                ArrayList<ArrayList<String>> newGuess = LoginActivity.userScore.getGuess();

                if (LoginActivity.userScore.getGamesPlayed().get(diffucultyIndex) != 0){
                    newDots.get(diffucultyIndex).add(currentDotScore);
                    newGuess.get(diffucultyIndex).add(currentGuessScore);

                }else{
                    newDots.get(diffucultyIndex).set(0, currentDotScore);
                    newGuess.get(diffucultyIndex).set(0, currentGuessScore);
                }

                String dotsStr = TextUtils.join(",", newDots.get(diffucultyIndex));
                String guessStr = TextUtils.join(",", newGuess.get(diffucultyIndex));
                Integer numPlayed = newNumPlayed.get(diffucultyIndex) + 1;
                newNumPlayed.set(diffucultyIndex, numPlayed);

                userRef.child("dots").setValue(dotsStr);
                userRef.child("guess").setValue(guessStr);
                userRef.child("gamesPlayed").setValue(Integer.toString(numPlayed));

                LoginActivity.userScore.setGamesPlayed(newNumPlayed);
                LoginActivity.userScore.setDots(newDots);
                LoginActivity.userScore.setDots(newGuess);
                intent = new Intent(EndGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}