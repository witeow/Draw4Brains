package com.example.draw4brains.games.connectthedots.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.draw4brains.R;
import com.example.draw4brains.games.connectthedots.controller.ConnectDotDatabaseMgr;
import com.example.draw4brains.games.connectthedots.controller.GameMgr;
import com.example.draw4brains.games.connectthedots.model.ConnectDots;
import com.example.draw4brains.games.connectthedots.model.Constants;
import com.example.draw4brains.main.view.ForgetPasswordActivity;
import com.example.draw4brains.main.view.LoginActivity;
import com.example.draw4brains.main.view.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Random;

public class GuessImageActivity extends AppCompatActivity implements View.OnClickListener {


    private Chronometer chronometer;

    Intent intent;
    ImageView guessImage;
    GameMgr gameMgr;
    TextView answerTV;
    Button resetButton;
    Button submitButton;
    Button giveUp;
    ConnectDotDatabaseMgr connectDotDatabaseMgr;
    Button buttonList[] = new Button[14];

    private int guessTrial;
    private int WORD_LENGTH;
    private String wordToGuess;
    private boolean occupied[];
    private String answerArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_image);

        // Link to relevant controller
        connectDotDatabaseMgr = new ConnectDotDatabaseMgr();

        chronometer = findViewById(R.id.chronometer);
        guessImage = findViewById(R.id.guess_image);
        guessTrial = 1;

        resetButton = findViewById(R.id.resetButton);
        submitButton = findViewById(R.id.submitButton);
        giveUp = findViewById(R.id.btn_give_up);

        // Set listener
        resetButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        giveUp.setOnClickListener(this);

        intent = getIntent();
        gameMgr = (GameMgr) intent.getSerializableExtra(Constants.INTENT_KEY_GAME_MANAGER);


        Log.d("SelectImage", "Enter Function");
        Log.d("StringRef", gameMgr.getConnectDotsLevelObject().getStorageStringRef());

        connectDotDatabaseMgr.loadImageFromDatabase(gameMgr, new ConnectDotDatabaseMgr.onCompleteGetImageURL() {
            @Override
            public void onComplete(Uri url) {
                Glide.with(getApplicationContext())
                        .load(url)
                        .fitCenter()
                        .into(guessImage); // Load into imageView
                wordToGuess = gameMgr.getConnectDotsLevelObject().getImageName();
                WORD_LENGTH = wordToGuess.length();

                occupied = new boolean[WORD_LENGTH];
                answerArray = new String[WORD_LENGTH];
                for (int i = 0; i < WORD_LENGTH; i++) {
                    answerArray[i] = "_";
                    occupied[i] = false;
                }

                String answerString = formatAnswerString(answerArray);
                answerTV = findViewById(R.id.answer_field);
                answerTV.setText(answerString);

                buttonList[0] = findViewById(R.id.button1);
                buttonList[1] = findViewById(R.id.button2);
                buttonList[2] = findViewById(R.id.button3);
                buttonList[3] = findViewById(R.id.button4);
                buttonList[4] = findViewById(R.id.button5);
                buttonList[5] = findViewById(R.id.button6);
                buttonList[6] = findViewById(R.id.button7);
                buttonList[7] = findViewById(R.id.button8);
                buttonList[8] = findViewById(R.id.button9);
                buttonList[9] = findViewById(R.id.button10);
                buttonList[10] = findViewById(R.id.button11);
                buttonList[11] = findViewById(R.id.button12);
                buttonList[12] = findViewById(R.id.button13);
                buttonList[13] = findViewById(R.id.button14);

                shuffleWords(buttonList, wordToGuess); // Temporary for testing. Assign letter to button

                for (int buttonNum = 0; buttonNum < buttonList.length; buttonNum++) {
                    setSelectionButtton(buttonList[buttonNum], answerTV, WORD_LENGTH, answerArray, occupied);
                }
                // Start timer
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
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
            case R.id.resetButton:
                resetStates(answerTV, WORD_LENGTH, occupied, answerArray, buttonList);
                break;
            case R.id.submitButton:
                boolean isCorrect = checkAnswer(answerArray, wordToGuess);
                if (isCorrect) {
                    long guessTime = (long) setGuessTime();
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Yay! You guessed it", Toast.LENGTH_SHORT).show();
                    Log.d("guessTime", String.valueOf(guessTime));
                    Log.d("guessTrial", String.valueOf(guessTrial));
                    gameMgr.calculateGuessScore(guessTime, guessTrial, wordToGuess);
                    Log.d("output", "toasting");
                    intent = new Intent(GuessImageActivity.this, EndGameActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, gameMgr);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                    guessTrial += 1;
                    resetStates(answerTV, WORD_LENGTH, occupied, answerArray, buttonList);
                }
                break;
            case R.id.btn_give_up:
                gameMgr.calculateGuessScore(99999999, 6, wordToGuess);
                intent = new Intent(GuessImageActivity.this, EndGameActivity.class);
                intent.putExtra(Constants.INTENT_KEY_GAME_MANAGER, gameMgr);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }


    public int setGuessTime() {
        int guessTime = 0;
        long elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        guessTime = (int) elapsed / 1000;
        return guessTime;
    }

    private boolean checkAnswer(String[] checkAnswerArray, String guessingWord) {
        String userAnswer = "";
        for (String s : checkAnswerArray) {
            userAnswer += s;
        }
        return userAnswer.equalsIgnoreCase(guessingWord);
    }

    private void resetStates(TextView answerTV, Integer wordLength, boolean[] occupiedStates
            , String[] resetAnswerArray, Button[] resetButtonList) {
        for (int i = 0; i < wordLength; i++) {
            occupiedStates[i] = false;
            resetAnswerArray[i] = "_";
        }
        updateAnswerTextView(answerTV, resetAnswerArray);
        for (Button button : resetButtonList) {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void shuffleWords(Button[] arr, String wordToGuess) {
        int index = 0;
        String letters = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        while (wordToGuess.length() < 14) {
            wordToGuess += letters.charAt(rand.nextInt(25));
        }
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        for (int i : array) {
            Button button = arr[i];
            button.setText(String.valueOf(wordToGuess.charAt(index)));
            index++;
        }
    }

    private String formatAnswerString(String[] arr) {
        String separator = " "; // Spacing
        String formattedString = "";

        for (String s : arr) {
            formattedString += s;
            formattedString += separator;
        }
        return formattedString;
    }

    private void setSelectionButtton(Button b, TextView answer_tv, Integer wordLength, String[]
            strAnswerArray, boolean[] occupied) {
        // When button is clicked: 1) Button invisible, 2) Text updated on screen

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = b.getText().toString();
                int result = updateAnswerArray(input, wordLength, occupied, strAnswerArray);
                if (result == -1) {
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Cannot add anymore to answer", Toast.LENGTH_SHORT).show();
                    // No change to answer string
                } else {
                    updateAnswerTextView(answer_tv, strAnswerArray);
                    b.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateAnswerTextView(TextView answer_tv, String[] strAnswerArray) {
        String getAnswerString = formatAnswerString(strAnswerArray);
        answer_tv.setText(getAnswerString);
    }

    private int updateAnswerArray(String input, int word_length, boolean[] occupied, String[]
            answerArray) {
        int indexToInsert = -1;
        for (int i = 0; i < word_length; i++) {
            if (occupied[i]) {
                continue;
            } else {
                indexToInsert = i;
                break;
            }
        }

        if (indexToInsert != -1) {
            answerArray[indexToInsert] = input;
            occupied[indexToInsert] = true;
        }
        return indexToInsert;
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }
}