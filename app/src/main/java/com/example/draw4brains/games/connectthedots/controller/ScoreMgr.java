package com.example.draw4brains.games.connectthedots.controller;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

import java.io.Serializable;


public class ScoreMgr extends AppCompatActivity implements Serializable {
    private int scoreSum = 0;
    private static int scoreConnect;
    private static int scoreGuess;

    public int scoreConnect(long connectTime, int numCircle){
        int averageCircleTime = ((int) connectTime)/numCircle;
        if (averageCircleTime < 1){
            averageCircleTime = 1;
        }
        Log.d("averageT", String.valueOf(averageCircleTime));
        int score1 = (int) ((1.0/averageCircleTime)*50.0);
        scoreConnect = score1;
        scoreSum += score1;
        Log.d("connectScore", String.valueOf(score1));
        Log.d("scoreSum", String.valueOf(scoreSum));

        return scoreSum;
    }

    public int scoreGuess(long guessTime, int guessTrials, String wordToGuess){
        double timePerLetter = 2.0;
        double totalWordTime =timePerLetter*wordToGuess.length();
        int score2 = 0;
        double scoreMatrix = totalWordTime/guessTime;
        if (scoreMatrix>1){
            scoreMatrix = 1;
        }
        if (guessTrials <= 5){
            score2 = (int) ((scoreMatrix) * 25 + 25 - (guessTrials-1) * 5);
        }
        else {
            score2 = (int) ((scoreMatrix) * 25);
            }
        scoreSum += score2;
        scoreGuess = score2;
        Log.d("guessScore", String.valueOf(score2));
        Log.d("scoreSum", String.valueOf(scoreSum));
        return scoreSum;

    }

    public int getScoreSum(){
        return scoreSum;
    }

    public int getDotScore(){
        return scoreConnect;
    }

    public int getGuessScore(){
        return scoreGuess;
    }

}


