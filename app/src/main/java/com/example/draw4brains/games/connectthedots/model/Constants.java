package com.example.draw4brains.games.connectthedots.model;

public final class Constants {

    // Intent Passing Constants
    public static final String INTENT_KEY_GAME_MANAGER = "GAME_MANAGER";

    // Node Scaling Constant
    public static final float DIAMETER_LIMIT_MIN = 70f;
    public static final float DIAMETER_CALCULATION_TOLERANCE_FACTOR = 1.5f;
    public static final int PERCENTAGE_FILL_REQUIRED = 80; // To check if the nodes are spaced big enough
    public static final float SCALING_FACTOR = 1f;
    public static final float SCALING_FACTOR_MODIFIER = 1.5f;

    // Connect-dot-Game Constants
    public static final float MAX_SCORE_CONNECT_DOT = 50f;
    public static final float MAX_SCORE_GUESS = 50f;
    public static final int NO_OF_GUESSES_ALLOWED = 5;
    public static final double AVERAGE_TIME_PER_LETTER = 2.0;

    // Firebase URLs
    public static final String FIREBASE_STORAGE_URL = "gs://draw4brains.appspot.com/";
}
