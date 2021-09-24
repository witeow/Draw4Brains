package com.example.draw4brains.controller;



import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.view.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMgr extends LoginActivity {

    private ImageButton login;
    private ImageButton register;
    private ImageButton forgot_pass;
    private ToggleButton user_type;
    private EditText login_username;
    private EditText login_password;

    private FirebaseAuth auth;




    private void verifyLogin(String email, String password){
        /**insert code for check input valid**/
        /**insert code for verify with firebase data**/

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginMgr.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean checkInputValid(String email, String password, Context context){
        return true;
    }
}
