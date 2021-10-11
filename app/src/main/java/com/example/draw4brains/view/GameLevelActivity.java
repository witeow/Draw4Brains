package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.draw4brains.R;

public class GameLevelActivity extends AppCompatActivity {

    private Button btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5, btnBack;
    public static String gameName;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game_level);
        gameName = "";

        btnBack = findViewById(R.id.btn_back);
        btnLevel1 = findViewById(R.id.btn_level1);
        btnLevel2 = findViewById(R.id.btn_level2);
        btnLevel3 = findViewById(R.id.btn_level3);
        btnLevel4 = findViewById(R.id.btn_level4);
        btnLevel5 = findViewById(R.id.btn_level5);

        btnLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = "seven";
                changeActivitytoConnectDots(view);
            }
        });

        btnLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = "house";
                changeActivitytoConnectDots(view);
            }
        });

        btnLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = "star";
                changeActivitytoConnectDots(view);
            }
        });

        btnLevel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = "one";
                changeActivitytoConnectDots(view);
            }
        });

        btnLevel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameName = "shirt";
                changeActivitytoConnectDots(view);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameLevelActivity.this, SelectGameActivity.class);
                gameName = "";
                startActivity(intent);
            }
        });

    }

    private void changeActivitytoConnectDots(View view){
        intent = new Intent(GameLevelActivity.this, ConnectDotsActivity.class);
        startActivity(intent);
    }
}