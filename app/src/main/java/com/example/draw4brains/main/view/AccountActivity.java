package com.example.draw4brains.main.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.draw4brains.R;
import com.example.draw4brains.main.controller.AuthenticationController;
import com.example.draw4brains.main.controller.MasterController;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvBirthday;
    private TextView tvPhone;
    private TextView tvHouseNum;
    private TextView tvAddress;
    private TextView tvEmail;
    private TextView tvCaretakerEmail;
    private TextView tvNextOfKinName;
    private TextView tvNextOfKinNum;
    private ImageButton btnBack, btnLogout;
    private Intent intent;
    private AuthenticationController authenticationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

        // Get reference to controller classes needed.
        authenticationController = MasterController.authenticationController;

        // Initialize XML Elements to use
        btnBack = findViewById(R.id.btn_back);
        btnLogout = findViewById(R.id.btn_logout);
        linearLayout = findViewById(R.id.linear_layout_account);
        tvName = findViewById(R.id.tv_name);
        tvGender = findViewById(R.id.tv_gender);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvPhone = findViewById(R.id.tv_phone);
        tvHouseNum = findViewById(R.id.tv_house_num);
        tvAddress = findViewById(R.id.tv_address);
        tvEmail = findViewById(R.id.tv_email);
        tvCaretakerEmail = findViewById(R.id.tv_caretaker_email);
        tvNextOfKinName = findViewById(R.id.tv_nok_name);
        tvNextOfKinNum = findViewById(R.id.tv_nok_num);

        // Set listeners
        btnBack.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        // Retrieve information from static variables to display on screen
        if (authenticationController.isCurrentlyAdmin()) {

            // Display information of admin
            tvName.setText(authenticationController.getCurrentAdmin().getAdminName());
            tvPhone.setText(authenticationController.getCurrentAdmin().getPhoneNo());
            tvEmail.setText(authenticationController.getCurrentAdmin().getEmailAddress());

            linearLayout.removeView(tvGender);
            linearLayout.removeView(tvBirthday);
            linearLayout.removeView(tvHouseNum);
            linearLayout.removeView(tvAddress);
            linearLayout.removeView(tvCaretakerEmail);
            linearLayout.removeView(tvNextOfKinName);
            linearLayout.removeView(tvNextOfKinNum);

            linearLayout.removeView(findViewById(R.id.tv_gender_title));
            linearLayout.removeView(findViewById(R.id.tv_birthday_title));
            linearLayout.removeView(findViewById(R.id.tv_house_num_title));
            linearLayout.removeView(findViewById(R.id.tv_address_title));
            linearLayout.removeView(findViewById(R.id.tv_caretaker_email_title));
            linearLayout.removeView(findViewById(R.id.tv_nok_name_title));
            linearLayout.removeView(findViewById(R.id.tv_nok_num_title));

        } else {
            // Display information of normal user
            tvName.setText(authenticationController.getCurrentUser().getUserName());
            tvGender.setText(authenticationController.getCurrentUser().getGender());
            tvBirthday.setText(authenticationController.getCurrentUser().getBirthday());
            tvPhone.setText(authenticationController.getCurrentUser().getPhoneNo());
            tvHouseNum.setText(authenticationController.getCurrentUser().getHouseNo());
            tvAddress.setText(authenticationController.getCurrentUser().getAddress());
            tvEmail.setText(authenticationController.getCurrentUser().getEmailAddress());
            tvCaretakerEmail.setText(authenticationController.getCurrentUser().getCaretaker_email());
            tvNextOfKinName.setText(authenticationController.getCurrentUser().getNokName());
            tvNextOfKinNum.setText(authenticationController.getCurrentUser().getNokNum());
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
                this.backToHomeScreen(authenticationController.isCurrentlyAdmin());
                break;
            case R.id.btn_logout:
                this.logout();
                break;
        }
    }

    private void logout() {
        authenticationController.logout(new AuthenticationController.callBackOnLogout() {
            @Override
            public void onComplete() {
                Toast.makeText(AccountActivity.this, "Logging out!", Toast.LENGTH_SHORT).show();
                intent = new Intent(AccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
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