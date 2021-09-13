package com.example.draw4brains.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.UserProfileMgr;

public class ForgetPasswordActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_page);

        findViewById(R.id.forgetPasswordResetPasswordButton).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UserProfileMgr userProfileMgr = new UserProfileMgr();
                Context context = getApplicationContext();
                userProfileMgr.resetPassword(ForgetPasswordActivity.this, context);
            }
        });


    }
}
