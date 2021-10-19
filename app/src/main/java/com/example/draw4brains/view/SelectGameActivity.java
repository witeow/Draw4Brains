package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnGame1, btnBackHome;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game);

        btnGame1 = findViewById(R.id.connectDotGame);
        btnBackHome = findViewById(R.id.back_button);

        btnGame1.setOnClickListener(this);
        btnBackHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectDotGame:
                intent = new Intent(SelectGameActivity.this, GameLevelActivity.class);
                startActivity(intent);
                break;
            case R.id.back_button:
                intent = new Intent(SelectGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}