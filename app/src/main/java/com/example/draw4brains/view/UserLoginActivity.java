package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.LoginMgr;
import com.example.draw4brains.controller.RegisterMgr;

public class UserLoginActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        findViewById(R.id.mainLoginBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginMgr loginMgr = new LoginMgr();
                loginMgr.login(UserLoginActivity.this);
            }
        });
    }

    public void register(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void forgetPassword(View v) { startActivity(new Intent(this, ForgetPasswordActivity.class));}
}
