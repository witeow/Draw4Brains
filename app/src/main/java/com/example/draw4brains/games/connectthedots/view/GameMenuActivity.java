package com.example.draw4brains.games.connectthedots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.DatabaseMgr;
import com.example.draw4brains.games.connectthedots.controller.GameMgr;
import com.example.draw4brains.games.connectthedots.model.ConnectDots;
import com.example.draw4brains.games.connectthedots.model.Constants;
import com.example.draw4brains.games.connectthedots.model.Level;
import com.example.draw4brains.games.connectthedots.model.Node;
import com.example.draw4brains.main.view.SelectGameActivity;

import java.util.ArrayList;
import java.util.Random;

public class GameMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEasy, btnNormal, btnHard;
    private ImageButton btnBack;
    private TextView instructionTextView;

    Intent intent;
    GameMgr gameMgr;

    private static final String GAME_TYPE = "connectDots";
    private static final String[] EASY_GAMES = new String[]{"seven", "house"};
    private static final String[] NORMAL_GAMES = new String[]{"star", "one"};
    private static final String[] HARD_GAMES = new String[]{"shirt"};

    // Intent Key
    private static final String INTENT_KEY_GAME_MANAGER = "GAME_MANAGER";

    DatabaseMgr databaseMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_game_level);

        // Link database manager
        databaseMgr = new DatabaseMgr();

        // Initialize XML Elements to use
        btnBack = findViewById(R.id.btn_back);
        btnEasy = findViewById(R.id.easyBtn);
        btnNormal = findViewById(R.id.normalBtn);
        btnHard = findViewById(R.id.hardBtn);
        instructionTextView = findViewById(R.id.instructionTextView);

        // Set Listeners
        btnBack.setOnClickListener(this);
        btnEasy.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        instructionTextView.setText(
                "Instructions:\n\n\n" +
                        "Connect-The-Dot Game consist of 2 stages.\n\n" +
                        "Stage 1 - Connect the Dots\n" +
                        "Connect the dots based on the ordering (1,2,3,4,...)\n\n" +
                        "Stage 2 - Guess the Image\n" +
                        "Guess the image shown by entering the correct characters\n\n");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                intent = new Intent(GameMenuActivity.this, SelectGameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.easyBtn:
                Level easyLevel = createNewGameInstance("easy");
                startGame(easyLevel);
                break;
            case R.id.normalBtn:
                Level mediumLevel = createNewGameInstance("medium");
                startGame(mediumLevel);
                break;
            case R.id.hardBtn:
                Level hardLevel = createNewGameInstance("hard");
                startGame(hardLevel);
                break;
        }
    }

    public Level createNewGameInstance(String gameDifficulty) {
        String gameType = GAME_TYPE;
        String gameName;
        Random r = new Random();
        String[] availableGames = null;
        switch (gameDifficulty) {
            case "easy":
                availableGames = EASY_GAMES;
                break;
            case "medium":
                availableGames = NORMAL_GAMES;
                break;
            case "hard":
                availableGames = HARD_GAMES;
                break;
        }
        int noOfGames = availableGames.length;
        gameName = availableGames[r.nextInt(noOfGames)];
        return new Level(gameName, gameDifficulty, gameType);
    }

//    String gameName = levelInfo.getGameName();
//    String storageUrl = ConnectDots.firebaseStorageUrl + gameName + ".jpg";
//    connectDotsLevelObject = new ConnectDots(gameName, cordString, gameLevel, storageUrl);
////                    Log.d("ConnectDotsgameName", newGame.getStorageStringRef());


    private void startGame(Level levelInfo) {
        this.gameMgr = new GameMgr(levelInfo);
        databaseMgr.loadNodeData(gameMgr, new DatabaseMgr.onCompleteDataLoad() {
            @Override
            public void onComplete(ArrayList<Node> preprocessedNodes, String[] cordString, int gameLevel) {
                // Load game level object to ConenctDot attribute inside gameMgr and change activity.
                Log.d("ArrayNode", preprocessedNodes.toString());
                String gameName = gameMgr.getLevelInfo().getGameName();
                String storageUrl = ConnectDots.firebaseStorageUrl + gameName + ".jpg";
                ConnectDots levelObject = new ConnectDots(gameName, cordString, gameLevel, storageUrl);
                gameMgr.setConnectDotsLevelObject(levelObject);
                Log.d("ConnectDotsgameName", levelObject.getStorageStringRef());
                gameMgr.setNodeList(preprocessedNodes); // Get from database first but cannot scale yet
                Intent intent = new Intent(GameMenuActivity.this, ConnectDotsActivity.class);
                intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, gameMgr);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
//        intent = new Intent(GameMenuActivity.this, ConnectDotsActivity.class);
//        intent.putExtra(INTENT_KEY_GAME_MANAGER, gameMgr);
//        startActivity(intent);
//        Intent intent = new Intent(gameMenuActivity, ConnectDotsActivity.class);
//        intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, currentGameManagerInstance);
//        gameMenuActivity.startActivity(intent);
    }
}
