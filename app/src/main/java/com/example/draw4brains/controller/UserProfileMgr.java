package com.example.draw4brains.controller;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.model.User;
import com.example.draw4brains.view.HomeActivity;

public class UserProfileMgr {
    User user;

    public UserProfileMgr(){
        user = User.getInstance();
    }

    public void initialize(HomeActivity aca){

    }

    public void resetPassword(AppCompatActivity aca, Context context){

    }

    public boolean checkInputValid(String email, Context context){
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
