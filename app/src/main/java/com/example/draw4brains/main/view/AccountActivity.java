package com.example.draw4brains.main.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.main.controller.AuthenticationMgr;
import com.example.draw4brains.main.controller.MasterMgr;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvEmail, tvName, tvBirthday, tvPhone;
    private ImageButton btnBack, btnLogout;
    private Intent intent;
    private AuthenticationMgr authenticationMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

        // Get reference to controller classes needed.
        authenticationMgr = MasterMgr.authenticationMgr;

        // Initialize XML Elements to use
        btnBack = findViewById(R.id.btn_back);
        tvEmail = findViewById(R.id.tv_email);
        tvName = findViewById(R.id.tv_name);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvPhone = findViewById(R.id.tv_phone);
        btnLogout = findViewById(R.id.btn_logout);

        // Set listeners
        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        // Retrieve information from static variables to display on screen
        if (authenticationMgr.isCurrentlyAdmin()) {

            // Display information of admin
            tvEmail.setText(authenticationMgr.getCurrentAdmin().getEmailAddress());
            tvName.setText(authenticationMgr.getCurrentAdmin().getAdminName());
            tvBirthday.setText("Not available");
            tvPhone.setText(authenticationMgr.getCurrentAdmin().getPhoneNo());
        } else {
            // Display information of normal user
            tvEmail.setText(authenticationMgr.getCurrentUser().getEmailAddress());
            tvName.setText(authenticationMgr.getCurrentUser().getUserName());
            tvBirthday.setText(authenticationMgr.getCurrentUser().getBirthday());
            tvPhone.setText(authenticationMgr.getCurrentUser().getPhoneNo());
        }

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
            case R.id.btn_back:
                this.backToHomeScreen(authenticationMgr.isCurrentlyAdmin());
                break;
            case R.id.btn_logout:
                authenticationMgr.logout(AccountActivity.this);
                break;
        }
    }

    private void backToHomeScreen(boolean isAdmin) {
        if (isAdmin) {
            // Admin is using currently
            intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
        } else {
            // User is using currently
            intent = new Intent(getApplicationContext(), UserHomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}