package com.example.draw4brains.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.GameDatabaseController;
import com.example.draw4brains.games.connectthedots.object.Score;
import com.example.draw4brains.main.controller.MasterController;

public class GameStatisticActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Class CONNECT_THE_DOT_STATISTICS = com.example.draw4brains.games.connectthedots.view.StatisticsPageActivity.class;

    private ImageButton btnConnectDot, btnBackHome;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game_stat);

        btnConnectDot = findViewById(R.id.connectDotGame);
        btnBackHome = findViewById(R.id.back_button);


        btnConnectDot.setOnClickListener(this);
        btnBackHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connectDotGame:
                GameDatabaseController gameDatabaseController = new GameDatabaseController();
                gameDatabaseController.getScoreFromDatabase(MasterController.authenticationController.getCurrentUser().getUserID(), new GameDatabaseController.onCallBackUserScore() {
                    @Override
                    public void onCallback(Score score) {
                        MasterController.authenticationController.getCurrentUser().setScore(score);
                        intent = new Intent(GameStatisticActivity.this, CONNECT_THE_DOT_STATISTICS);
                        intent.putExtra("User Score", MasterController.authenticationController.getCurrentUser().userScore);
                        intent.putExtra("Name", MasterController.authenticationController.getCurrentUser().getUserName());
                        startActivity(intent);
                    }
                });
                break;
            case R.id.back_button:
                intent = new Intent(GameStatisticActivity.this, UserHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}