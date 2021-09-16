package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;

public class AccountActivity extends AppCompatActivity {

    private TextView tvEmail, tvName, tvBirthday, tvPhone;
    private ImageButton btnBack, btnLogout;
    private boolean isAdmin;
    private Bundle extras;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

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


        // Set onClick Listeners
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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