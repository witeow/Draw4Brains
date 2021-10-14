package com.example.draw4brains.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.model.Node;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GameLevelActivity extends AppCompatActivity {

    private Button btnEasy, btnNormal, btnHard;
    private ImageButton btnBack;
    private TextView instructionTextView;
    public static String gameName;
    public static String gameType;
    public static String gameDifficulty;

    Intent intent;
    ArrayList<String> levelString = new ArrayList<>();

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

        r = new Random();

        instructionTextView.setText(
                "Instructions:\n\n\n" +
                "Connect-The-Dot Game consist of 2 stages.\n\n" +
                "Stage 1 - Connect the Dots\n" +
                "Connect the dots based on the ordering (1,2,3,4,...)\n\n" +
                "Stage 2 - Guess the Image\n" +
                "Guess the image shown by entering the correct characters\n\n");

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType = "connectDots";
                gameDifficulty = "easy"; // For use with database
                games = new String[] {"seven", "house"};
                gameName = games[r.nextInt(2)];
                changeActivitytoConnectDots(view);
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType = "connectDots";
                gameDifficulty = "normal"; // For use with database
                games = new String[] {"star", "one"};
                gameName = games[r.nextInt(2)];
                changeActivitytoConnectDots(view);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType = "connectDots";
                gameDifficulty = "hard"; // For use with database
                games = new String[] {"shirt"};
                gameName = games[r.nextInt(1)];
                changeActivitytoConnectDots(view);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameLevelActivity.this, SelectGameActivity.class);
                gameName = "";
                gameDifficulty = "";
                startActivity(intent);
            }
        });

    }

    private void changeActivitytoConnectDots(View view){
        intent = new Intent(GameLevelActivity.this, ConnectDotsActivity.class);
        startActivity(intent);
    }
}