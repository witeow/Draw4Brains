package com.example.draw4brains.controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.draw4brains.R;
import com.example.draw4brains.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterMgr {
    /**user input for user profile info**/
    EditText usernameInput, emailInput, passwordInput, repasswordInput;
    DatePicker birthdayInput;
    RadioButton maleRadioInput, femaleRadioInput, otherRadioInput;
    Button registerBtn;
    TextView loginTextBtn;

    private static final String TAG = "RegisterMgr";


    public void register(AppCompatActivity aca) {

        usernameInput = aca.findViewById(R.id.userNameTxtInput);
        emailInput = aca.findViewById(R.id.emailTxtInput);
        passwordInput = aca.findViewById(R.id.passwordTxtInput);
        repasswordInput = aca.findViewById(R.id.rePasswordTxtInput);
        birthdayInput = aca.findViewById(R.id.registerDatePicker);
        maleRadioInput = aca.findViewById(R.id.maleRadioBtn);
        femaleRadioInput = aca.findViewById(R.id.femaleRadioButton);
        otherRadioInput = aca.findViewById(R.id.otherGenderRadio);
        registerBtn = aca.findViewById(R.id.registerBtn);
        loginTextBtn = aca.findViewById(R.id.loginTextBtn);

        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String repassword = repasswordInput.getText().toString().trim();

        String gender;

        if (maleRadioInput.isChecked()) {
            gender = "male";
        } else if (femaleRadioInput.isChecked()) {
            gender = "female";
        } else if (otherRadioInput.isChecked()) {
            gender = "others";
        } else {
            gender = null;
        }

        int day = birthdayInput.getDayOfMonth();
        int month = birthdayInput.getMonth() + 1;
        int year = birthdayInput.getYear();

        String userBirthday = String.format("%02d%02d%04d", day, month, year);

        boolean valid = checkInputValid(username, email, password, repassword, gender, userBirthday, aca);
        if(!valid){
            return;
        }

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = User.getInstance();
                    user.setUserName(username);
                    user.getBirthday().setTime(userBirthday);
                    user.setEmailAddress(email);
                    user.setGender(gender);

                    FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference uRef = fDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User");

                    Toast.makeText(aca, "User created", Toast.LENGTH_SHORT).show();
                    FirebaseUser fuser = fAuth.getCurrentUser();
                    uRef.child(fAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d(TAG, "register: registration successful, please verify email before login");
                            Toast.makeText(aca, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "register: registration failed, unknown error");
                            Toast.makeText(aca, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    });

                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(aca, "Verification email has been sent to your email" , Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                           Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(aca, "Unsuccessful" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean checkInputValid(String userName, String email, String password, String rePassword, String gender, String birthday, Context context){
        if(userName.isEmpty()){
            Toast.makeText(context, "no username", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(email.isEmpty()){
            Toast.makeText(context, "no email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.isEmpty()){
            Toast.makeText(context, "no pwd", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(rePassword.isEmpty()){
            Toast.makeText(context, "no repwd", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.equals(rePassword)){
            Toast.makeText(context, "pwd mismatch repwd", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(gender == null){
            Toast.makeText(context, "no gender selection", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(birthday == null){
            Toast.makeText(context, "no bd", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
