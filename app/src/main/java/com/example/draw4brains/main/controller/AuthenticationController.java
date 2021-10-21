package com.example.draw4brains.main.controller;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.draw4brains.main.model.Admin;
import com.example.draw4brains.main.model.User;
import com.example.draw4brains.main.view.AdminHomeActivity;
import com.example.draw4brains.main.view.UserHomeActivity;
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

public class AuthenticationController {

    private User currentUser;
    private Admin currentAdmin;
    private boolean isCurrentlyAdmin;
    private Intent intent;
    private FirebaseAuth auth;

    public AuthenticationController() {
        auth = FirebaseAuth.getInstance();
    }

    public boolean isCurrentlyAdmin() {
        return this.isCurrentlyAdmin;
    }

    public void setCurrentlyAdmin(boolean currentlyAdmin) {
        isCurrentlyAdmin = currentlyAdmin;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public void signInIfAccountExist(Activity loginActivity) {
        if (auth.getCurrentUser() != null) {
            String firebaseUserEmail = auth.getCurrentUser().getEmail();
            String userEmail = currentUser != null ? currentUser.getEmailAddress() : null;
            String adminEmail = currentAdmin != null ? currentAdmin.getEmailAddress() : null;
            if (firebaseUserEmail.equalsIgnoreCase(userEmail)) {
                loginActivity.startActivity(new Intent(loginActivity, UserHomeActivity.class));
                loginActivity.finish();
            } else if (firebaseUserEmail.equalsIgnoreCase(adminEmail)) {
                loginActivity.startActivity(new Intent(loginActivity, AdminHomeActivity.class));
                loginActivity.finish();
            }
        }
    }

    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface callBackOnLoginAttempt {
        void onSuccess(String message, boolean isAdmin);

        void onFailure(String message);
    }

    public void login(String email, String password, boolean isAdmin, callBackOnLoginAttempt callBackOnLoginAttempt) {
        String str_email = email.trim();
        String str_pass = password.trim();
        Log.d("Debug email:", str_email);
        Log.d("Debug pass:", str_pass);
        if (isAdmin) {
            auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DatabaseReference userRef = FirebaseDatabase
                                .getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("Admin");
                        Query query = userRef.orderByChild("adminEmail").equalTo(str_email);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    currentAdmin = new Admin();
                                    adminLogin(str_email, callBackOnLoginAttempt);
                                } else {
                                    Log.d("LoginDEBUG", "Account Type not specified!");
                                    callBackOnLoginAttempt.onFailure("Account type not specified!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callBackOnLoginAttempt.onFailure("Database error!");
                            }
                        });
                    } else {
                        callBackOnLoginAttempt.onFailure("Sign in failed!");
                    }
                }
            });
        } else { // User portion
            try {
                str_pass = AESCrypt.encrypt(str_pass);
                Log.d("Debug encrypt pass:", str_pass);

                auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference userRef = FirebaseDatabase
                                    .getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                    .getReference("User");
                            Query query = userRef.orderByChild("userEmail").equalTo(str_email);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        currentUser = new User();
                                        userLogin(str_email, callBackOnLoginAttempt);
                                    } else {
                                        Log.d("LoginDEBUG", "Account Type not specified!");
                                        callBackOnLoginAttempt.onFailure("Account type not specified!");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    callBackOnLoginAttempt.onFailure("Database error!");
                                }
                            });
                        } else {
                            callBackOnLoginAttempt.onFailure("Sign in failed!");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void adminLogin(String email, callBackOnLoginAttempt callBackOnLoginAttempt) {
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
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
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
                        currentAdmin.setEmailAddress(email);
                        currentAdmin.setAdminPass(ds.child("adminPassword").getValue().toString());
                        String dbArrayUser = ds.child("arrayUserId").getValue().toString();
                        String[] arrayUserId = TextUtils.split(dbArrayUser, ",");
                        currentAdmin.setAdminUserId(arrayUserId);

                    }
                    setCurrentlyAdmin(true);
                    callBackOnLoginAttempt.onSuccess("Successful Admin Login!", true);
                } else {
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                    callBackOnLoginAttempt.onFailure("No user found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBackOnLoginAttempt.onFailure("Database error!");
            }
        });
    }

    private void userLogin(String email, callBackOnLoginAttempt callBackOnLoginAttempt) {
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
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String userUid = ds.getKey();
                        currentUser.setAddress(snapshot.child(userUid).child("userAddress").getValue().toString());
                        currentUser.setCaretaker_email(snapshot.child(userUid).child("userAdmin").getValue().toString());
                        if (TextUtils.isEmpty(currentUser.getCaretaker_email())) {
                            currentUser.setIs_admin(false);
                        } else {
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
                        Log.d("userName", snapshot.child(userUid).child("userName").getValue().toString());
                        currentUser.setUserName(snapshot.child(userUid).child("userName").getValue().toString());
                    }
                    setCurrentlyAdmin(false);
                    callBackOnLoginAttempt.onSuccess("User Login Successful!", false);
                } else {
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                    callBackOnLoginAttempt.onFailure("No user found!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBackOnLoginAttempt.onFailure("Database error!");
            }
        });
    }

    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface callBackOnLogout {
        void onComplete();
    }

    public void logout(callBackOnLogout onLogout) {
        auth.signOut();
        onLogout.onComplete();
    }

}
