package com.example.draw4brains.games.connectthedots.controller;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.draw4brains.games.connectthedots.model.Node;
import com.example.draw4brains.games.connectthedots.model.Score;
import com.example.draw4brains.games.connectthedots.view.GameMenuActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseMgr {

    public DatabaseMgr() {
    }

    ;


    // Interfaces

    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface onCallBackUserScore {
        void onCallback(Score score);
    }

    public interface onCompleteDataLoad {
        void onComplete(ArrayList<Node> preprocessedNodes, String[] cordString, int gameLevel);
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


    public void loadNodeData(GameMgr currentGameManagerInstance, onCompleteDataLoad completeDataLoad) {
        Log.d("LoadNode", "Inside function loadNodeData-DatabaseMgr");
        ArrayList<Node> preprocessedArray = new ArrayList<Node>();
        DatabaseReference dotsDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ConnectDots");
        String gameName = currentGameManagerInstance.getLevelInfo().getGameName();
        Log.d("LoadNode", "Game Name: " + gameName);
        Query query = dotsDb.orderByChild("imageName").equalTo(currentGameManagerInstance.getLevelInfo().getGameName());
        ValueEventListener newTest = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TEST", "Enter Test");
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String gameId = ds.getKey();
                    String dotsArray = ds.child("arrayDotsPosition").getValue().toString();
//                    String imageId = ds.child("imageId").getValue().toString();
                    String imageName = ds.child("imageName").getValue().toString();
                    Integer gameLevel = Integer.parseInt(ds.child("level").getValue().toString());
                    Log.d("DEBUG", gameId);
                    Log.d("DEBUG", ds.getValue().toString());
                    Log.d("dotsArray", dotsArray);
//                    Log.d("imageId", imageId);
                    Log.d("imageName", imageName);

                    dotsArray = dotsArray.replace("[", "").replace("]", "").replace("x", "")
                            .replace("y", "").replace("=", "")
                            .replace("{", "").replace("}", "")
                            .replaceAll("\\s", "");

                    String[] cordString = TextUtils.split(dotsArray, ",");
                    ArrayList<Integer> xCord = new ArrayList<Integer>();
                    ArrayList<Integer> yCord = new ArrayList<Integer>();
                    for (int coord = 0; coord < cordString.length; coord++) {
//                        Log.d("cordString", cordString[coord].toString());
                        int intCoord = Integer.parseInt(cordString[coord]);
                        if (coord % 2 == 0) {
                            xCord.add(intCoord);
                        } else {
                            yCord.add(intCoord);
                        }
                    }

                    Log.d("xCord", xCord.toString());
                    Log.d("yCord", yCord.toString());

                    for (int node_int = 0; node_int < xCord.size(); node_int++) {
                        String node_num = Integer.toString(node_int + 1);
                        Node node = new Node(node_num, xCord.get(node_int), yCord.get(node_int));
                        Log.d("Node Num", (node_num));
                        Log.d("xCord", xCord.get(node_int).toString());
                        Log.d("yCord", yCord.get(node_int).toString());
                        preprocessedArray.add(node);
                    }

                    Log.d("ArrayNode", preprocessedArray.toString());
                    completeDataLoad.onComplete(preprocessedArray, cordString, gameLevel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DatabaseError", "Error in database level load");
            }

        };
        query.addListenerForSingleValueEvent(newTest);
    }
}
