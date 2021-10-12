package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.draw4brains.R;
import com.example.draw4brains.model.Admin;
import com.example.draw4brains.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    //EditText firstname;
    //EditText lastname;
    private ImageButton btnLogin, btnForgotPW, btnRegister;
    private ToggleButton btnAccType;
    private FirebaseAuth auth;
    public static User currentUser;
    public static Admin currentAdmin;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_login);

        email =findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPW = findViewById(R.id.btn_forgot_pw);
        btnRegister = findViewById(R.id.btn_register);
        btnAccType = findViewById(R.id.btn_acc_type);
        btnAccType.setChecked(false);

        // For testing purposes
        Button shortcutToGame = findViewById(R.id.shortcutGame);
        shortcutToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ConnectDotsActivity.class);
                intent.putExtra("isAdmin",true);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString();
                String str_pass = password.getText().toString(); //TODO: useBcrypt to encrypt password
//                Log.d("Debug email:",str_email);
//                Log.d("Debug pass:",str_pass);
                auth.signInWithEmailAndPassword(str_email, str_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        if (btnAccType.isChecked()) {
                            // Checked == Admin Mode
                            intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                            intent.putExtra("isAdmin",true);
                            currentAdmin = new Admin(str_email);
                            startActivity(intent);
                        } else {
                            // Unchecked == User Mode
                            intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                            intent.putExtra("isAdmin",false);
                            currentUser = new User(str_email);
                            Log.d("current", String.valueOf(currentUser.getUserID()));
                            Log.d("UserDEBUG", "User has logged in!");
                            startActivity(intent);
                        }
                    }
                });

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
