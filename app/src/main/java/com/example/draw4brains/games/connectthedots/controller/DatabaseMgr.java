package com.example.draw4brains.games.connectthedots.controller;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.draw4brains.games.connectthedots.model.Score;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseMgr {

    public DatabaseMgr(){};


    // Interfaces
    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface onCallBackUserScore {
        void onCallback(Score score);
    }



    public void getScoreFromDatabase(String userId, onCallBackUserScore callBackUserScore) {
        ArrayList<ArrayList<String>> dotsScore = new ArrayList<>();
        ArrayList<ArrayList<String>> guessScore = new ArrayList<>();
        ArrayList<Integer> gamesPlayed = new ArrayList<>();

        Score userScore = new Score();
        userScore.setUserId(userId);

        FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference userRef = userDb.getReference("Score").child(userId);
        DatabaseReference userRef = userDb.getReference("Score").child(userId);
        Log.d("score", userId);
        Log.d("score", userScore.getGameType());
        Log.d("score key", userRef.getKey());
        Query query = userRef.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("score", "Accessing Firebase");
                Log.d("score", snapshot.toString());
                if (snapshot.exists()) {
                    Log.d("score", "Retrieving snapshot");
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d("score ds", ds.toString());
                        for (int difficultyLevel = 0; difficultyLevel < userScore.getGameDifficulty().size(); difficultyLevel++) {
                            String dots = ds.child(userScore.getGameDifficulty().get(difficultyLevel))
                                    .child("dots").getValue()
                                    .toString();

                            Log.d("score dots", dots);
                            String guess = ds.child(userScore.getGameDifficulty().get(difficultyLevel))
                                    .child("guess").getValue()
                                    .toString();

                            Log.d("score guess", guess);
                            Log.d("score numPlayed", ds.child(userScore.getGameDifficulty().get(difficultyLevel))
                                    .child("guess").getValue()
                                    .toString());
                            Integer numPlayed = Integer.parseInt(ds.child(userScore.getGameDifficulty().get(difficultyLevel))
                                    .child("gamesPlayed").getValue()
                                    .toString());


                            ArrayList<String> dotsArr = new ArrayList<>(Arrays.asList(TextUtils.split(dots, ",")));
                            ArrayList<String> guessArr = new ArrayList<>(Arrays.asList(TextUtils.split(guess, ",")));
//                            ArrayList<String> guessArr = TextUtils.split(guess, ",");


//                            dotsScore[difficultyLevel] = dotsArr;
//                            guessScore[difficultyLevel] = guessArr;
//                            gamesPlayed[difficultyLevel] = numPlayed;

                            dotsScore.add(dotsArr);
                            guessScore.add(guessArr);
                            gamesPlayed.add(numPlayed);
                        }

                        userScore.setDots(dotsScore);
                        userScore.setGuess(guessScore);
                        userScore.setGamesPlayed(gamesPlayed);

                        Log.d("dotsScore", dotsScore.toString());
                        Log.d("score", "Score object has been updated");
                        callBackUserScore.onCallback(userScore);
                    }
                } else {
                    Log.d("score", "Snapshot doesnt not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("score", error.toString());
            }
        });

    }
}
