package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class SelectGameActivity extends AppCompatActivity {

    private Button btnGame1, btnBackHome;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game);

        btnGame1 = findViewById(R.id.btn_game1);
        btnBackHome = findViewById(R.id.btn_back_home);

        btnGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SelectGameActivity.this, GameLevelActivity.class);
                startActivity(intent);
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SelectGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}