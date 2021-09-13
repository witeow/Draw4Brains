package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.draw4brains.R;

public class EndGameActivity extends AppCompatActivity {

    Button mainMenuButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_end_game);

        mainMenuButton = findViewById(R.id.return_to_main_menu_button);

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EndGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}