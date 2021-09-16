package com.example.draw4brains.view;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

import java.security.AccessControlContext;

public class AdminHomeActivity extends AppCompatActivity {

    private ImageButton btnCustomerList, btnAccInfo;
    boolean isAdmin;
    Bundle extras;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_admin);

        // Get data of intent
        extras = getIntent().getExtras();
        if (extras != null) {
            isAdmin = extras.getBoolean("isAdmin");
            //The key argument here must match that used in the other activity
        }

        btnCustomerList = findViewById(R.id.btn_customer_list);
        btnAccInfo = findViewById(R.id.btn_acc_info);

        btnCustomerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), UsersListActivity.class);
                startActivity(intent);
            }
        });

        btnAccInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), AccountActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                startActivity(intent);
            }
        });

    }
}
