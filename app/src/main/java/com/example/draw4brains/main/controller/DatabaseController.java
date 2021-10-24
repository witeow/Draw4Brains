package com.example.draw4brains.main.controller;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.draw4brains.games.connectthedots.controller.GameDatabaseController;
import com.example.draw4brains.games.connectthedots.object.Score;
import com.example.draw4brains.main.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseController {

    GameDatabaseController gameDatabaseController = new GameDatabaseController();

    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface callbackUserScoreForAdmin {
        void onCallback(Score score, ArrayList<User> usersList, int position);
    }

    public void getUserScoreForList(Activity usersListActivity, ListView lvUsers, callbackUserScoreForAdmin onCallbackUserScoreForAdmin){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        DatabaseReference usersdRef = rootRef.child("User");
        ArrayList<User> usersList=new ArrayList<User>();

        String admin_email = fAuth.getCurrentUser().getEmail();
        Log.d("Admin's uidddddddddd", admin_email);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String child_uid = ds.getKey();
                    Log.d("TAG", child_uid);
                    String email=ds.child("userEmail").getValue(String.class);
                    String name=ds.child("userName").getValue(String.class);
                    String score = ds.child("userScore").getValue(String.class);

                    int number_played = Integer.parseInt(ds.child("userNumGamesPlayed").getValue(String.class));

                    String played = (ds.child("userNumGamesPlayed").getValue(String.class));
                    Log.d("Played", played);

                    //String caretaker_email = "wlim095@e.ntu.edu.sg";
                    String caretaker_email=ds.child("userAdmin").getValue(String.class);
                    Log.d("UserList", "AdminEmail" + admin_email+" ,CareTaker Email" + caretaker_email);


                    if (admin_email.equalsIgnoreCase(caretaker_email)) {
                        usersList.add(new User(name, "gender", child_uid, email, caretaker_email, false, score,number_played));
                    }

                }

                Log.d("UserList", usersList.toString());


                AdminController mAdminController = new AdminController(usersListActivity,usersList);
                lvUsers.setAdapter(mAdminController);
                lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Query query = rootRef.child("Score").orderByKey().equalTo(usersList.get(position).getPhoneNo());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.d("score", "Accessing Firebase");
                                Log.d("score", snapshot.toString());
                                if (snapshot.exists()) {
                                    Score userScore = new Score();
                                    ArrayList<ArrayList<String>> dotsScore = new ArrayList<>();
                                    ArrayList<ArrayList<String>> guessScore = new ArrayList<>();
                                    ArrayList<Integer> gamesPlayed = new ArrayList<>();

                                    userScore.setUserId(usersList.get(position).getUserID());
                                    Log.d("score", "Retrieving snapshot");
                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        Log.d("score ds", ds.toString());
                                        for (int difficultyLevel = 0; difficultyLevel < userScore.getGameDifficulty().size(); difficultyLevel++) {
                                            String dots = ds.child(userScore.getGameType()).child(userScore.getGameDifficulty().get(difficultyLevel))
                                                    .child("dots").getValue()
                                                    .toString();

                                            Log.d("score dots", dots);
                                            String guess = ds.child(userScore.getGameType()).child(userScore.getGameDifficulty().get(difficultyLevel))
                                                    .child("guess").getValue()
                                                    .toString();

                                            Log.d("score guess", guess);
                                            Log.d("score numPlayed", ds.child(userScore.getGameType()).child(userScore.getGameDifficulty().get(difficultyLevel))
                                                    .child("guess").getValue()
                                                    .toString());
                                            Integer numPlayed = Integer.parseInt(ds.child(userScore.getGameType()).child(userScore.getGameDifficulty().get(difficultyLevel))
                                                    .child("gamesPlayed").getValue()
                                                    .toString());


                                            ArrayList<String> dotsArr = new ArrayList<>(Arrays.asList(TextUtils.split(dots, ",")));
                                            ArrayList<String> guessArr = new ArrayList<>(Arrays.asList(TextUtils.split(guess, ",")));

                                            dotsScore.add(dotsArr);
                                            guessScore.add(guessArr);
                                            gamesPlayed.add(numPlayed);
                                        }

                                        userScore.setDots(dotsScore);
                                        userScore.setGuess(guessScore);
                                        userScore.setGamesPlayed(gamesPlayed);

                                        Log.d("dotsScore", dotsScore.toString());
                                        Log.d("score", "Score object has been updated");
                                        onCallbackUserScoreForAdmin.onCallback(userScore, usersList, position);
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
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);

    }

}
