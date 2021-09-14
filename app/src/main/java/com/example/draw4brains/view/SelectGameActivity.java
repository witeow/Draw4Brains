package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class SelectGameActivity extends AppCompatActivity {

    Button selectGame1Button, backButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game);

        selectGame1Button = findViewById(R.id.game1_button);
        backButton = findViewById(R.id.btn_back_home);

        selectGame1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SelectGameActivity.this, GameLevelActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SelectGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}