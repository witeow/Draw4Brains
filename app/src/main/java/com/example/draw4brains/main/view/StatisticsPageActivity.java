package com.example.draw4brains.main.view;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.model.Score;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticsPageActivity extends AppCompatActivity {

    String easydot;
    String easygame;
    String easyguess;
    String meddot;
    String medgame;
    String medguess;
    String harddot;
    String hardgame;
    String hardguess;
    Score score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        String Name = getIntent().getStringExtra("Name");
        score = (Score) getIntent().getSerializableExtra("User Score");

        ArrayList<String> easyDotArray = score.getDots().get(score.getGameDifficulty().indexOf("easy"));
        Integer easyGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("easy"));
        ArrayList<String> easyGuessArray = score.getDots().get(score.getGameDifficulty().indexOf("easy"));

        ArrayList<String> mediumDotArray = score.getDots().get(score.getGameDifficulty().indexOf("medium"));
        Integer mediumGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("medium"));
        ArrayList<String> mediumGuessArray = score.getDots().get(score.getGameDifficulty().indexOf("medium"));

        ArrayList<String> hardDotArray = score.getDots().get(score.getGameDifficulty().indexOf("hard"));
        Integer hardGamesPlayed = score.getGamesPlayed().get(score.getGameDifficulty().indexOf("hard"));
        ArrayList<String> hardGuessArray = score.getDots().get(score.getGameDifficulty().indexOf("hard"));



        int easyScore =0;
        for(int game = 0;game<easyGamesPlayed;game++){
            easyScore += Integer.parseInt(easyDotArray.get(game));
            easyScore += Integer.parseInt(easyGuessArray.get(game));
        }

        int mediumScore =0;
        for(int game = 0;game<mediumGamesPlayed;game++){
            mediumScore += Integer.parseInt(mediumDotArray.get(game));
            mediumScore += Integer.parseInt(mediumGuessArray.get(game));
        }

        int hardScore =0;
        for(int game = 0;game<hardGamesPlayed;game++){
            hardScore += Integer.parseInt(hardDotArray.get(game));
            hardScore += Integer.parseInt(hardGuessArray.get(game));
        }

        int number_played = easyGamesPlayed + mediumGamesPlayed + hardGamesPlayed;
        int avgScore =0;
        if (number_played!=0){
            // avg score
            avgScore = (easyScore + mediumScore + hardScore)/number_played;
        }


        easydot = cutting(TextUtils.join(", ", easyDotArray));
        easygame = easyGamesPlayed.toString();
        easyguess = cutting(TextUtils.join(", ", easyGuessArray));

        meddot = cutting(TextUtils.join(", ", mediumDotArray));
        medgame = mediumGamesPlayed.toString();
        medguess = cutting(TextUtils.join(", ", mediumGuessArray));

        harddot = cutting(TextUtils.join(", ", hardDotArray));
        hardgame = hardGamesPlayed.toString();
        hardguess = cutting(TextUtils.join(", ", hardGuessArray));

        TextView t_score = (TextView) findViewById(R.id.tv_stats);
        TextView t_name = (TextView) findViewById(R.id.tv_name);
        TextView t_games_played = (TextView) findViewById(R.id.no_games_played);
        TextView dot = (TextView) findViewById(R.id.textView_dots);
        TextView guess = (TextView) findViewById(R.id.textView_guess);
        TextView game = (TextView) findViewById(R.id.textView_games);

        Button button_easy = (Button) findViewById(R.id.button_easy);

        Button button_med = (Button) findViewById(R.id.button_med);
        Button button_hard = (Button) findViewById(R.id.button_hard);
        button_easy.setBackgroundColor(Color.argb(50, 203, 58, 103));
        button_med.setBackgroundColor(Color.argb(100, 203, 58, 103));
        button_hard.setBackgroundColor(Color.argb(100, 203, 58, 103));
        dot.setText("Last 3 Dots Score: " + easydot);
        guess.setText("Last 3 Guess Score: " + easyguess);
        game.setText("Games: " + easygame);


        button_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_easy.setBackgroundColor(Color.argb(50, 203, 58, 103));
                button_med.setBackgroundColor(Color.argb(100, 203, 58, 103));
                button_hard.setBackgroundColor(Color.argb(100, 203, 58, 103));

                dot.setText("Last 3 Dots Score: " + easydot);
                guess.setText("Last 3 Guess Score: " + easyguess);
                game.setText("Games: " + easygame);
            }
        });

        button_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_med.setBackgroundColor(Color.argb(50, 203, 58, 103));
                button_easy.setBackgroundColor(Color.argb(100, 203, 58, 103));
                button_hard.setBackgroundColor(Color.argb(100, 203, 58, 103));

                dot.setText("Last 3 Dots Score: " + meddot);
                guess.setText("Last 3 Guess Score: " + medguess);
                game.setText("Games: " + medgame);
            }
        });

        button_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_hard.setBackgroundColor(Color.argb(50, 203, 58, 103));
                button_med.setBackgroundColor(Color.argb(100, 203, 58, 103));
                button_easy.setBackgroundColor(Color.argb(100, 203, 58, 103));

                dot.setText("Last 3 Dots Score: " + harddot);
                guess.setText("Last 3 Guess Score: " + hardguess);
                game.setText("Games: " + hardgame);
            }
        });

        t_name.setText(Name);
        t_score.setText(Integer.toString(avgScore));
        t_games_played.setText(Integer.toString(number_played));
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