package com.example.draw4brains.games.connectthedots.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.model.Score;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticsPageActivity extends AppCompatActivity implements View.OnClickListener{

    String easyDot;
    String easyGame;
    String easyGuess;
    String mediumDot;
    String mediumGame;
    String mediumGuess;
    String hardDot;
    String hardGame;
    String hardGuess;
    Score score;

    TextView tScore, tName, tGamesPlayed, latestThreeDotScore, latestThreeGuessScore, gamesPlayed;
    Button buttonEasy, buttonMed, buttonHard;

    private Integer easyGamesPlayed, mediumGamesPlayed, hardGamesPlayed;
    private ArrayList<String> easyDotArray, mediumDotArray, hardDotArray;
    private ArrayList<String> easyGuessArray, mediumGuessArray, hardGuessArray;
    private int avgScoreEasy =0;
    private int avgScoreMedium =0;
    private int avgScoreHard = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from intent
        String Name = getIntent().getStringExtra("Name");
        score = (Score) getIntent().getSerializableExtra("User Score");

        easyDotArray = score.getDots().get(score.getGameDifficulty().indexOf("easy"));
        easyGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("easy"));
        easyGuessArray = score.getGuess().get(score.getGameDifficulty().indexOf("easy"));

        mediumDotArray = score.getDots().get(score.getGameDifficulty().indexOf("medium"));
        mediumGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("medium"));
        mediumGuessArray = score.getGuess().get(score.getGameDifficulty().indexOf("medium"));

        hardDotArray = score.getDots().get(score.getGameDifficulty().indexOf("hard"));
        hardGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("hard"));
        hardGuessArray = score.getGuess().get(score.getGameDifficulty().indexOf("hard"));

        // Get avg score for easy difficulty level
        if (easyGamesPlayed != 0) {
            int easyScore =0;
            for(int game = 0;game<easyGamesPlayed;game++){
                easyScore += Integer.parseInt(easyDotArray.get(game));
                easyScore += Integer.parseInt(easyGuessArray.get(game));
            }
            avgScoreEasy = easyScore/easyGamesPlayed;
        }

        if (mediumGamesPlayed != 0) {
            int mediumScore =0;
            for(int game = 0;game<mediumGamesPlayed;game++){
                mediumScore += Integer.parseInt(mediumDotArray.get(game));
                mediumScore += Integer.parseInt(mediumGuessArray.get(game));
            }
            avgScoreMedium = mediumScore/mediumGamesPlayed;
        }

        if (hardGamesPlayed != 0) {
            int hardScore =0;
            for(int game = 0;game<hardGamesPlayed;game++){
                hardScore += Integer.parseInt(hardDotArray.get(game));
                hardScore += Integer.parseInt(hardGuessArray.get(game));
            }
            avgScoreHard = hardScore/hardGamesPlayed;
        }

        // Get first 3 score for dot and guess
        easyGame = easyGamesPlayed.toString();
        easyDot = cutting(TextUtils.join(", ", easyDotArray));
        easyGuess = cutting(TextUtils.join(", ", easyGuessArray));

        mediumGame = mediumGamesPlayed.toString();
        mediumDot = cutting(TextUtils.join(", ", mediumDotArray));
        mediumGuess = cutting(TextUtils.join(", ", mediumGuessArray));

        hardGame = hardGamesPlayed.toString();
        hardDot = cutting(TextUtils.join(", ", hardDotArray));
        hardGuess = cutting(TextUtils.join(", ", hardGuessArray));

        tScore = (TextView) findViewById(R.id.tv_stats);
        tName = (TextView) findViewById(R.id.tv_name);
        tGamesPlayed = (TextView) findViewById(R.id.no_games_played);
        latestThreeDotScore = (TextView) findViewById(R.id.textView_dots);
        latestThreeGuessScore = (TextView) findViewById(R.id.textView_guess);
        gamesPlayed = (TextView) findViewById(R.id.textView_games);

        buttonEasy = (Button) findViewById(R.id.button_easy);
        buttonMed = (Button) findViewById(R.id.button_med);
        buttonHard = (Button) findViewById(R.id.button_hard);
        buttonEasy.setBackgroundColor(Color.argb(50, 203, 58, 103));
        buttonMed.setBackgroundColor(Color.argb(100, 203, 58, 103));
        buttonHard.setBackgroundColor(Color.argb(100, 203, 58, 103));

        buttonEasy.setOnClickListener(this);
        buttonMed.setOnClickListener(this);
        buttonHard.setOnClickListener(this);


        // Default is easy
        latestThreeDotScore.setText("Last 3 Dots Score: \n" + easyDot);
        latestThreeGuessScore.setText("Last 3 Guess Score: \n" + easyGuess);
        gamesPlayed.setText("Games: \n" + easyGame);
        tScore.setText(Integer.toString(avgScoreEasy));
        tGamesPlayed.setText(Integer.toString(easyGamesPlayed));
        tName.setText(Name);

    }

    /**
     * This function overwrites the onClick function for the View class Android to accommodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Login, Register, Forget Password
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_easy:
                buttonEasy.setBackgroundColor(Color.argb(50, 203, 58, 103));
                buttonMed.setBackgroundColor(Color.argb(100, 203, 58, 103));
                buttonHard.setBackgroundColor(Color.argb(100, 203, 58, 103));

                latestThreeDotScore.setText("Last 3 Dots Score: \n" + easyDot);
                latestThreeGuessScore.setText("Last 3 Guess Score: \n" + easyGuess);
                gamesPlayed.setText("Games: \n" + easyGame);
                tScore.setText(Integer.toString(avgScoreEasy));
                tGamesPlayed.setText(Integer.toString(easyGamesPlayed));
                break;
            case R.id.button_med:
                buttonMed.setBackgroundColor(Color.argb(50, 203, 58, 103));
                buttonEasy.setBackgroundColor(Color.argb(100, 203, 58, 103));
                buttonHard.setBackgroundColor(Color.argb(100, 203, 58, 103));

                latestThreeDotScore.setText("Last 3 Dots Score: \n" + mediumDot);
                latestThreeGuessScore.setText("Last 3 Guess Score: \n" + mediumGuess);
                gamesPlayed.setText("Games: \n" + mediumGame);
                tScore.setText(Integer.toString(avgScoreMedium));
                tGamesPlayed.setText(Integer.toString(mediumGamesPlayed));
                break;
            case R.id.button_hard:
                buttonHard.setBackgroundColor(Color.argb(50, 203, 58, 103));
                buttonMed.setBackgroundColor(Color.argb(100, 203, 58, 103));
                buttonEasy.setBackgroundColor(Color.argb(100, 203, 58, 103));

                latestThreeDotScore.setText("Last 3 Dots Score: \n" + hardDot);
                latestThreeGuessScore.setText("Last 3 Guess Score: \n" + hardGuess);
                gamesPlayed.setText("Games: \n" + hardGame);
                tScore.setText(Integer.toString(avgScoreHard));
                tGamesPlayed.setText(Integer.toString(hardGamesPlayed));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public String cutting(String string) {
        int length = string.length();
        StringBuilder cutdown = new StringBuilder();
        int comma_count = 0;
        while (length > 0 && comma_count < 3) {
            String cha = String.valueOf(string.charAt(length - 1));
            if (cha.equals(",") || cha.equals("，")) {
                cha = ",";
                Log.d(cha, "comma: ");
                comma_count += 1;
                Log.d(String.valueOf(comma_count), "commacount: ");
            }
            if (comma_count == 3) {
                break;
            }
            cutdown.insert(0, cha);
            Log.d(cutdown.toString(), "string: ");
            length -= 1;
        }
        return cutdown.toString();

    }
}