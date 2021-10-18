package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.LoginMgr;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnReset, btnBack;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password);

        btnReset = findViewById(R.id.btn_reset);
        btnBack = findViewById(R.id.btn_back);

        btnReset.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
            case R.id.btn_reset:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                showToast("A reset link has been sent to the email!");
                startActivity(intent);
                break;
            case R.id.btn_back:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

}