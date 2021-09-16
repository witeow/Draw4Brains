package com.example.draw4brains;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class firebaseTestWT extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private EditText address;
    private EditText phoneNum;
    private EditText houseNum;
    private EditText email;
    private EditText gender;
    private EditText birthday;
    private EditText nokName;
    private EditText nokNum;
    private Integer score;
    private String adminId;

    private Button register;

    private String realtimeDBUrl = "https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test_wt);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        phoneNum = findViewById(R.id.phoneNum);
        houseNum = findViewById(R.id.houseNum);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        nokName = findViewById(R.id.nokName);
        nokNum = findViewById(R.id.nokPhoneNum);
        score = 0;


        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtName = name.getText().toString();
                String txtPassword = password.getText().toString();
                String txtAddress = address.getText().toString();
                String txtPhoneNum = phoneNum.getText().toString();
                String txtHouseNum = houseNum.getText().toString();
                String txtEmail = email.getText().toString();
                String txtGender = gender.getText().toString();
                String txtBirthday = birthday.getText().toString();
                String txtNokName = nokName.getText().toString();
                String txtNokNum = nokNum.getText().toString();
                String txtScore = score.toString();

                if(TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtPassword) ||
                        TextUtils.isEmpty(txtAddress) || TextUtils.isEmpty(txtPhoneNum) ||
                        TextUtils.isEmpty(txtHouseNum) || TextUtils.isEmpty(txtEmail) ||
                        TextUtils.isEmpty(txtGender) || TextUtils.isEmpty(txtBirthday) ||
                        TextUtils.isEmpty(txtNokName) || TextUtils.isEmpty(txtNokNum)){
                    Toast.makeText(firebaseTestWT.this, "Please fill up all the fields!", Toast.LENGTH_SHORT).show();
                } else{

//                    add HashMap to upload to database easier
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("userName", txtName);
                    userMap.put("userPassword", txtPassword);
                    userMap.put("userAddress", txtAddress);
                    userMap.put("userPhoneNum", txtPhoneNum);
                    userMap.put("userHouseNum", txtHouseNum);
                    userMap.put("userEmail", txtEmail);
                    userMap.put("userGender", txtGender);
                    userMap.put("userBirthday", txtBirthday);
                    userMap.put("userNokName", txtNokName);
                    userMap.put("userNokNum", txtNokNum);

                    FirebaseDatabase.getInstance(realtimeDBUrl).getReference().child("User").push().updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(firebaseTestWT.this, "Database Updated!", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }
                    });


                }
            }
        });
    }
}