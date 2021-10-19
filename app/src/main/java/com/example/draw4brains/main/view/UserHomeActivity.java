package com.example.draw4brains.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.main.controller.MasterMgr;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSelectGame, btnStats, btnAccInfo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_user);

        btnSelectGame = findViewById(R.id.btn_select_game);
        btnAccInfo = findViewById(R.id.btn_acc_info);
        btnStats = findViewById(R.id.btn_stats);

        btnSelectGame.setOnClickListener(this);
        btnAccInfo.setOnClickListener(this);
        btnStats.setOnClickListener(this);
    }

    /**
     * This function overwrites the onClick function for the View class Android to accommodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Select Game, View Account Information, View Statistics
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_game:
                intent = new Intent(UserHomeActivity.this, SelectGameActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_acc_info:
                intent = new Intent(UserHomeActivity.this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_stats:
                intent = new Intent(UserHomeActivity.this, StatisticsPageActivity.class);
                intent.putExtra("User Score", MasterMgr.authenticationMgr.getCurrentUser().userScore);
                intent.putExtra("Name", MasterMgr.authenticationMgr.getCurrentUser().getUserName());
                startActivity(intent);
                break;
        }
    }


}
