package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

import java.security.AccessControlContext;

public class UserHomeActivity extends AppCompatActivity {

    private ImageButton btnSelectGame, btnStats, btnAccInfo;

    boolean isAdmin;
    Bundle extras;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_user);

        // Get data of intent
        extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean("isAdmin");
            //The key argument here must match that used in the other activity
        }

        btnSelectGame = findViewById(R.id.btn_select_game);
        btnAccInfo = findViewById(R.id.btn_acc_info);
        btnStats = findViewById(R.id.btn_stats);


        btnSelectGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SelectGameActivity.class);
                startActivity(intent);
            }
        });

        btnAccInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserHomeActivity.this, StatisticsPageActivity.class);
                        intent.putExtra("Name","Hard Coded Test");
                        intent.putExtra("Score",String.valueOf(100));
                        Log.d("intent", String.valueOf(intent.getStringExtra("Name")));
                        Log.d("intent", String.valueOf(intent.getStringExtra("Score")));
                        startActivity(intent);
                    }
        });

    }
}
