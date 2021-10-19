package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.AuthenticationMgr;

public class AccountActivity extends AppCompatActivity {

    private TextView tvEmail, tvName, tvBirthday, tvPhone;
    private ImageButton btnBack, btnLogout;
    private boolean isAdmin;
    private Bundle extras;
    private Intent intent;
    private AuthenticationMgr authenticationMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

        authenticationMgr = new AuthenticationMgr();

        // Get data of intent
        extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean("isAdmin");
            //The key argument here must match that used in the other activity
        }

        // Set textview reference
        tvEmail = findViewById(R.id.tv_email);
        tvName = findViewById(R.id.tv_name);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvPhone = findViewById(R.id.tv_phone);

        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);

        if (isAdmin) {
            tvEmail.setText(AuthenticationMgr.currentAdmin.getEmailAddress());
            tvName.setText(AuthenticationMgr.currentAdmin.getAdminName());
            tvBirthday.setText("023029329");
            tvPhone.setText(AuthenticationMgr.currentAdmin.getPhoneNo());
        } else {
            tvEmail.setText(AuthenticationMgr.currentUser.getEmailAddress());
            tvName.setText(AuthenticationMgr.currentUser.getUserName());
            tvBirthday.setText(AuthenticationMgr.currentUser.getBirthday());
            tvPhone.setText(AuthenticationMgr.currentUser.getPhoneNo());
        }

        // Set onClick Listeners
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationMgr.logout(AccountActivity.this);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdmin) {
                    // Admin is using currently
                    intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                    intent.putExtra("isAdmin",isAdmin);
                    startActivity(intent);
                } else {
                    // User is using currently
                    intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                    intent.putExtra("isAdmin",isAdmin);
                    startActivity(intent);
                }
            }
        });


    }

}