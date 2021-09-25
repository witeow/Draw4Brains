package com.example.draw4brains.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.draw4brains.controller.RegisterMgr;

import com.example.draw4brains.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class RegisterActivity extends AppCompatActivity {

    private ImageButton btnRegister, btnBack;
    private Button choose_admin;
    private EditText email, firstName, lastName, pass, rePass, phoneNo, birthday, adminEdit;
    private RadioGroup genderChoice;
    private AutoCompleteTextView adminView;

    private ArrayList<String> attList = new ArrayList<>();

    private FirebaseAuth auth;
    private static String realtimedatabase = "https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance(realtimedatabase);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        //TODO Russell
        String admin_email = getIntent().getStringExtra("Admin_email");
        String admin_uid = getIntent().getStringExtra("Admin_uid");

        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
        email = findViewById(R.id.et_email);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        genderChoice = findViewById(R.id.gender_group);
        pass = findViewById(R.id.et_password);
        rePass = findViewById(R.id.et_password2);
//        admin = findViewById(R.id.)
//        passStrength = findViewById(R.id.pb_password_strength);
        phoneNo = findViewById(R.id.et_phone);
        birthday = findViewById(R.id.et_birthday);
        choose_admin = findViewById(R.id.button_chooseadmin);

        auth = FirebaseAuth.getInstance();


//        Fill AutoCompleteTextView

        if(admin_email!=null){
            choose_admin.setText(admin_email);
        }

        choose_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, ChooseAdminActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RegisterMgr registerMgr = new RegisterMgr();
//                registerMgr.register(RegisterActivity.this);
                String regEmail = email.getText().toString();
                String regFirstName = firstName.getText().toString();
                String regLastName = lastName.getText().toString();
                int intGender = genderChoice.getCheckedRadioButtonId();
//                gender = findViewById(intGender);
                String regPass = pass.getText().toString();
                String regRePass = rePass.getText().toString();
                String regPhoneNo = phoneNo.getText().toString();
                String regBirthday = birthday.getText().toString();


                Boolean checkOutcome = CredentialsCheck(regEmail, regFirstName, regLastName, intGender, regPass, regRePass, regPhoneNo,
                                                        regBirthday);
                if (checkOutcome){
                    registerUser(regEmail, regFirstName, regLastName, intGender, regPass, regRePass, regPhoneNo,
                                regBirthday);
                } else{
                    Toast.makeText(RegisterActivity.this, "Invalid Fields Detected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

//    private void showToast(String msg) {
//        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//    }

    private void registerUser(String strEmail, String strFirstName, String strLastName, Integer intGender, String strPass,
                              String strRePass, String strPhoneNo, String strBirthday){
//            Check if email used before
        DatabaseReference userRef = FirebaseDatabase.getInstance(realtimedatabase).getReference("User");
        Query query = userRef.orderByChild("userEmail").equalTo(strEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    email.setError("Email currently used! Please use a different email instead");
                }else{
                    auth.createUserWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                HashMap<String, Object> registerUser = new HashMap<>();
                                String[] userAttributes = {"userEmail", "userName", "userGender", "userPassword", "userPhoneNum",
                                        "userHouseNum", "userAddress", "userBirthday", "userScore",  "userNokName", "userNokNum"};

                                String strGender;
                                if (intGender==1){
                                    strGender = "Female";
                                }else{
                                    strGender = "Male";
                                }
                                String[] regAttributes = {strEmail, strFirstName + " " + strLastName, strGender, strPass,
                                        strPhoneNo, "6999999", "Pending Address from GUI", strBirthday, "0", "Pending NokName from Gui",
                                        "Pending NokPhoneNum from GUI"};

                                for(int attributeNo=0; attributeNo<userAttributes.length; attributeNo++){
                                    addToHashMap(registerUser, userAttributes[attributeNo], regAttributes[attributeNo]);
                                }
                                DatabaseReference newUserRef = FirebaseDatabase.getInstance(realtimedatabase).getReference("User").push();
                                newUserRef.updateChildren(registerUser);
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } else{
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
    //            Error when connecting to rtdb
                Toast.makeText(RegisterActivity.this, "Database Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addToHashMap(HashMap<String, Object> hashMap, String attributeType, String attribute) {
        hashMap.put(attributeType, attribute);
    }

    private Boolean CredentialsCheck(String strEmail, String strFirstName, String strLastName, Integer intGender, String strPass,
                                    String strRePass, String strPhoneNo, String strBirthday){

        boolean credentialCheck = true;
        // no string is blank
        Log.d("Register", strEmail);
        if (TextUtils.isEmpty(strEmail)){
            email.setError("Email is not filled!");
            credentialCheck = false;
        }
        if (TextUtils.isEmpty(strFirstName)){
            firstName.setError("First Name is not filled!");
            credentialCheck = false;
        }
        if (TextUtils.isEmpty(strLastName)){
            lastName.setError("Last Name is not filled!");
            credentialCheck = false;
        }
        if (TextUtils.isEmpty(strPass)) {
//            pass.setError("Password is not filled!");
            pass.setError("Password should contain the following:\n1. 6 to 12 characters\n2. alphanumeric\n3. Special Character !.@#$%^-+");
            credentialCheck = false;
        }else{
//            String passwordChecker = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8, 20}$";
            if (!validPassword(strPass)){
                pass.setError("Password should contain the following:\n1. 6 to 12 characters\n2. alphanumeric\n3. Special Character !.@#$%^-+");
                credentialCheck = false;
            }

        }
        if (TextUtils.isEmpty(strRePass)){
            rePass.setError("Re-enter Password is not filled!");
            credentialCheck = false;

            if (!TextUtils.equals(strPass, strRePass)){
                rePass.setError("Password is not the same!");
                credentialCheck = false;
            }
        }
        if (TextUtils.isEmpty(strPhoneNo)){
            phoneNo.setError("Phone Number is not filled!\neg. 91234567");
//            birthday.setError("Phone Number should be filled as 91234567");
            credentialCheck = false;
            if (!TextUtils.isDigitsOnly(strPhoneNo) && (strPhoneNo.length()!=8)){
                credentialCheck = false;
            }
        }
        if (TextUtils.isEmpty(strBirthday)){
            birthday.setError("Brithday is not filled!\n eg. 01-02-1930");
//            birthday.setError("Birthday should be filled as DD-MM-YYYY\neg. 01-02-1930");
            credentialCheck = false;
        } else{
            if (strBirthday.length()==10){
                int day = Integer.valueOf(strBirthday.substring(0,2));
                int month = Integer.valueOf(strBirthday.substring(3,5));
                int year = Integer.valueOf(strBirthday.substring(6,10));

                if (strBirthday.charAt(2)=='-' && strBirthday.charAt(5)=='-'){
                    if (year > 1900 && year < 2022){
                        if (month >= 1 && month <= 12){
                            if (month == 1 || month == 3 || month == 5 || month == 6 || month == 8 || month == 10 || month == 12){
                                if (day < 1 || day > 31){
                                    credentialCheck = false;
                                }
                            }else if (month == 4 || month == 7 || month == 9 || month == 11){
                                if (day < 1 || day > 30){
                                    credentialCheck = false;
                                }
                            }else{
                                if (year%4==0){
                                    if (day < 1 || day > 29){
                                        credentialCheck = false;
                                    }
                                }else{
                                    if (day < 1 || day > 28){
                                        credentialCheck = false;
                                    }
                                }
                            }
                        }else{
                            credentialCheck = false;
                        }
                    }
                    else{
                        credentialCheck = false;
                    }
                }else{
                    credentialCheck = false;
                }
            }else{
                credentialCheck = false;
            }
        }
//        for userid
//        if (TextUtils.isEmpty(strUserId)){
//            email.setError("UserId is is not filled!");
//            credentialCheck = false;
//        }
        // check if strEmail in database

        return credentialCheck;

    }


//    Ensure password has 6-12 chars long, alphanumeric, and contain 1 special character
    private boolean validPassword(String regPass) {
        boolean passwordFlag = true;
        String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", ".", ","};
        if (regPass.length()<6 || regPass.length()>12){
            passwordFlag = false;
        }else{
            int specialCount = 0;
            int capsCount = 0;
            int smallCount = 0;
            int numCount = 0;
            int spaceCount = 0;
            for(int letter = 0; letter<regPass.length(); letter++){
                char passwordLetter = regPass.charAt(letter);
                if(Character.isUpperCase(passwordLetter)){
                    capsCount++;
                }
                if(Character.isLowerCase(passwordLetter)){
                    smallCount++;
                }
                if(Character.isDigit(passwordLetter)){
                    numCount++;
                }
                if(passwordLetter>=33&&passwordLetter<=46||passwordLetter==64){
                    specialCount++;
                }
                if(Character.isSpaceChar(passwordLetter)){
                    spaceCount++;
                }
            }
            if (specialCount ==0 || capsCount ==0 || smallCount ==0 || numCount ==0 || spaceCount !=0){
                passwordFlag = false;
            }
        }

        return passwordFlag;
    }

//    Retrieve list of admins from database
    private void attributeList(DatabaseReference dbRef, String dbAttribute){
        Query query = dbRef.orderByChild(dbAttribute);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<String> tempAttList = new ArrayList<>();
                    for(DataSnapshot attributeCounter : snapshot.getChildren()){
                        String attributeVal = attributeCounter.child(dbAttribute).toString();
                        tempAttList.add(attributeVal);
                    }
                    attList = tempAttList;

                }
                else{
                    Toast.makeText(RegisterActivity.this, "There are no Caretakers in the database", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error connecting to the database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
