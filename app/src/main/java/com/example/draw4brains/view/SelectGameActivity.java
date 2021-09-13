package com.example.draw4brains.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.draw4brains.R;

public class SelectGameActivity extends AppCompatActivity {

    //ImageButton selectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        /*selectButton = findViewById(R.id.select_button);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SelectGameActivity.class));
            }
        });*/

    }
}