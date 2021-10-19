package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

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

        btnSelectGame.setOnClickListener(this);
        btnAccInfo.setOnClickListener(this);
        btnStats.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_game:
                intent = new Intent(getApplicationContext(), SelectGameActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_acc_info:
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
                break;
            case R.id.btn_stats:
                Intent intent = new Intent(UserHomeActivity.this, StatisticsPageActivity.class);
                startActivity(intent);
                break;
        }
    }


}
