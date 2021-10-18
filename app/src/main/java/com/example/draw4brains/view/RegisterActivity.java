package com.example.draw4brains.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.controller.AESCrypt;

import com.example.draw4brains.R;
import com.example.draw4brains.controller.RegisterMgr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnRegister, btnBack;
    private RadioButton gender, maleBtn, femaleBtn;
    private EditText email, firstName, lastName, pass, rePass, phoneNo, homePhoneNo, address, birthday, nokName, nokPhone, chooseAdmin;
    private DatePickerDialog picker;
    private RadioGroup genderChoice;

    private ArrayList<String> attList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        String admin_email = getIntent().getStringExtra("Admin_email");
//        String admin_uid = getIntent().getStringExtra("Admin_uid");

        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
        email = findViewById(R.id.et_email);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        genderChoice = findViewById(R.id.gender_group);
        pass = findViewById(R.id.et_password);
        rePass = findViewById(R.id.et_password2);
        phoneNo = findViewById(R.id.et_phone);
        homePhoneNo = findViewById(R.id.et_home_phone);
        address = findViewById(R.id.et_address);
        birthday = findViewById(R.id.et_birthday);
        nokName = findViewById(R.id.et_nok_name);
        nokPhone = findViewById(R.id.et_nok_phone);
        chooseAdmin = findViewById(R.id.et_chooseadmin);

        birthday.setInputType(InputType.TYPE_NULL);
        chooseAdmin.setInputType(InputType.TYPE_NULL);

        maleBtn = findViewById(R.id.btn_male);
        femaleBtn = findViewById(R.id.btn_female);

        // Retain information after selecting caretaker.
        if (admin_email != null) {
            email.setText(getIntent().getStringExtra("Stored_Email"));
            firstName.setText(getIntent().getStringExtra("Stored_First_Name"));
            lastName.setText(getIntent().getStringExtra("Stored_Last_Name"));
            Log.d("gender in tent", getIntent().getStringExtra("Stored_Gender"));
            if (TextUtils.equals(getIntent().getStringExtra("Stored_Gender"), "Male")) {
                maleBtn.setChecked(true);
                femaleBtn.setChecked(false);
            } else {
                femaleBtn.setChecked(true);
                maleBtn.setChecked(false);
            }
//            gender.setText(getIntent().getStringExtra("Stored_Gender"));
            pass.setText(getIntent().getStringExtra("Stored_Password"));
            rePass.setText(getIntent().getStringExtra("Stored_Re_Password"));
            phoneNo.setText(getIntent().getStringExtra("Stored_Phone"));
            homePhoneNo.setText(getIntent().getStringExtra("Stored_Home"));
            address.setText(getIntent().getStringExtra("Stored_Address"));
            birthday.setText(getIntent().getStringExtra("Stored_Birthday"));
            nokName.setText(getIntent().getStringExtra("Stored_Nok_Name"));
            nokPhone.setText(getIntent().getStringExtra("Stored_Nok_Phone"));

            chooseAdmin.setText(admin_email);
        }

        birthday.setOnClickListener(this);
        chooseAdmin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Integer intGender = null;
        switch (view.getId()) {
            case R.id.et_birthday:
                this.displayBirthdayDialog();
                break;
            case R.id.et_chooseadmin:
                Intent loadUsers = new Intent(RegisterActivity.this, ChooseAdminActivity.class);
                loadUsers.putExtra("Stored_Email", email.getText().toString());
                loadUsers.putExtra("Stored_First_Name", firstName.getText().toString());
                loadUsers.putExtra("Stored_Last_Name", lastName.getText().toString());
                intGender = genderChoice.getCheckedRadioButtonId();
                gender = findViewById(intGender);
                loadUsers.putExtra("Stored_Gender", gender.getText().toString());
                Log.d("gender before change", gender.getText().toString());
                loadUsers.putExtra("Stored_Password", pass.getText().toString());
                loadUsers.putExtra("Stored_Re_Password", rePass.getText().toString());
                loadUsers.putExtra("Stored_Phone", phoneNo.getText().toString());
                loadUsers.putExtra("Stored_Home", homePhoneNo.getText().toString());
                loadUsers.putExtra("Stored_Address", address.getText().toString());
                loadUsers.putExtra("Stored_Birthday", birthday.getText().toString());
                loadUsers.putExtra("Stored_Nok_Name", nokName.getText().toString());
                loadUsers.putExtra("Stored_Nok_Phone", nokPhone.getText().toString());
                startActivity(loadUsers);
                break;
            case R.id.btn_register:
                Log.d("Register", "Button clicked");
                RegisterMgr registerMgr = new RegisterMgr(RegisterActivity.this);
                registerMgr.registerUser(new RegisterMgr.onCallBackRegisterResult() {
                    @Override
                    public void onCallback(boolean isSuccessful) {
                        if (!isSuccessful) {
                            Log.d("Cred", "TOASTED");
                            Toast.makeText(RegisterActivity.this, "Invalid Fields Detected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btn_back:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    private void displayBirthdayDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(RegisterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        picker.getDatePicker().setMaxDate(System.currentTimeMillis());
        picker.show();
    }


}
