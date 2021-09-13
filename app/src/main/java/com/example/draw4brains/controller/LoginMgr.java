package com.example.draw4brains.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.view.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.regex.Pattern;

public class LoginMgr {

    public void login(AppCompatActivity aca){
        /**insert code for check input valid**/
        /**insert code for verify with firebase data**/
        EditText username;
        EditText psw;
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        username = aca.findViewById(R.id.loginUsernameTxtInput);
        psw  = aca.findViewById(R.id.loginPasswordTxtInput);
        String email = username.getText().toString().trim();
        String password = psw.getText().toString().trim();

        Boolean validInput = checkInputValid(email, password, aca);
        if(!validInput){
            return;
        }
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(aca, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(aca, HomeActivity.class);
                    aca.startActivity(i);
                    aca.finish();
                }
                else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(aca,"Login failed, invalid user", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(aca, "Login failed, wrong authentication", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(aca, "Login failed, unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public boolean checkInputValid(String email, String password, Context context){
        if(email.isEmpty()){
            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.isEmpty()){
            Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }

}
