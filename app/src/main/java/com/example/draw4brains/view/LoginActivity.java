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

    private EditText email;
    //EditText firstname;
    //EditText lastname;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        btnLogin = findViewById(R.id.btn_login);
        btnForgotPW = findViewById(R.id.btn_forgot_pw);
        btnRegister = findViewById(R.id.btn_register);
        btnAccType = findViewById(R.id.btn_acc_type);
        btnAccType.setChecked(false);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnAccType.isChecked()) {
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

        btnForgotPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }
}
