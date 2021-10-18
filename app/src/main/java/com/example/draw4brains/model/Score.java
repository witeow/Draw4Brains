package com.example.draw4brains.model;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.draw4brains.view.UserHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Score {

    private String userId;
    private String gameType;


    private ArrayList<String> gameDifficulty;

    private ArrayList<ArrayList<String>> dots;
    private ArrayList<ArrayList<String>> guess;
    private ArrayList<Integer> gamesPlayed;

//    score arranged using list in list, easy/medium/hard

    public Score(){
        ArrayList<String> standardDifficulty = new ArrayList<>();
        standardDifficulty.add("easy");
        standardDifficulty.add("medium");
        standardDifficulty.add("hard");
        this.gameDifficulty = standardDifficulty;
        this.gameType = "connectDots";
    };

    public ArrayList<String> getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(ArrayList<String> gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public ArrayList<ArrayList<String>> getDots() {
        return dots;
    }

    public void setDots(ArrayList<ArrayList<String>> dots) {
        this.dots = dots;
    }

    public ArrayList<ArrayList<String>> getGuess() {
        return guess;
    }

    public void setGuess(ArrayList<ArrayList<String>> guess) {
        this.guess = guess;
    }

    public ArrayList<Integer> getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(ArrayList<Integer> gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }


}
