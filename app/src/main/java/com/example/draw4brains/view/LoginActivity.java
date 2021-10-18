package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import com.example.draw4brains.R;
import com.example.draw4brains.controller.LoginMgr;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        email =findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPW = findViewById(R.id.btn_forgot_pw);
        btnRegister = findViewById(R.id.btn_register);
        btnAccType = findViewById(R.id.btn_acc_type);
        btnAccType.setChecked(false);

        // For easy logging in
        email.setText("witeow223@gmail.com");
        password.setText("Witeow1!");

        btnLogin.setOnClickListener(this);
        btnForgotPW.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    /**
     * This function overwrites the onClick function for the View class Android to accomodate for
     * different behaviour for different click events when different views are interacted with by user
     *
     * Options available: Back to Start Up, Login Button, Forgot Password Option.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_login:
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                boolean isAdmin = btnAccType.isChecked() ? true : false;
                LoginMgr loginMgr = new LoginMgr();
                loginMgr.login(LoginActivity.this, emailString, passwordString, isAdmin);
                break;
            case R.id.btn_register:
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forgot_pw:
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }


}
