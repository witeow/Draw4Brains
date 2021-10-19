package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.AuthenticationMgr;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;
    private Intent intent;
    private AuthenticationMgr authenticationMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        // Initialize controller class instances
        authenticationMgr = new AuthenticationMgr();

        // Initialize XML Elements to use
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPW = findViewById(R.id.btn_forgot_pw);
        btnRegister = findViewById(R.id.btn_register);
        btnAccType = findViewById(R.id.btn_acc_type);
        btnAccType.setChecked(false); // Default is user account

        // Set listeners
        btnLogin.setOnClickListener(this);
        btnForgotPW.setOnClickListener(this);
        btnRegister.setOnClickListener(this);


        // For easy logging  and switching to admin ///////
        email.setText("witeow223@gmail.com");
        password.setText("Witeow1!");

        btnAccType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdmin = btnAccType.isChecked() ? true : false;
                if (isAdmin) {
                    email.setText("WLIM095@e.ntu.edu.sg");
                    password.setText("Witeow1!");
                } else {
                    email.setText("witeow223@gmail.com");
                    password.setText("Witeow1!");
                }
            }
        });
        /////////////////////////////////////////////////////

    }

    /**
     * This function overwrites the onClick function for the View class Android to accomodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Login, Register, Forget Password
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                boolean isAdmin = btnAccType.isChecked() ? true : false;
                authenticationMgr.login(LoginActivity.this, emailString, passwordString, isAdmin);
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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // Check if user is signed in already and then move to main screen
        authenticationMgr.signInIfAccountExist(LoginActivity.this);
    }
}
