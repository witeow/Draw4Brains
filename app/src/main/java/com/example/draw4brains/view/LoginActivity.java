package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.example.draw4brains.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText firstname;
    EditText lastname;
    ImageButton loginButton, forgotPassword, registerButton;
    ToggleButton accountTypeToggle;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        loginButton = findViewById(R.id.login_button);
        forgotPassword = findViewById(R.id.forgotPasswordButton);
        registerButton = findViewById(R.id.btn_register);
        accountTypeToggle = findViewById(R.id.toggle_account_type);
        accountTypeToggle.setChecked(false);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountTypeToggle.isChecked()) {
                    // Checked == Admin Mode
                    intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                    intent.putExtra("isAdmin",true);
                    startActivity(intent);
                } else {
                    // Unchecked == User Mode
                    intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                    intent.putExtra("isAdmin",false);
                    startActivity(intent);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }
}
