package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.RegisterMgr;

public class RegisterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                RegisterMgr registerMgr = new RegisterMgr();
                registerMgr.register(RegisterActivity.this);
            }
        });
    }


}
