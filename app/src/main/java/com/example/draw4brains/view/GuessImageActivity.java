package com.example.draw4brains.view;

import static com.example.draw4brains.view.GameLevelActivity.gameId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draw4brains.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GuessImageActivity extends AppCompatActivity {

//    String wordToGuess = "sunny";

//    int WORD_LENGTH;
//
//    Button buttonList[];
//    boolean occupied[];
//    String answerArray[];



    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_image);

        // Load imageName from db
        DatabaseReference dotsDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ConnectDots");
        Query query = dotsDb.orderByChild("gameId").equalTo(gameId);
        ValueEventListener getImageName = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String imageName = ds.child("imageName").getValue().toString();
                    Log.d("imageName", imageName);

                    String wordToGuess = imageName;
                    int WORD_LENGTH = wordToGuess.length();
                    Button buttonList[] = new Button[14];
                    boolean occupied[] = new boolean[WORD_LENGTH];
                    String answerArray[] = new String[WORD_LENGTH];

                    for (int i=0; i < WORD_LENGTH; i++) {
                        answerArray[i] = "-";
                        occupied[i]  = false;
                    }
                    String answerString = formatAnswerString(answerArray);
                    TextView answerTV = findViewById(R.id.answer_field);
                    answerTV.setText(answerString);

                    Button button1 = findViewById(R.id.button1);
                    Button button2 = findViewById(R.id.button2);
                    Button button3 = findViewById(R.id.button3);
                    Button button4 = findViewById(R.id.button4);
                    Button button5 = findViewById(R.id.button5);
                    Button button6 = findViewById(R.id.button6);
                    Button button7 = findViewById(R.id.button7);
                    Button button8 = findViewById(R.id.button8);
                    Button button9 = findViewById(R.id.button9);
                    Button button10 = findViewById(R.id.button10);
                    Button button11 = findViewById(R.id.button11);
                    Button button12 = findViewById(R.id.button12);
                    Button button13 = findViewById(R.id.button13);
                    Button button14 = findViewById(R.id.button14);
                    Button resetButton = findViewById(R.id.resetButton);
                    Button submitButton = findViewById(R.id.submitButton);
                    Button giveUp = findViewById(R.id.btn_give_up);


                    buttonList[0] = button1;
                    buttonList[1] = button2;
                    buttonList[2] = button3;
                    buttonList[3] = button4;
                    buttonList[4] = button5;
                    buttonList[5] = button6;
                    buttonList[6] = button7;
                    buttonList[7] = button8;
                    buttonList[8] = button9;
                    buttonList[9] = button10;
                    buttonList[10] = button11;
                    buttonList[11] = button12;
                    buttonList[12] = button13;
                    buttonList[13] = button14;

                    tempSetButtonText(buttonList, wordToGuess); // Temporary for testing. Assign letter to button

                    setSelectionButtton(button1, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button2, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button3, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button4, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button5, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button6, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button7, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button8, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button9, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button10, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button11, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button12, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button13, answerTV, WORD_LENGTH, answerArray, occupied);
                    setSelectionButtton(button14, answerTV, WORD_LENGTH, answerArray, occupied);

                    resetButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetStates(answerTV, WORD_LENGTH, occupied, answerArray, buttonList);
                        }
                    });

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isCorrect = checkAnswer(answerArray, wordToGuess);
                            if (isCorrect) {
                                Log.d("output", "toasting");
                                Toast.makeText(GuessImageActivity.this, "Yay! You guessed it", Toast.LENGTH_SHORT).show();
                                resetStates(answerTV, WORD_LENGTH, occupied, answerArray, buttonList);
                            } else {
                                Log.d("output", "toasting");
                                Toast.makeText(GuessImageActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                                resetStates(answerTV, WORD_LENGTH, occupied, answerArray, buttonList);
                            }
                        }
                    });

                    giveUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent = new Intent(GuessImageActivity.this, EndGameActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(getImageName);

    }

    private boolean checkAnswer(String[] checkAnswerArray ,String guessingWord){
        String userAnswer = "";
        for (String s : checkAnswerArray) {
            userAnswer += s;
        }
        return userAnswer.equalsIgnoreCase(guessingWord);
    }

    private void resetStates(TextView answerTV, Integer wordLength, boolean[] occupiedStates
            , String[] resetAnswerArray, Button[] resetButtonList) {
        for (int i=0; i < wordLength; i++) {
            occupiedStates[i] = false;
            resetAnswerArray[i] = "_";
        }
        updateAnswerTextView(answerTV, resetAnswerArray);
        for (Button button : resetButtonList) {
            button.setVisibility(View.VISIBLE);
        }
    }

    private void tempSetButtonText(Button[] arr, String wordToGuess) {
        int index = 0;
        String letters = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        while (wordToGuess.length()<14){
            wordToGuess += letters.charAt(rand.nextInt(25));
        }
        int[] array = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
        for (int i : array){
            Button button = arr[i];
            button.setText(String.valueOf(wordToGuess.charAt(index)));
            index++;
        }
//        for (char c : wordToGuess.toCharArray()) {
//            Button button = arr[index];
//            button.setText(String.valueOf(c));
//            index++;
//        }
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

    private void setSelectionButtton(Button b, TextView answer_tv, Integer wordLength, String[] strAnswerArray, boolean[] occupied) {
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

    private void updateAnswerTextView(TextView answer_tv, String[] strAnswerArray){
        String getAnswerString = formatAnswerString(strAnswerArray);
        answer_tv.setText(getAnswerString);
    }

    private int updateAnswerArray(String input, int word_length, boolean[] occupied, String[] answerArray) {
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
}