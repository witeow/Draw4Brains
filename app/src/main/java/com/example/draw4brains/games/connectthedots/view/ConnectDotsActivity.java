package com.example.draw4brains.games.connectthedots.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.GameController;
import com.example.draw4brains.games.connectthedots.object.Constants;
import com.example.draw4brains.games.connectthedots.object.Node;
import com.example.draw4brains.games.connectthedots.widget.CanvasView;

import java.util.ArrayList;

public class ConnectDotsActivity extends AppCompatActivity implements View.OnClickListener {

    private static RelativeLayout relLayout;
    private CanvasView canvasView;
    private Button giveUpButton, undoBtn;
    private Chronometer chronometer;
    Intent intent;


    // Game Initialization
    private int canvasWidth;
    private int canvasHeight;

    // For Game Logic Checking & Interaction
    private int offset;
    private int startNode;
    private int startX = 0;
    private int startY = 0;
    private int circleToCheckX;
    private int circleToCheckY;
    private int nextCircleToCheckX;
    private int nextCircleToCheckY;
    private int radiusIntForGame;
    ArrayList<Node> nodeList;
    GameController gameController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_connect_dots);

        // Initilize XML Elements to use
        relLayout = findViewById(R.id.relativeLayout);
        canvasView = findViewById(R.id.canvasView);
        giveUpButton = findViewById(R.id.btn_give_up);
        chronometer = findViewById(R.id.chronometer);
        undoBtn = findViewById(R.id.undoBtn);

        // Set Listeners
        giveUpButton.setOnClickListener(this);
        undoBtn.setOnClickListener(this);

        // Parse intent information (Passing GameController)
        intent = getIntent();
        gameController = (GameController) intent.getSerializableExtra(Constants.INTENT_KEY_GAME_MANAGER);
        Log.d("Serializable", "Success!" + gameController.toString());

        // Initialize the game here at this point
        relLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Get generated space for relative layout and create canvas
                Log.d("ViewTreeValues", "Called once!");
                int[] dimensions = getCanvasDimensions(relLayout); // Dimensions of relative layout
                // Scaling and displaying on canvas
                canvasWidth = dimensions[0];
                canvasHeight = dimensions[1];
                canvasView.createCanvas(relLayout, canvasWidth, canvasHeight);
                gameController.calibrateNodeToCanvasSize(canvasWidth, canvasHeight, Constants.PERCENTAGE_FILL_REQUIRED, Constants.SCALING_FACTOR, Constants.SCALING_FACTOR_MODIFIER);
                gameController.createCircles(ConnectDotsActivity.this);
                displayCirclesOnLayout(gameController, relLayout);

                // Initialize tools
                offset = getYOffset(relLayout);
                nodeList = gameController.getNodeList();
                startNode = 0;
                radiusIntForGame = gameController.getRadiusOfNodes();

                // Start timer
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                relLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    /**
     * This function overwrites the onClick function for the View class Android to accommodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Login, Register, Forget Password
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_give_up:
                gameController.calculateConnectScore(99999999, 1);
                intent = new Intent(ConnectDotsActivity.this, GuessImageActivity.class);
                intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, gameController);
                gameController.dropImageViewsAfterGameEnd();
                startActivity(intent);
                break;
            case R.id.undoBtn:
                canvasView.onClickUndo();
                gameController.getNodeList().get(startNode).setNodeBackgroundColor(Color.TRANSPARENT);
                if (startNode != 0) {
                    Log.d("startNode", "StartNode Value during undo (i): " + String.valueOf(startNode));
                    startNode--;
                    Log.d("startNode", "StartNode Value during undo (ii): " + String.valueOf(startNode));
                }
        }
    }

    private void displayCirclesOnLayout(GameController gameController, RelativeLayout relLayout) {

        for (Node node : gameController.getNodeList()) {
            ImageView circleImage = node.getNodeImage();
            if (circleImage.getParent() != null) {
                ((ViewGroup) circleImage.getParent()).removeView(circleImage); // <- fix
            }
            relLayout.addView(circleImage);
            circleImage.setX(node.getCenter_x());
            circleImage.setY(node.getCenter_y());
        }
    }

    private int[] getCanvasDimensions(RelativeLayout relLayout) {
        // Get dimensions of relative layout rendered as dimensions of canvas
        // Size of relative layout == Size of canvas by design since the canvas takes up the whole relative layout
        int[] dimensions = new int[2]; // index 0,1 stores x,y values respectively
        dimensions[0] = relLayout.getWidth();
        dimensions[1] = relLayout.getHeight();
        Log.d("CanvasDimensions", String.format("getCanvasDimensions: %d by %d", dimensions[0], dimensions[1]));
        return dimensions;
    }

    public int setConnectTime() {
        int connectTime = 0;
        long elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        connectTime = (int) elapsed / 1000;
        Log.d("ScoreTime", "Connect Time:" + String.valueOf(connectTime));
        return connectTime;
    }

    private int getYOffset(RelativeLayout view) {
        int offset;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        offset = location[1];
        return offset;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY() - this.offset;

        Log.d("Offset", String.valueOf(this.offset));

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.canvasView.startTouch(x, y);
                circleToCheckX = (int) gameController.getNodeList().get(startNode).getNodeImagePosX() + radiusIntForGame;
                circleToCheckY = (int) gameController.getNodeList().get(startNode).getNodeImagePosY() + radiusIntForGame;
                Log.d("CircleCheck", String.valueOf(circleToCheckX) + "," + String.valueOf(circleToCheckY));
                // If touch within then highlight in Yellow to show current starting point
                if ((circleToCheckX - radiusIntForGame <= x && x <= circleToCheckX + radiusIntForGame) &&
                        (circleToCheckY - radiusIntForGame <= y && y <= circleToCheckY + radiusIntForGame)) {
                    gameController.getNodeList().get(startNode).setNodeBackgroundColor(Color.YELLOW);
                    startX = (int) x;
                    startY = (int) y;
                    Log.d("Pos X: ", String.valueOf(x));
                } else {
                    startX = -1;
                    startY = -1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                this.canvasView.moveTouch(x, y);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("previousCx", String.valueOf(startX));
                Log.d("previousCy", String.valueOf(startY));
                nextCircleToCheckX = (int) gameController.getNodeList().get(startNode+1).getNodeImagePosX() + radiusIntForGame;
                nextCircleToCheckY = (int) gameController.getNodeList().get(startNode+1).getNodeImagePosY() + radiusIntForGame;
                if ((nextCircleToCheckX - radiusIntForGame <= x && x <= nextCircleToCheckX + radiusIntForGame) &&
                        (nextCircleToCheckY - radiusIntForGame <= y && y <= nextCircleToCheckY + radiusIntForGame) &&
                        (circleToCheckX - radiusIntForGame <= startX && startX <= circleToCheckX + radiusIntForGame) &&
                        (circleToCheckY - radiusIntForGame <= startY && startY <= circleToCheckY + radiusIntForGame)) {

                    this.canvasView.upTouch(); // Keep path drawn
                    gameController.getNodeList().get(startNode).setNodeBackgroundColor(Color.GREEN); // Current Node
                    gameController.getNodeList().get(startNode+1).setNodeBackgroundColor(Color.GREEN);
                    startNode++; // Increment

                    // Completion Condition Check
                    if (startNode == nodeList.size() - 1) { // Check if reach final node
                        Toast.makeText(ConnectDotsActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                        long connectTime = (long) setConnectTime();
                        Log.d("connectTime", String.valueOf(connectTime));
                        Log.d("connectNode", String.valueOf(nodeList.size()));
                        gameController.calculateConnectScore(connectTime, nodeList.size());
                        intent = new Intent(ConnectDotsActivity.this, GuessImageActivity.class);
                        gameController.dropImageViewsAfterGameEnd();
                        intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, gameController);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else {
                    // Do not record action and revert back the highlights
                    this.canvasView.doNotRecordAction();
                    if (startNode == 0) {
                        gameController.getNodeList().get(startNode).setNodeBackgroundColor(Color.TRANSPARENT);
                    } else {
                        gameController.getNodeList().get(startNode).setNodeBackgroundColor(Color.GREEN);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }

}