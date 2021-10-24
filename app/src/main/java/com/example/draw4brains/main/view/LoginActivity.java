package com.example.draw4brains.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.draw4brains.R;
import com.example.draw4brains.main.controller.AuthenticationController;
import com.example.draw4brains.main.controller.MasterController;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;
    private Intent intent;
    private AuthenticationController authenticationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        // Get reference to controller classes needed.
        authenticationController = MasterController.authenticationController;

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
     * This function overwrites the onClick function for the View class Android to accommodate for
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
                login(email, password);
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

    private void login(EditText email, EditText password) {
        if (hasValidLoginFields(email, password)) {
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();
            boolean isAdmin = btnAccType.isChecked() ? true : false;
            authenticationController.login(emailString, passwordString, isAdmin, new AuthenticationController.callBackOnLoginAttempt() {
                @Override
                public void onSuccess(String message, boolean isAdmin) {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    if (isAdmin) {
                        Log.d("AdminDEBUG", "Admin has logged in!");
                        intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("UserDEBUG", "User has logged in!");
                        Log.d("UserDEBUG", authenticationController.getCurrentUser().getAddress());
                        intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(String message) {
                    Log.d("LoginFail", "Login failed");
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Invalid login fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasValidLoginFields(EditText email, EditText password) {
        boolean isValid = true;
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString();
        if (emailString.contains(" ")) {
            email.setError("Email cannot contain spaces!");
            isValid = false;
        }
        if (emailString.equalsIgnoreCase("")) {
            email.setError("Email cannot be blank!");
            isValid = false;
        }
        if (passwordString.contains(" ")) {
            password.setError("Password cannot contain spaces!");
            isValid = false;
        }
        if (passwordString.trim().equalsIgnoreCase("")){
            password.setError("Password cannot be empty!");
            isValid = false;
        }
        return true ? isValid : false;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in already and then move to main screen, using the correct UI
        authenticationController.signInIfAccountExist(LoginActivity.this);
    }
}
