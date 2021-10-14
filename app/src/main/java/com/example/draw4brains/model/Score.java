package com.example.draw4brains.model;

import java.util.ArrayList;

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
