package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.AESCrypt;
import com.example.draw4brains.model.Admin;
import com.example.draw4brains.model.Score;
import com.example.draw4brains.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    //EditText firstname;
    //EditText lastname;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;
    private FirebaseAuth auth;
    public static User currentUser;
    public static Score userScore;
    public static Admin currentAdmin;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        email =findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPW = findViewById(R.id.btn_forgot_pw);
        btnRegister = findViewById(R.id.btn_register);
        btnAccType = findViewById(R.id.btn_acc_type);
        btnAccType.setChecked(false);

        // For easy logging in
        email.setText("witeow223@gmail.com");
        password.setText("Witeow1!");

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString();
                String str_pass = password.getText().toString();
                Log.d("Debug email:",str_email);
                Log.d("Debug pass:",str_pass);
                if (btnAccType.isChecked()) {
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
                                            adminLogin(str_email);
                                        } else {
                                            Log.d("LoginDEBUG", "Account Type not specified!");
                                            Toast.makeText(LoginActivity.this, "Account Type not specified!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }else{
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
                                                userLogin(str_email);
                                            } else {
                                                Log.d("LoginDEBUG", "Account Type not specified!");
                                                Toast.makeText(LoginActivity.this, "Account Type not specified!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
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
                                            userLogin(str_email);
                                        } else {
                                            Log.d("LoginDEBUG", "Account Type not specified!");
                                            Toast.makeText(LoginActivity.this, "Account Type not specified!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        btnForgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }

    private void userLogin(String email){
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
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
                        currentUser.setUserName(email);
                        currentUser.setUserPassword(snapshot.child(userUid).child("userPassword").getValue().toString());
                        currentUser.setTotalScore(currentUser.getscore(), currentUser.getNumber_played());

                        retrieveScore(userUid);
                    }
//                    Log.d("UserDEBUG", "User has logged in!");
//                    Log.d("UserDEBUG", currentUser.getAddress());
//                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this, "No User found!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database could not be accessed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adminLogin(String email){
        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
        intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
        intent.putExtra("isAdmin",true);
        Log.d("AdminDEBUG", "Accessing Firebase");
        FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = userDb.getReference("Admin");
        Query query = userRef.orderByChild("adminEmail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("TAG", snapshot.getKey());
//                Log.d("Snapshot Children", snapshot.getChildren().toString());
//                Log.d("Snapshot String", snapshot.toString());
                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String adminUid = ds.getKey();
                        currentAdmin.setAdminId(adminUid);
                        currentAdmin.setAdminName(ds.child(adminUid).child("adminName").toString());
                        currentAdmin.setPhoneNo(ds.child(adminUid).child("adminMobile").toString());
                        currentAdmin.setEmailAddress(email);;
                        currentAdmin.setAdminPass(ds.child(adminUid).child("adminPassword").toString());
                        String dbArrayUser = ds.child(adminUid).child("arrayUserId").toString();
                        String[] arrayUserId = TextUtils.split(dbArrayUser, ",");
                        currentAdmin.setAdminUserId(arrayUserId);

                    }
                    Log.d("AdminDEBUG", "Admin has logged in!");
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "No User found!", Toast.LENGTH_SHORT).show();
                    Log.d("LoginDEBUG", "Task Unsuccessful!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database could not be accessed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveScore(String userId){
        intent = new Intent(getApplicationContext(), UserHomeActivity.class);
        intent.putExtra("isAdmin",false);

        ArrayList<ArrayList<String>> dotsScore = new ArrayList<>();
        ArrayList<ArrayList<String>> guessScore = new ArrayList<>();
        ArrayList<Integer> gamesPlayed = new ArrayList<>();

        userScore = new Score();

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
                if (snapshot.exists()){
                    Log.d("score", "Retrieving snapshot");
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Log.d("score ds", ds.toString());
                        for(int difficultyLevel = 0; difficultyLevel<userScore.getGameDifficulty().size(); difficultyLevel++){
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

                        Log.d("UserDEBUG", "User has logged in!");
                        Log.d("UserDEBUG", currentUser.getAddress());
                        startActivity(intent);
                    }
                } else{
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
