package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draw4brains.R;

import java.util.Random;

public class GuessImageActivity extends AppCompatActivity {

    String wordToGuess = "sunny";
    int WORD_LENGTH = wordToGuess.length();

    Button buttonList[] = new Button[14];
    boolean occupied[] = new boolean[WORD_LENGTH];
    String answerArray[] = new String[WORD_LENGTH];



    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_image);

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

        setSelectionButtton(button1, answerTV);
        setSelectionButtton(button2, answerTV);
        setSelectionButtton(button3, answerTV);
        setSelectionButtton(button4, answerTV);
        setSelectionButtton(button5, answerTV);
        setSelectionButtton(button6, answerTV);
        setSelectionButtton(button7, answerTV);
        setSelectionButtton(button8, answerTV);
        setSelectionButtton(button9, answerTV);
        setSelectionButtton(button10, answerTV);
        setSelectionButtton(button11, answerTV);
        setSelectionButtton(button12, answerTV);
        setSelectionButtton(button13, answerTV);
        setSelectionButtton(button14, answerTV);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStates(answerTV);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCorrect = checkAnswer();
                if (isCorrect) {
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Yay! You guessed it", Toast.LENGTH_SHORT).show();
                    resetStates(answerTV);
                } else {
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                    resetStates(answerTV);
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

    private boolean checkAnswer(){
        String userAnswer = "";
        for (String s : answerArray) {
            userAnswer += s;
        }
        return userAnswer.equalsIgnoreCase(wordToGuess);
    }

    private void resetStates(TextView answerTV) {
        for (int i=0; i < WORD_LENGTH; i++) {
            occupied[i] = false;
            answerArray[i] = "_";
        }
        updateAnswerTextView(answerTV);
        for (Button button : buttonList) {
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

    private void setSelectionButtton(Button b, TextView answer_tv) {
        // When button is clicked: 1) Button invisible, 2) Text updated on screen

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = b.getText().toString();
                int result = updateAnswerArray(input, WORD_LENGTH);
                if (result == -1) {
                    Log.d("output", "toasting");
                    Toast.makeText(GuessImageActivity.this, "Cannot add anymore to answer", Toast.LENGTH_SHORT).show();
                    // No change to answer string
                } else {
                    updateAnswerTextView(answer_tv);
                    b.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void updateAnswerTextView(TextView answer_tv){
        String getAnswerString = formatAnswerString(answerArray);
        answer_tv.setText(getAnswerString);
    }

    private int updateAnswerArray(String input, int word_length) {
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