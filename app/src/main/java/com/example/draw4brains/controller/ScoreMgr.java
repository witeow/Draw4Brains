package com.example.draw4brains.controller;

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
        int score1 = (1/averageCircleTime)*50;
        scoreConnect = score1;
        scoreSum += score1;
        Log.d("connectScore", String.valueOf(score1));
        Log.d("scoreSum", String.valueOf(scoreSum));

        return scoreSum;
    }

    public int scoreGuess(long guessTime, int guessTrials){
        int score2 = 0;
        if (guessTrials <= 5){
            score2 = (1/((int)guessTime)) * 25 + 25 - (guessTrials-1) * 5;
        }
        else {
            score2 = (1/((int)guessTime)) * 25;
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


