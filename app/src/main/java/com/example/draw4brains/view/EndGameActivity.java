package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.draw4brains.R;

public class EndGameActivity extends AppCompatActivity {

    private Button btnMainMenu;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_end_game);

        btnMainMenu = findViewById(R.id.btn_main_menu);

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(EndGameActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });

    }
}