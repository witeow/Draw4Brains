package com.example.draw4brains.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.draw4brains.R;

import androidx.appcompat.app.AppCompatActivity;

public class UserLoginActivity extends AppCompatActivity {

    EditText email;
    EditText firstname;
    EditText lastname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);




    }
}
