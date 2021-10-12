package com.example.draw4brains.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.AESCrypt;
import com.example.draw4brains.model.Admin;
import com.example.draw4brains.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
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

        // For easy logging in
        email.setText("witeow223@gmail.com");
        password.setText("Witeow1!");

        auth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString();
                String str_pass = password.getText().toString(); //TODO: useBcrypt to encrypt password
                Log.d("Debug email:",str_email);
                Log.d("Debug pass:",str_pass);
                try {
                    str_pass = AESCrypt.encrypt(str_pass);
                    Log.d("Debug encrypt pass:",str_pass);
//                    str_pass = str_pass.substring(0, str_pass.length()-3);
//                    Log.d("Debug encrypt pass:",str_pass);
                    auth.signInWithEmailAndPassword(str_email, str_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String refUserType = "";
                                if (btnAccType.isChecked()) {

                                    refUserType = "Admin";
                                }else{
                                    refUserType = "User";
                                }
                                DatabaseReference userRef = FirebaseDatabase
                                        .getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference(refUserType);
                                Query query = userRef.orderByChild("userEmail").equalTo(str_email);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            if (btnAccType.isChecked()){
                                                // Checked == Admin Mode
                                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                                                intent.putExtra("isAdmin",true);
                                                currentAdmin = new Admin(str_email);
                                                Log.d("AdminDEBUG", "Admin has logged in!");
                                                startActivity(intent);
                                            } else if (!btnAccType.isChecked()) {
//                                        Unchecked == User Mode
                                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                intent = new Intent(getApplicationContext(), UserHomeActivity.class);
                                                intent.putExtra("isAdmin",false);
                                                currentUser = new User(str_email);
                                                Log.d("UserDEBUG", "User has logged in!");
                                                startActivity(intent);
                                            } else{
                                                Log.d("LoginDEBUG", "Account Type not specified!");
                                                Toast.makeText(LoginActivity.this,"Account Type not specified!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else{
                                            Log.d("LoginDEBUG", "Account not found in database!");
                                            Toast.makeText(LoginActivity.this,"No User found!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(LoginActivity.this, "Databse could not be accessed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else{
                                Toast.makeText(LoginActivity.this, "No User found!", Toast.LENGTH_SHORT).show();
                                Log.d("LoginDEBUG", "Task Unsuccessful!");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
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
