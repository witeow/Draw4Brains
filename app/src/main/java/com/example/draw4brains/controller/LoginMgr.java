package com.example.draw4brains.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.draw4brains.model.Admin;
import com.example.draw4brains.model.Score;
import com.example.draw4brains.model.User;
import com.example.draw4brains.view.AdminHomeActivity;
import com.example.draw4brains.view.LoginActivity;
import com.example.draw4brains.view.UserHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginMgr {

    public static User currentUser;
    public static Admin currentAdmin;
    private Intent intent;
    private FirebaseAuth auth;

    public LoginMgr(){
        auth = FirebaseAuth.getInstance();
    }

    public void login(Activity loginActivity, String email, String password, boolean isAdmin) {
        String str_email = email.trim();
        String str_pass = password.trim();
        Log.d("Debug email:",str_email);
        Log.d("Debug pass:",str_pass);
        if (isAdmin) {
            auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        DatabaseReference userRef = FirebaseDatabase
                                .getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("Admin");
                        Query query = userRef.orderByChild("adminEmail").equalTo(str_email);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    currentAdmin = new Admin();
                                    adminLogin(loginActivity, str_email);
                                } else {
                                    Log.d("LoginDEBUG", "Account Type not specified!");
                                    Toast.makeText(loginActivity, "Account Type not specified!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(loginActivity, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }else{ // User portion
            try {
                str_pass = AESCrypt.encrypt(str_pass);
                Log.d("Debug encrypt pass:",str_pass);

                auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            DatabaseReference userRef = FirebaseDatabase
                                    .getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("User");
                            Query query = userRef.orderByChild("userEmail").equalTo(str_email);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        currentUser = new User();
                                        userLogin(loginActivity, str_email);
                                    } else {
                                        Log.d("LoginDEBUG", "Account Type not specified!");
                                        Toast.makeText(loginActivity, "Account Type not specified!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(loginActivity, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void adminLogin(Activity loginActivity, String email){
        Toast.makeText(loginActivity, "Login Successful!", Toast.LENGTH_SHORT).show();
        intent = new Intent(loginActivity.getApplicationContext(), AdminHomeActivity.class);
        intent.putExtra("isAdmin",true);
        Log.d("AdminDEBUG", "Accessing Firebase");
        FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = userDb.getReference("Admin");
        Query query = userRef.orderByChild("adminEmail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", snapshot.getKey());
                Log.d("Snapshot Children", snapshot.getChildren().toString());
                Log.d("Snapshot String", snapshot.toString());
                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String adminUid = ds.getKey();
                        currentAdmin.setAdminId(adminUid);
//                        Log.d("AdminUID", adminUid);
//                        Log.d("Snapshot", ds.toString());
//                        Log.d("Snapshot", ds.child("adminName").getValue().toString());
//                        Log.d("Snapshot", ds.child(adminUid).child("adminMobile").getValue().toString());
//                        Log.d("Snapshot", ds.child(adminUid).child("adminPassword").getValue().toString());
//                        Log.d("Snapshot", ds.child(adminUid).child("arrayUserId").getValue().toString());
                        currentAdmin.setAdminName(ds.child("adminName").getValue().toString());
                        currentAdmin.setPhoneNo(ds.child("adminMobile").getValue().toString());
                        currentAdmin.setEmailAddress(email);;
                        currentAdmin.setAdminPass(ds.child("adminPassword").getValue().toString());
                        String dbArrayUser = ds.child("arrayUserId").getValue().toString();
                        String[] arrayUserId = TextUtils.split(dbArrayUser, ",");
                        currentAdmin.setAdminUserId(arrayUserId);

                    }
                    Log.d("AdminDEBUG", "Admin has logged in!");
                    intent = new Intent(loginActivity.getApplicationContext(), AdminHomeActivity.class);
                    intent.putExtra("isAdmin", true);
                    loginActivity.startActivity(intent);
                }else{
                    Toast.makeText(loginActivity, "No User found!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(loginActivity, "Database could not be accessed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void userLogin(Activity loginActivity, String email){
        final ProgressDialog progressDialog = new ProgressDialog(loginActivity);
        progressDialog.setTitle("Logging in");
        Toast.makeText(loginActivity, "Login Successful!", Toast.LENGTH_SHORT).show();
        Log.d("UserDEBUG", "Accessing Firebase");
        FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = userDb.getReference("User");
        Query query = userRef.orderByChild("userEmail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("TAG", snapshot.getKey());
//                Log.d("Snapshot Children", snapshot.getChildren().toString());
//                Log.d("Snapshot String", snapshot.toString());
                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        String userUid = ds.getKey();
                        currentUser.setAddress(snapshot.child(userUid).child("userAddress").getValue().toString());
                        currentUser.setCaretaker_email(snapshot.child(userUid).child("userAdmin").getValue().toString());
                        if (TextUtils.isEmpty(currentUser.getCaretaker_email())){
                            currentUser.setIs_admin(false);
                        }
                        else{
                            currentUser.setIs_admin(true);
                        }
                        currentUser.setBirthday(snapshot.child(userUid).child("userBirthday").getValue().toString());
                        currentUser.setEmailAddress(snapshot.child(userUid).child("userEmail").getValue().toString());
                        currentUser.setGender(snapshot.child(userUid).child("userGender").getValue().toString());
                        currentUser.setHouseNo(snapshot.child(userUid).child("userHouseNum").getValue().toString());
                        currentUser.setNokName(snapshot.child(userUid).child("userNokName").getValue().toString());
                        currentUser.setNokNum(snapshot.child(userUid).child("userNokNum").getValue().toString());
                        currentUser.setNumber_played(Integer.parseInt(snapshot.child(userUid).child("userNumGamesPlayed").getValue().toString()));
                        currentUser.setPhoneNo(snapshot.child(userUid).child("userPhoneNum").getValue().toString());
                        currentUser.setscore(Integer.parseInt(snapshot.child(userUid).child("userScore").getValue().toString()));
                        currentUser.setUserID(userUid);
                        currentUser.setUserPassword(snapshot.child(userUid).child("userPassword").getValue().toString());
                        currentUser.setTotalScore(currentUser.getscore(), currentUser.getNumber_played());
                        Log.d("userName",snapshot.child(userUid).child("userName").getValue().toString() );
                        currentUser.setUserName(snapshot.child(userUid).child("userName").getValue().toString());

//                        currentUser.retrieveScore(userUid);
                    }
//                    Log.d("UserDEBUG", "User has logged in!");
//                    Log.d("UserDEBUG", currentUser.getAddress());
                    DatabaseMgr databaseMgr = new DatabaseMgr();
                    databaseMgr.getScoreFromDatabase(currentUser.getUserID(), new DatabaseMgr.onCallBackUserScore() {
                        @Override
                        public void onCallback(Score score) {
                            currentUser.setScore(score);
                            // Login after getting score.
                            intent = new Intent(loginActivity.getApplicationContext(), UserHomeActivity.class);
                            intent.putExtra("isAdmin", false);
                            loginActivity.startActivity(intent);
                        }
                    });



                }else{
                    Toast.makeText(loginActivity, "No User found!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(loginActivity, "Database could not be accessed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
