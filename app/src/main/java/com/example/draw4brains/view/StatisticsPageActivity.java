package com.example.draw4brains.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.draw4brains.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.draw4brains.R.color.light_blue;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
//        intent.putExtra("easydot",easy_dot);
//        intent.putExtra("easygames",easy_gamesplayed);
//        intent.putExtra("easyguess",easy_guess);
//        intent.putExtra("meddot",med_dot);
//        intent.putExtra("medgames",med_gamesplayed);
//        intent.putExtra("medguess",med_guess);
//        intent.putExtra("harddot",hard_dot);
//        intent.putExtra("hardgames",hard_gamesplayed);
//        intent.putExtra("hardguess",hard_guess);
        String Name = getIntent().getStringExtra("Name");
        int Score = getIntent().getIntExtra("Score",0);
        int number_played = getIntent().getIntExtra("number_played",0);

       easydot = cutting(getIntent().getStringExtra("easydot"));
        easygame = getIntent().getStringExtra("easygames");
        easyguess = cutting(getIntent().getStringExtra("easyguess"));


        meddot = cutting(getIntent().getStringExtra("meddot"));
        medgame = getIntent().getStringExtra("medgames");
        medguess = cutting(getIntent().getStringExtra("medguess"));

        harddot = cutting(getIntent().getStringExtra("harddot"));
       hardgame = getIntent().getStringExtra("hardgames");
         hardguess = cutting(getIntent().getStringExtra("hardguess"));

//        int average_score=0;
//        int i=Integer.parseInt(Score);
//        if (number_played != 0) {
//           average_score = i/number_played;
//        }

        Log.d(cutting(getIntent().getStringExtra("medguess")), "onCreate: ");
        TextView t_score = (TextView) findViewById(R.id.tv_stats);
        TextView t_name = (TextView) findViewById(R.id.tv_name);
        TextView t_games_played = (TextView) findViewById(R.id.no_games_played);
        TextView dot = (TextView) findViewById(R.id.textView_dots) ;
        TextView guess = (TextView) findViewById(R.id.textView_guess) ;
        TextView game = (TextView) findViewById(R.id.textView_games) ;

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
        t_score.setText(Integer.toString(Score));
        t_games_played.setText(Integer.toString(number_played));
    }


    public String cutting(String string){
        int length = string.length();
        StringBuilder cutdown= new StringBuilder();
        int comma_count = 0;
        while(length>0 && comma_count<3) {
            String cha = String.valueOf(string.charAt(length - 1));
            if (cha.equals(",") || cha.equals("，")) {
                cha = ",";
                Log.d(cha, "comma: ");
                comma_count += 1;
                Log.d(String.valueOf(comma_count), "commacount: ");
            }
            if (comma_count == 3){
            break;
        }
            cutdown.insert(0, cha);
            Log.d(cutdown.toString(), "string: ");
            length-=1;
        }
        return cutdown.toString();

    }
}