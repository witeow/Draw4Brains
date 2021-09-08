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

import com.example.draw4brains.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterMgr {
    /**user input for user profile info**/
    EditText usernameInput, emailInput, passwordInput, repasswordInput;
    DatePicker birthdayInput;
    RadioButton maleRadioInput, femaleRadioInput, otherRadioInput;
    Button registerBtn;
    TextView loginTextBtn;
    FirebaseAuth fAuth;


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
        String email = usernameInput.getText().toString().trim();
        String password = usernameInput.getText().toString().trim();
        String repassword = usernameInput.getText().toString().trim();

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
        int month = birthdayInput.getMonth();
        int year = birthdayInput.getYear();

        String userBirthday = String.format("%02d%02d%04d", day, month, year);

        boolean valid = checkInputValid(username, email, password, repassword, gender, userBirthday, aca);
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(aca, "User created", Toast.LENGTH_SHORT).show();
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
