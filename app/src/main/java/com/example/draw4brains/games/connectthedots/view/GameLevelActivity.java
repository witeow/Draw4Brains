package com.example.draw4brains.games.connectthedots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.main.view.SelectGameActivity;

import java.util.Random;

public class GameLevelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEasy, btnNormal, btnHard;
    private ImageButton btnBack;
    private TextView instructionTextView;
    public static String gameName;
    public static String gameType;
    public static String gameDifficulty;

    Intent intent;

    // Temp random levels
    private Random r;
    private String[] games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game_level);
        gameName = "";
        gameType = "";

        btnBack = findViewById(R.id.btn_back);
        btnEasy = findViewById(R.id.easyBtn);
        btnNormal = findViewById(R.id.normalBtn);
        btnHard = findViewById(R.id.hardBtn);
        instructionTextView = findViewById(R.id.instructionTextView);

        btnBack.setOnClickListener(this);
        btnEasy.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        instructionTextView.setText(
                "Instructions:\n\n\n" +
                        "Connect-The-Dot Game consist of 2 stages.\n\n" +
                        "Stage 1 - Connect the Dots\n" +
                        "Connect the dots based on the ordering (1,2,3,4,...)\n\n" +
                        "Stage 2 - Guess the Image\n" +
                        "Guess the image shown by entering the correct characters\n\n");
        r = new Random();


    }

    private void changeActivityToConnectDots() {
        intent = new Intent(GameLevelActivity.this, ConnectDotsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                intent = new Intent(GameLevelActivity.this, SelectGameActivity.class);
                gameName = "";
                gameDifficulty = "";
                startActivity(intent);
                break;
            case R.id.easyBtn:
                gameType = "connectDots";
                gameDifficulty = "easy"; // For use with database
                games = new String[]{"seven", "house"};
                gameName = games[r.nextInt(2)];
                changeActivityToConnectDots();
                break;
            case R.id.normalBtn:
                gameType = "connectDots";
                gameDifficulty = "medium"; // For use with database
                games = new String[]{"star", "one"};
                gameName = games[r.nextInt(2)];
                changeActivityToConnectDots();

                break;
            case R.id.hardBtn:
                gameType = "connectDots";
                gameDifficulty = "hard"; // For use with database
                games = new String[]{"shirt"};
                gameName = games[r.nextInt(1)];
                changeActivityToConnectDots();
                break;
        }
    }
}
