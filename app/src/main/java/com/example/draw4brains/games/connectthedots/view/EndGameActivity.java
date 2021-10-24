package com.example.draw4brains.games.connectthedots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.GameController;
import com.example.draw4brains.games.connectthedots.controller.GameDatabaseController;
import com.example.draw4brains.games.connectthedots.object.Constants;
import com.example.draw4brains.main.controller.AuthenticationController;
import com.example.draw4brains.main.controller.MasterController;

public class EndGameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMainMenu;
    private TextView dotScore, guessScore, totalScore;
    Intent intent;
    AuthenticationController authenticationController;
    String currentDotScoreString, currentGuessScoreString;

    GameDatabaseController gameDatabaseController;
    GameController gameController;

    private static final Class REFERENCE_TO_USER_HOME = com.example.draw4brains.main.view.UserHomeActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_end_game);

        // Get reference to controller classes needed.
        authenticationController = MasterController.authenticationController;
        gameDatabaseController = new GameDatabaseController();


        intent = getIntent();
        gameController = (GameController) intent.getSerializableExtra(Constants.INTENT_KEY_GAME_MANAGER);
        Log.d("scoreMgrDots", String.valueOf(gameController.getConnectScore()));
        Log.d("scoreMgrDots", String.valueOf(gameController.getGuessScore()));

        dotScore = findViewById(R.id.connectDotScoreText);
        guessScore = findViewById(R.id.guessScoreText);
        totalScore = findViewById(R.id.total_text);
        currentDotScoreString = String.valueOf(gameController.getConnectScore());
        currentGuessScoreString = String.valueOf(gameController.getGuessScore());

        Log.d("currentDotScore", currentDotScoreString);
        Log.d("currentGuessScore", currentGuessScoreString);

        dotScore.setText(currentDotScoreString + "/" + String.valueOf((int) Constants.MAX_SCORE_CONNECT_DOT));
        guessScore.setText(currentGuessScoreString + "/" + String.valueOf((int) Constants.MAX_SCORE_GUESS));
        totalScore.setText(String.valueOf(gameController.getConnectScore() + gameController.getGuessScore() + "/" +
                String.valueOf((int) (Constants.MAX_SCORE_GUESS + Constants.MAX_SCORE_CONNECT_DOT))));
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
                gameDatabaseController.updateScore(gameController, currentDotScoreString, currentGuessScoreString);
                intent = new Intent(EndGameActivity.this, REFERENCE_TO_USER_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
}