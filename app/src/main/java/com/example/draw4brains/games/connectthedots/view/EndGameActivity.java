package com.example.draw4brains.games.connectthedots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.GameMgr;
import com.example.draw4brains.main.controller.AuthenticationMgr;
import com.example.draw4brains.main.controller.MasterMgr;
import com.example.draw4brains.main.model.Score;
import com.example.draw4brains.main.view.UserHomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EndGameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMainMenu;
    private TextView dotScore, guessScore;
    Intent intent;
//    ScoreMgr scoreMgr;
    AuthenticationMgr authenticationMgr;
    String currentDotScore, currentGuessScore;

    GameMgr gameMgr;
    private static final String INTENT_GAME_MANAGER = "GAME_MANAGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_end_game);

        // Get reference to controller classes needed.
        authenticationMgr = MasterMgr.authenticationMgr;


        intent = getIntent();
//        scoreMgr = (ScoreMgr) intent.getSerializableExtra("score");
        gameMgr = (GameMgr) intent.getSerializableExtra(INTENT_GAME_MANAGER);
        Log.d("scoreMgrDots", String.valueOf(gameMgr.getConnectScore()));
        Log.d("scoreMgrDots", String.valueOf(gameMgr.getGuessScore()));
        dotScore = findViewById(R.id.connectDotScoreText);
        guessScore = findViewById(R.id.guessScoreText);

//        dotScore.setText("TESTING");
//        guessScore.setText("TESTING");

        currentDotScore = String.valueOf(gameMgr.getConnectScore());
        currentGuessScore = String.valueOf(gameMgr.getGuessScore());
        Log.d("currentDotScore", currentDotScore);
        Log.d("currentGuessScore", currentGuessScore);

        dotScore.setText(currentDotScore);
        guessScore.setText(currentGuessScore);
        btnMainMenu = findViewById(R.id.btn_main_menu);

        btnMainMenu.setOnClickListener(this);

    }


    /**
     * This function overwrites the onClick function for the View class Android to accomodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Back to Main Menu
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_menu:
                FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
                Score score = authenticationMgr.getCurrentUser().getScore();
                DatabaseReference userRef = userDb.getReference("Score").child(score.getUserId())
                        .child(gameMgr.getLevelInfo().getGameType())
                        .child(gameMgr.getLevelInfo().getGameDifficulty());


                Integer diffucultyIndex = score.getGameDifficulty().indexOf(gameMgr.getLevelInfo().getGameDifficulty());

                ArrayList<Integer> newNumPlayed = score.getGamesPlayed();
                ArrayList<ArrayList<String>> newDots = score.getDots();
                ArrayList<ArrayList<String>> newGuess = score.getGuess();

                if (score.getGamesPlayed().get(diffucultyIndex) != 0) {
                    newDots.get(diffucultyIndex).add(currentDotScore);
                    newGuess.get(diffucultyIndex).add(currentGuessScore);

                } else {
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

                score.setGamesPlayed(newNumPlayed);
                score.setDots(newDots);
                score.setDots(newGuess);
                intent = new Intent(EndGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}