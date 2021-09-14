package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.draw4brains.R;

public class GameLevelActivity extends AppCompatActivity {

    Button testLevel1, backButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game_level);

        backButton = findViewById(R.id.btn_back_game);
        testLevel1 = findViewById(R.id.level_1_button);

        testLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameLevelActivity.this, ConnectDotsActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameLevelActivity.this, SelectGameActivity.class);
                startActivity(intent);
            }
        });

    }
}