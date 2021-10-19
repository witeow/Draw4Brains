package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.PasswordResetMgr;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnReset, btnBack;
    private EditText etEmail;
    private Intent intent;
    private PasswordResetMgr passwordResetMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password);

        // Initialize Controllers
        passwordResetMgr = new PasswordResetMgr();

        // Initialize XML Elements to use
        btnReset = findViewById(R.id.btn_reset);
        btnBack = findViewById(R.id.btn_back);
        etEmail = findViewById(R.id.et_email);

        // Set listeners
        btnReset.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This function overwrites the onClick function for the View class Android to accomodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Back to Login, Reset Email
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset:
                this.resetEmail();
                break;
            case R.id.btn_back:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void resetEmail() {
        if (etEmail.getText().length() == 0) { // Means cannot reset
            showToast("Email field blank");
            etEmail.setError("Email field blank");
            etEmail.setFocusable(true);
        } else {
            String email = etEmail.getText().toString().trim();
            passwordResetMgr.resetPassword(email);
            showToast("A reset link will be send if email exists!");
            intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}