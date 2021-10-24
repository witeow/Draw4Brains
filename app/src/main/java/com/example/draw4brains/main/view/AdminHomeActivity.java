package com.example.draw4brains.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnCustomerList, btnAccInfo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_admin);

        btnCustomerList = findViewById(R.id.btn_customer_list);
        btnAccInfo = findViewById(R.id.btn_acc_info);

        btnCustomerList.setOnClickListener(this);
        btnAccInfo.setOnClickListener(this);
    }

    /**
     * This function overwrites the onClick function for the View class Android to accommodate for
     * different behaviour for different click events when different views are interacted with by user
     * <p>
     * Options available: View Statistics of Customers, View Account Information
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_customer_list:
                intent = new Intent(AdminHomeActivity.this, UsersListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_acc_info:
                intent = new Intent(AdminHomeActivity.this, AccountActivity.class);
                startActivity(intent);
                break;
        }
    }
}
