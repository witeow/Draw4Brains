package com.example.draw4brains.games.connectthedots.object;

import java.io.Serializable;

public class Level implements Serializable {

    private String gameDifficulty;
    private String gameType;
    private String gameGuessAnswer;
    private String gameName; // Game name = Guess Answer

    public Level(String gameName, String gameDifficulty, String gameType) {
        this.gameName = gameName;
        this.gameDifficulty = gameDifficulty;
        this.gameType = gameType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(String gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getGameGuessAnswer() {
        return gameGuessAnswer;
    }

    public void setGameGuessAnswer(String gameGuessAnswer) {
        this.gameGuessAnswer = gameGuessAnswer;
    }
}
