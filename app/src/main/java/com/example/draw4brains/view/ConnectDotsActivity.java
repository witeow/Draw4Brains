package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.draw4brains.R;

public class ConnectDotsActivity extends AppCompatActivity {

    private Button btnGiveUp, btnSubmit;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_connect_dots);

        btnSubmit = findViewById(R.id.btn_submit);
        btnGiveUp = findViewById(R.id.btn_give_up);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConnectDotsActivity.this, EndGameActivity.class);
                startActivity(intent);
            }
        });

        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConnectDotsActivity.this, EndGameActivity.class);
                startActivity(intent);
            }
        });
    }
}