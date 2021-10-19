package com.example.draw4brains.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class SelectGameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Class CONNECT_THE_DOT_REFERENCE_POINT = com.example.draw4brains.games.connectthedots.view.GameMenuActivity.class;

    private ImageButton btnConnectDot, btnBackHome;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game);

        btnConnectDot = findViewById(R.id.connectDotGame);
        btnBackHome = findViewById(R.id.back_button);

        btnConnectDot.setOnClickListener(this);
        btnBackHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectDotGame:
                intent = new Intent(SelectGameActivity.this, CONNECT_THE_DOT_REFERENCE_POINT);
                startActivity(intent);
                break;
            case R.id.back_button:
                intent = new Intent(SelectGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}