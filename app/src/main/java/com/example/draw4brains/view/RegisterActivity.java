package com.example.draw4brains.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class RegisterActivity extends AppCompatActivity {

    private ImageButton btnRegister, btnBack;
    private RadioButton gender, maleBtn, femaleBtn;
    private EditText email, firstName, lastName, pass, rePass, phoneNo, homePhoneNo, address, birthday, nokName, nokPhone, chooseAdmin;
    private DatePickerDialog picker;
    private RadioGroup genderChoice;

    private ArrayList<String> attList = new ArrayList<>();

    private FirebaseAuth auth;
    private static String realtimedatabase = "https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance(realtimedatabase);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

//        ADD on from wt
//        String intentEmail = getIntent().getStringExtra("Stored_Email");
//        String intentFirstName = getIntent().getStringExtra("Stored_First_Name");
//        String intentLastName = getIntent().getStringExtra("Stored_Last_Name");
//        String intentPass = (getIntent().getStringExtra("Stored_Password"));
//        String intentRePass = (getIntent().getStringExtra("Stored_Re_Password"));
//        String intentPhone = (getIntent().getStringExtra("Stored_Phone"));
//        String intentHome = (getIntent().getStringExtra("Stored_Home"));
//        String intentAddress = (getIntent().getStringExtra("Stored_Address"));
//        String intentBirthday = (getIntent().getStringExtra("Stored_Birthday"));
//        String intentNokName= (getIntent().getStringExtra("Stored_Nok_Name"));
//        String intentNokPhone = (getIntent().getStringExtra("Stored_Nok_Phone"));

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
        phoneNo = findViewById(R.id.et_phone);
        homePhoneNo = findViewById(R.id.et_home_phone);
        address = findViewById(R.id.et_address);
        birthday = findViewById(R.id.et_birthday);
        nokName = findViewById(R.id.et_nok_name);
        nokPhone = findViewById(R.id.et_nok_phone);
        chooseAdmin = findViewById(R.id.et_chooseadmin);

        birthday.setInputType(InputType.TYPE_NULL);
        chooseAdmin.setInputType(InputType.TYPE_NULL);

        auth = FirebaseAuth.getInstance();

        maleBtn = findViewById(R.id.btn_male);
        femaleBtn = findViewById(R.id.btn_female);


        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        if(admin_email!=null){
            email.setText(getIntent().getStringExtra("Stored_Email"));
            firstName.setText(getIntent().getStringExtra("Stored_First_Name"));
            lastName.setText(getIntent().getStringExtra("Stored_Last_Name"));
            Log.d("gender in tent", getIntent().getStringExtra("Stored_Gender"));
            if (TextUtils.equals(getIntent().getStringExtra("Stored_Gender"), "Male")){
                maleBtn.setChecked(true);
                femaleBtn.setChecked(false);
            }else{
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

        chooseAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadUsers = new Intent(RegisterActivity.this, ChooseAdminActivity.class);
                loadUsers.putExtra("Stored_Email",email.getText().toString());
                loadUsers.putExtra("Stored_First_Name",firstName.getText().toString());
                loadUsers.putExtra("Stored_Last_Name",lastName.getText().toString());
                Integer intGender = genderChoice.getCheckedRadioButtonId();
                gender = findViewById(intGender);
                loadUsers.putExtra("Stored_Gender",gender.getText().toString());
                Log.d("gender before change", gender.getText().toString());
                loadUsers.putExtra("Stored_Password",pass.getText().toString());
                loadUsers.putExtra("Stored_Re_Password",rePass.getText().toString());
                loadUsers.putExtra("Stored_Phone",phoneNo.getText().toString());
                loadUsers.putExtra("Stored_Home",homePhoneNo.getText().toString());
                loadUsers.putExtra("Stored_Address", address.getText().toString());
                loadUsers.putExtra("Stored_Birthday",birthday.getText().toString());
                loadUsers.putExtra("Stored_Nok_Name",nokName.getText().toString());
                loadUsers.putExtra("Stored_Nok_Phone",nokPhone.getText().toString());
                startActivity(loadUsers);

//                startActivity(new Intent(RegisterActivity.this, ChooseAdminActivity.class));
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

                Integer intGender = genderChoice.getCheckedRadioButtonId();
                gender = findViewById(intGender);
                String regGender = gender.getText().toString();
                Log.d("register", "gender: " + regGender);

                String regPass = pass.getText().toString();
                String regRePass = rePass.getText().toString();
                String regPhoneNo = phoneNo.getText().toString();
                String regHomeNo = homePhoneNo.getText().toString();
                String regAddress = address.getText().toString();
                String regBirthday = birthday.getText().toString();
                String regNokName = nokName.getText().toString();
                String regNokPhone = nokPhone.getText().toString();
                String regAdmin = chooseAdmin.getText().toString();


                Boolean checkOutcome = CredentialsCheck(regEmail, regFirstName, regLastName, regPass, regRePass, regPhoneNo,
                        regHomeNo, regAddress, regBirthday,regNokName,regNokPhone, regAdmin);
                if (checkOutcome){
                    registerUser(regEmail, regFirstName, regLastName, regGender, regPass, regRePass, regPhoneNo,
                            regHomeNo, regAddress, regBirthday,regNokName,regNokPhone, regAdmin);
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

    private void registerUser(String strEmail, String strFirstName, String strLastName, String strGender, String strPass,
                              String strRePass, String strPhoneNo, String strHomeNo, String strAddress, String strBirthday,
                              String strNokName, String strNokPhone, String strAdmin){
//            Check if email used before
        DatabaseReference userRef = db.getReference("User");
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
                                        "userHouseNum", "userAddress", "userBirthday", "userScore",  "userNokName", "userNokNum", "userAdmin"};

                                String[] regAttributes = {strEmail, strFirstName + " " + strLastName, strGender, strPass,
                                        strPhoneNo, strHomeNo, strAddress, strBirthday, "0", strNokName, strNokPhone, strAdmin};

                                for(int attributeNo=0; attributeNo<userAttributes.length; attributeNo++){
                                    addToHashMap(registerUser, userAttributes[attributeNo], regAttributes[attributeNo]);
                                }
                                DatabaseReference newUserRef = FirebaseDatabase.getInstance(realtimedatabase).getReference("User").push();
                                newUserRef.updateChildren(registerUser);
                                String userRefId = newUserRef.getKey();
                                updateAdmin(userRefId, strAdmin);
//                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                                finish();
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

    private void updateAdmin(String userRefId, String strAdmin) {
        Log.d("userRefId", userRefId);
        Log.d("strAdmin", strAdmin);
        DatabaseReference adminRef = db.getReference("Admin");
        Query query =adminRef.orderByChild("adminEmail").equalTo(strAdmin);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot datas: snapshot.getChildren()){
                        String adminUid =datas.getKey();
                        Log.d("adminUid", adminUid);
                        String userString = datas.child("arrayUserId").getValue().toString();
                        Log.d("userString", userString);
                        if (TextUtils.isEmpty(userString)){
                            userString = userRefId;
                        }else{
                            userString += ", " + userRefId;
                        }
                        adminRef.child(adminUid).child("arrayUserId").setValue(userString);
                    }
//                    String userString = snapshot.child("arrayUserId").toString();
//                    Log.d("userString", userString);
//                    String adminUid = snapshot.getKey();
//                    Log.d("adminUid", adminUid);
//                    if (TextUtils.isEmpty(userString)){
//                        userString = userRefId;
//                    }else{
//                        userString += ", " + userRefId;
//                    }
//                    db.getReference(adminUid).child("arrayUserId").setValue(userString).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Log.d("Update", "Database has been successfully updated!");
//                        }
//                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "No such Caretaker exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error connecting to the database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToHashMap(HashMap<String, Object> hashMap, String attributeType, String attribute) {
        hashMap.put(attributeType, attribute);
    }

    private Boolean CredentialsCheck(String strEmail, String strFirstName, String strLastName, String strPass,
                                     String strRePass, String strPhoneNo, String strHomeNo, String strAddress, String strBirthday,
                                     String strNokName, String strNokPhone, String strAdmin){

        boolean credentialCheck = true;
        // no string is blank
        boolean emptyStringCheck = emptyStringChecker(strEmail,  strFirstName,  strLastName, strPass, strRePass,  strPhoneNo,  strHomeNo,
                strAddress,  strBirthday, strNokName,  strNokPhone,  strAdmin);
        
        boolean passwordCheck = passwordChecker(strPass, strRePass);
        
        boolean numberCheck = phoneNumberChecker(strPhoneNo, strHomeNo, strNokPhone);

        if (!emptyStringCheck || !passwordCheck || !numberCheck){
            credentialCheck = false;
        }

        return credentialCheck;

    }

    private boolean phoneNumberChecker(String strPhoneNo, String strHomeNo, String strNokPhone) {
        String[] phones = {strPhoneNo, strHomeNo, strNokPhone};
        EditText[] editPhones = {phoneNo, homePhoneNo, nokPhone};
        String[] errorPhones = {"Phone Number", "Home Number", "Next-Of-Kin Phone Number"};
        boolean phoneCheck = true;
        for(int phone = 0; phone<phones.length; phone++){
            if (!TextUtils.isDigitsOnly(phones[phone]) || phones[phone].length()!=8){
                editPhones[phone].setError(errorPhones[phone] + " needs to have 8 numbers!");
                phoneCheck = false;
            }else if (!(phones[phone].charAt(0)== '6' || phones[phone].charAt(0)== '9' || phones[phone].charAt(0)== '8')){
                editPhones[phone].setError(errorPhones[phone] + " needs to start with either a 6, 8 or 9!");
                phoneCheck = false;
            }
        }
        return phoneCheck;
    }

    private boolean passwordChecker(String strPass, String strRePass) {
        boolean passwordcheck = true;
        if (!validPassword(strPass)){
            pass.setError("Password should contain the following:\n1. 6 to 12 characters\n2. alphanumeric\n3. Special Character !.@#$%^-+");
            passwordcheck = false;
        }
        if (!TextUtils.equals(strPass, strRePass)) {
            rePass.setError("Password is not the same!");
            passwordcheck = false;
        }
        return passwordcheck;
    }

    private boolean emptyStringChecker(String strEmail, String strFirstName, String strLastName, String strPass, String strRePass, String strPhoneNo,
                                       String strHomeNo, String strAddress, String strBirthday, String strNokName, String strNokPhone, String strAdmin) {
        String[] inputField = {strEmail, strFirstName, strLastName, strPass, strRePass, strPhoneNo, strHomeNo, strAddress, strBirthday, strNokName, strNokPhone, strAdmin};
        String[] errorVariable = {"Email", "First Name", "Last Name", "Password", "Re-Type Password", "Phone Number", "Home Number", "Address", "Birthday", "Next-Of-Kin Name",
                "Next-Of-Kin Phone Number", "Caretaker"};
        EditText[] inputText = {email, firstName, lastName, pass, rePass, phoneNo, homePhoneNo, address, birthday, nokName, nokPhone, chooseAdmin};
        String emptyStringError = " is not filled!";
        
        boolean stringChecker = true;
        for(int textfield = 0; textfield<inputField.length; textfield++){
            if(TextUtils.isEmpty(inputField[textfield])){
                inputText[textfield].setError(errorVariable[textfield] + emptyStringError);
                stringChecker = false;
            }
        }
        return stringChecker;
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

////    Retrieve list of admins from database
//    private void attributeList(DatabaseReference dbRef, String dbAttribute){
//        Query query = dbRef.orderByChild(dbAttribute);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    ArrayList<String> tempAttList = new ArrayList<>();
//                    for(DataSnapshot attributeCounter : snapshot.getChildren()){
//                        String attributeVal = attributeCounter.child(dbAttribute).toString();
//                        tempAttList.add(attributeVal);
//                    }
//                    attList = tempAttList;
//
//                }
//                else{
//                    Toast.makeText(RegisterActivity.this, "There are no Caretakers in the database", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(RegisterActivity.this, "Error connecting to the database", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
