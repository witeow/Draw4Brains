package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.draw4brains.R;

public class ConnectDotsActivity extends AppCompatActivity {

    Button giveUpButton, completeGameButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_connect_dots);

        completeGameButton = findViewById(R.id.complete_game_button);
        giveUpButton = findViewById(R.id.give_up_button);

        completeGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConnectDotsActivity.this, EndGameActivity.class);
                startActivity(intent);
            }
        });

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConnectDotsActivity.this, EndGameActivity.class);
                startActivity(intent);
            }
        });
    }
}