package com.example.draw4brains.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.draw4brains.R;

public class AccountActivity extends AppCompatActivity {

    TextView emailView, nameVew, birthdayView, phoneNumberView;
    ImageButton backBtn, logoutBtn;
    boolean isAdmin;
    Bundle extras;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Get data of intent
        extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean("isAdmin");
            //The key argument here must match that used in the other activity
        }

        // Set textview reference
        emailView = findViewById(R.id.email_text_view);
        nameVew = findViewById(R.id.name_text_view);
        birthdayView = findViewById(R.id.birthday_text_view);
        phoneNumberView = findViewById(R.id.phone_text_view);

        backBtn = findViewById(R.id.back_button);
        logoutBtn = findViewById(R.id.logout_button);


        // Set onClick Listeners
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
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