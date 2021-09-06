package com.example.draw4brains;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class firebaseTest extends AppCompatActivity {

     @Override
     protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         Toast.makeText(firebaseTest.this, "Firebase worked!", Toast.LENGTH_LONG).show();
     }
}
