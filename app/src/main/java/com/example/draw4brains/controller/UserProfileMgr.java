package com.example.draw4brains.controller;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.model.User;
import com.example.draw4brains.view.ForgetPasswordActivity;
import com.example.draw4brains.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileMgr {
    User user;
    FirebaseAuth fAuth;

    public UserProfileMgr(){
        user = User.getInstance();
    }

    public void initialize(HomeActivity aca){

    }

    public void resetPassword(AppCompatActivity aca, Context context){
        TextView email = aca.findViewById(R.id.forgetPasswordEmailInput);
        Button resetBtn = aca.findViewById(R.id.forgetPasswordResetPasswordButton);
        String resetPwdEmailString = email.getText().toString().trim();
        fAuth = FirebaseAuth.getInstance();

        boolean valid = checkInputValid(resetPwdEmailString, context);
        if (!valid) {
            return;
        }

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fAuth.sendPasswordResetEmail(resetPwdEmailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(aca, "Password sent to your email", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(aca, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public boolean checkInputValid(String email, Context context){
        if(email.isEmpty()) {
            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void displayUserHomeInfo(AppCompatActivity aca, View view){

    }

    public void editUserProfile(AppCompatActivity aca, View view){

    }

    private void save(){

    }

    public void logout(AppCompatActivity aca){

    }
}
