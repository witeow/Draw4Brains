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
import com.example.draw4brains.games.connectthedots.model.Score;
import com.example.draw4brains.main.controller.AuthenticationMgr;
import com.example.draw4brains.main.controller.MasterMgr;

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
    private AuthenticationMgr authenticationMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_settings);

        // Get reference to controller classes needed.
        authenticationMgr = MasterMgr.authenticationMgr;

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
        if (authenticationMgr.isCurrentlyAdmin()) {

            // Display information of admin
            tvName.setText(authenticationMgr.getCurrentAdmin().getAdminName());
            tvPhone.setText(authenticationMgr.getCurrentAdmin().getPhoneNo());
            tvEmail.setText(authenticationMgr.getCurrentAdmin().getEmailAddress());

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
            tvName.setText(authenticationMgr.getCurrentUser().getUserName());
            tvGender.setText(authenticationMgr.getCurrentUser().getGender());
            tvBirthday.setText(authenticationMgr.getCurrentUser().getBirthday());
            tvPhone.setText(authenticationMgr.getCurrentUser().getPhoneNo());
            tvHouseNum.setText(authenticationMgr.getCurrentUser().getHouseNo());
            tvAddress.setText(authenticationMgr.getCurrentUser().getAddress());
            tvEmail.setText(authenticationMgr.getCurrentUser().getEmailAddress());
            tvCaretakerEmail.setText(authenticationMgr.getCurrentUser().getCaretaker_email());
            tvNextOfKinName.setText(authenticationMgr.getCurrentUser().getNokName());
            tvNextOfKinNum.setText(authenticationMgr.getCurrentUser().getNokNum());
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
                this.backToHomeScreen(authenticationMgr.isCurrentlyAdmin());
                break;
            case R.id.btn_logout:
                this.logout();
                break;
        }
    }

    private void logout() {
        authenticationMgr.logout(new AuthenticationMgr.callBackOnLogout() {
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