package com.example.draw4brains.main.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;
import com.example.draw4brains.main.controller.MasterController;
import com.example.draw4brains.main.controller.RegisterController;

import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnRegister, btnBack;
    private RadioButton maleBtn;
    private EditText email, firstName, lastName, pass, rePass, phoneNo, homePhoneNo, address, birthday, nokName, nokPhone, chooseAdmin;
    private DatePickerDialog picker;
    private RadioGroup genderChoice;
    private RegisterController registerController;
    private Intent intent;

    // Intent Keys for Fields
    private static final String INTENT_KEY_EMAIL = "STORED_EMAIL";
    private static final String INTENT_KEY_FIRST_NAME = "STORED_FIRST_NAME";
    private static final String INTENT_KEY_LAST_NAME = "STORED_LAST_NAME";
    private static final String INTENT_KEY_GENDER_ID = "STORED_GENDER_ID";
    private static final String INTENT_KEY_PASSWORD = "STORED_PASSWORD";
    private static final String INTENT_KEY_PASSWORD2 = "STORED_PASSWORD2";
    private static final String INTENT_KEY_PHONE_NUM = "STORED_PHONE_NUM";
    private static final String INTENT_KEY_HOME_NUM = "STORED_HOME_NUM";
    private static final String INTENT_KEY_ADDRESS = "STORED_ADDRESS";
    private static final String INTENT_KEY_BIRTHDAY = "STORED_BIRTHDAY";
    private static final String INTENT_KEY_NOK_NAME = "STORED_NOK_NAME";
    private static final String INTENT_KEY_NOK_PHONE_NUM = "STORED_NOK_PHONE_NUM";
    private static final String INTENT_KEY_ADMIN_EMAIL = "STORED_ADMIN_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        // Get reference to controller classes needed.
        MasterController.registerController.loadActivityAndElements(RegisterActivity.this); // Load activity
        registerController = MasterController.registerController;

        // Initialize XML Elements to use
        btnBack = findViewById(R.id.btn_back);
        email = findViewById(R.id.et_email);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        genderChoice = findViewById(R.id.gender_group);
        maleBtn = findViewById(R.id.btn_male);
        pass = findViewById(R.id.et_password);
        rePass = findViewById(R.id.et_password2);
        phoneNo = findViewById(R.id.et_phone);
        homePhoneNo = findViewById(R.id.et_home_phone);
        address = findViewById(R.id.et_address);
        birthday = findViewById(R.id.et_birthday);
        nokName = findViewById(R.id.et_nok_name);
        nokPhone = findViewById(R.id.et_nok_phone);
        chooseAdmin = findViewById(R.id.et_chooseadmin);
        btnRegister = findViewById(R.id.btn_register);

        birthday.setInputType(InputType.TYPE_NULL);
        chooseAdmin.setInputType(InputType.TYPE_NULL);

        // Set listeners
        birthday.setOnClickListener(this);
        chooseAdmin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        // Process information from intent (if have)
        String adminEmail = getIntent().getStringExtra(INTENT_KEY_ADMIN_EMAIL);
        /// Purpose: Retain information after selecting caretaker.
        if (adminEmail != null) {
            // If adminEmail has been selected in choose admin activity, restore field data from intent
            email.setText(getIntent().getStringExtra(INTENT_KEY_EMAIL));
            firstName.setText(getIntent().getStringExtra(INTENT_KEY_FIRST_NAME));
            lastName.setText(getIntent().getStringExtra(INTENT_KEY_LAST_NAME));
            genderChoice.clearCheck(); // Clear all selection in radio group
            RadioButton selectedButton = findViewById(getIntent().getIntExtra(INTENT_KEY_GENDER_ID, maleBtn.getId())); // Get button view by gender id default is male button
            selectedButton.setChecked(true); // Set button to true
            pass.setText(getIntent().getStringExtra(INTENT_KEY_PASSWORD));
            rePass.setText(getIntent().getStringExtra(INTENT_KEY_PASSWORD2));
            phoneNo.setText(getIntent().getStringExtra(INTENT_KEY_PHONE_NUM));
            homePhoneNo.setText(getIntent().getStringExtra(INTENT_KEY_HOME_NUM));
            address.setText(getIntent().getStringExtra(INTENT_KEY_ADDRESS));
            birthday.setText(getIntent().getStringExtra(INTENT_KEY_BIRTHDAY));
            nokName.setText(getIntent().getStringExtra(INTENT_KEY_NOK_NAME));
            nokPhone.setText(getIntent().getStringExtra(INTENT_KEY_NOK_PHONE_NUM));
            chooseAdmin.setText(adminEmail);
        }
    }

    /**
     * This function overwrites the onClick function for the View class Android to accommodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: Choose Birthday, Choose Admin (Caretaker), Register Account, Back to Login Screen
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_birthday:
                this.displayBirthdayDialog();
                break;
            case R.id.et_chooseadmin:
                Intent loadUsers = new Intent(RegisterActivity.this, ChooseAdminActivity.class);
                loadUsers.putExtra(INTENT_KEY_EMAIL, email.getText().toString());
                loadUsers.putExtra(INTENT_KEY_FIRST_NAME, firstName.getText().toString());
                loadUsers.putExtra(INTENT_KEY_LAST_NAME, lastName.getText().toString());
                loadUsers.putExtra(INTENT_KEY_GENDER_ID, genderChoice.getCheckedRadioButtonId());
                loadUsers.putExtra(INTENT_KEY_PASSWORD, pass.getText().toString());
                loadUsers.putExtra(INTENT_KEY_PASSWORD2, rePass.getText().toString());
                loadUsers.putExtra(INTENT_KEY_PHONE_NUM, phoneNo.getText().toString());
                loadUsers.putExtra(INTENT_KEY_HOME_NUM, homePhoneNo.getText().toString());
                loadUsers.putExtra(INTENT_KEY_ADDRESS, address.getText().toString());
                loadUsers.putExtra(INTENT_KEY_BIRTHDAY, birthday.getText().toString());
                loadUsers.putExtra(INTENT_KEY_NOK_NAME, nokName.getText().toString());
                loadUsers.putExtra(INTENT_KEY_NOK_PHONE_NUM, nokPhone.getText().toString());
                startActivity(loadUsers);
                break;
            case R.id.btn_register:
                registerController.registerUser(new RegisterController.onCallBackFailRegisterResult() {
                    @Override
                    public void onFailure() {
                        Log.d("RegisterAttempt", "Failed");
                        Toast.makeText(RegisterActivity.this, "Invalid Fields Detected", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_back:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
