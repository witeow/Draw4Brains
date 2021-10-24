package com.example.draw4brains.main.controller;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.draw4brains.R;
import com.example.draw4brains.main.view.LoginActivity;
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

import java.util.HashMap;

public class RegisterController {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Activity registerActivity;
    private EditText email, firstName, lastName, pass, rePass, phoneNo, homePhoneNo, address, birthday, nokName, nokPhone, chooseAdmin;
    private RadioGroup genderChoice;

    Intent intent;
    private static String realtimedatabase = "https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public RegisterController() {

    }

    public void loadActivityAndElements(Activity registerActivity) {
        this.registerActivity = registerActivity;
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseDatabase.getInstance(realtimedatabase);
        this.email = registerActivity.findViewById(R.id.et_email);
        this.firstName = registerActivity.findViewById(R.id.et_first_name);
        this.lastName = registerActivity.findViewById(R.id.et_last_name);
        this.pass = registerActivity.findViewById(R.id.et_password);
        this.rePass = registerActivity.findViewById(R.id.et_password2);
        this.phoneNo = registerActivity.findViewById(R.id.et_phone);
        this.homePhoneNo = registerActivity.findViewById(R.id.et_home_phone);
        this.address = registerActivity.findViewById(R.id.et_address);
        this.birthday = registerActivity.findViewById(R.id.et_birthday);
        this.nokName = registerActivity.findViewById(R.id.et_nok_name);
        this.nokPhone = registerActivity.findViewById(R.id.et_nok_phone);
        this.chooseAdmin = registerActivity.findViewById(R.id.et_chooseadmin);
        this.genderChoice = registerActivity.findViewById(R.id.gender_group);

    }

    // Interfaces

    /**
     * Interface used to handle callBacks and manipulate Account objects
     */
    public interface onCallBackFailRegisterResult {
        void onFailure();
    }

    public void registerUser(onCallBackFailRegisterResult callBackFailRegisterResult) {


        // Initialize strings
        String strEmail = email.getText().toString();
        String strPass = pass.getText().toString();
        String strFirstName = firstName.getText().toString();
        String strLastName = lastName.getText().toString();
        String strRePass = rePass.getText().toString();
        String strPhoneNo = phoneNo.getText().toString();
        String strHomeNo = homePhoneNo.getText().toString();
        String strAddress = address.getText().toString();
        String strBirthday = birthday.getText().toString();
        String strNokName = nokName.getText().toString();
        String strNokPhone = nokPhone.getText().toString();
        String strAdmin = chooseAdmin.getText().toString();

        // Check credentials first
        Log.d("Cred", "Before");

        boolean isSuccessful = this.credentialsCheck(strEmail, strFirstName, strLastName, strPass, strRePass, strPhoneNo, strHomeNo, strAddress, strBirthday,
                strNokName, strNokPhone, strAdmin);

        if (!isSuccessful) {
            callBackFailRegisterResult.onFailure();
        }

        Log.d("Cred", "After");
        int intGender = genderChoice.getCheckedRadioButtonId();
        RadioButton gender = registerActivity.findViewById(intGender);
        String strGender = gender.getText().toString();

//            Check if email used before
        DatabaseReference userRef = db.getReference("User");
        Query query = userRef.orderByChild("userEmail").equalTo(strEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    email.setError("Email currently used! Please use a different email instead");
                    callBackFailRegisterResult.onFailure();
                } else {
                    try {
                        String encryptStrPass = AESCrypt.encrypt(strPass);
//                        encryptStrPass = encryptStrPass.substring(0, encryptStrPass.length()-3);
                        Log.d("OldPw", strPass);
                        Log.d("NewPw", encryptStrPass);
                        auth.createUserWithEmailAndPassword(strEmail, encryptStrPass).addOnCompleteListener(registerActivity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    HashMap<String, Object> registerUser = new HashMap<>();
                                    String[] userAttributes = {"userEmail", "userName", "userGender", "userPassword", "userPhoneNum",
                                            "userHouseNum", "userAddress", "userBirthday", "userScore", "userNokName", "userNokNum", "userAdmin", "userNumGamesPlayed"};

                                    String[] regAttributes = {strEmail, strFirstName + " " + strLastName, strGender, encryptStrPass,
                                            strPhoneNo, strHomeNo, strAddress, strBirthday, "0", strNokName, strNokPhone, strAdmin, "0"};

                                    for (int attributeNo = 0; attributeNo < userAttributes.length; attributeNo++) {
                                        addToHashMap(registerUser, userAttributes[attributeNo], regAttributes[attributeNo]);
                                    }
                                    DatabaseReference newUserRef = db.getReference("User").push();
                                    newUserRef.updateChildren(registerUser);
                                    String userRefId = newUserRef.getKey();
                                    updateAdmin(userRefId, strAdmin);
                                    createScore(userRefId);
                                    Toast.makeText(registerActivity, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    intent = new Intent(registerActivity, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    registerActivity.startActivity(intent);
                                    registerActivity.finish();
                                } else {
                                    Toast.makeText(registerActivity, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //            Error when connecting to rtdb
                Toast.makeText(registerActivity, "Database Connection Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void createScore(String userId) {
        String[] gameDifficulty = {"easy", "medium", "hard"};
        String gameType = "connectDots";
        String initialValue = "0";
        String gameOne = "dots";
        String gameTwo = "guess";
        String gamesPlayed = "gamesPlayed";
        DatabaseReference newUser = db.getReference("Score").child(userId).child(gameType);
        HashMap<String, Object> allScores = new HashMap<>();
        for (int difficultyNum = 0; difficultyNum < gameDifficulty.length; difficultyNum++) {
            HashMap<String, Object> singleScore = new HashMap<>();
            addToHashMap(singleScore, gameOne, initialValue);
            addToHashMap(singleScore, gameTwo, initialValue);
            addToHashMap(singleScore, gamesPlayed, initialValue);

            allScores.put(gameDifficulty[difficultyNum], singleScore);
        }
        addToHashMap(allScores, "userId", userId);
        newUser.updateChildren(allScores);
    }

    private void updateAdmin(String userRefId, String strAdmin) {
        Log.d("userRefId", userRefId);
        Log.d("strAdmin", strAdmin);
        DatabaseReference adminRef = db.getReference("Admin");
        Query query = adminRef.orderByChild("adminEmail").equalTo(strAdmin);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datas : snapshot.getChildren()) {
                        String adminUid = datas.getKey();
                        Log.d("adminUid", adminUid);
                        String userString = datas.child("arrayUserId").getValue().toString();
                        Log.d("userString", userString);
                        if (TextUtils.isEmpty(userString)) {
                            userString = userRefId;
                        } else {
                            userString += ", " + userRefId;
                        }
                        adminRef.child(adminUid).child("arrayUserId").setValue(userString);
                    }
                } else {
                    Toast.makeText(registerActivity, "No such Caretaker exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(registerActivity, "Error connecting to the database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToHashMap(HashMap<String, Object> hashMap, String attributeType, String attribute) {
        hashMap.put(attributeType, attribute);
    }

    public boolean credentialsCheck(String strEmail, String strFirstName, String strLastName, String strPass, String strRePass, String strPhoneNo, String strHomeNo,
                                    String strAddress, String strBirthday, String strNokName, String strNokPhone, String strAdmin) {
        Log.d("Cred", strEmail);
        boolean credentialCheck = true;
        // no string is blank
        boolean emptyStringCheck = emptyStringChecker(strEmail, strFirstName, strLastName, strPass, strRePass, strPhoneNo, strHomeNo,
                strAddress, strBirthday, strNokName, strNokPhone, strAdmin);

        boolean passwordCheck = passwordChecker(strPass, strRePass);

        boolean numberCheck = phoneNumberChecker(strPhoneNo, strHomeNo, strNokPhone);

        if (!emptyStringCheck || !passwordCheck || !numberCheck) {
            credentialCheck = false;
        }

        return credentialCheck;

    }

    private boolean phoneNumberChecker(String strPhoneNo, String strHomeNo, String strNokPhone) {
        String[] phones = {strPhoneNo, strHomeNo, strNokPhone};
        EditText[] editPhones = {phoneNo, homePhoneNo, nokPhone};
        String[] errorPhones = {"Phone Number", "Home Number", "Next-Of-Kin Phone Number"};
        boolean phoneCheck = true;
        for (int phone = 0; phone < phones.length; phone++) {
            if (!TextUtils.isDigitsOnly(phones[phone]) || phones[phone].length() != 8) {
                editPhones[phone].setError(errorPhones[phone] + " needs to have 8 numbers!");
                phoneCheck = false;
            } else if (!(phones[phone].charAt(0) == '6' || phones[phone].charAt(0) == '9' || phones[phone].charAt(0) == '8')) {
                editPhones[phone].setError(errorPhones[phone] + " needs to start with either a 6, 8 or 9!");
                phoneCheck = false;
            }
        }
        return phoneCheck;
    }

    private boolean passwordChecker(String strPass, String strRePass) {
        boolean passwordcheck = true;
        if (!validPassword(strPass)) {
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
        for (int textfield = 0; textfield < inputField.length; textfield++) {
            if (TextUtils.isEmpty(inputField[textfield])) {
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
        if (regPass.length() < 6 || regPass.length() > 12) {
            passwordFlag = false;
        } else {
            int specialCount = 0;
            int capsCount = 0;
            int smallCount = 0;
            int numCount = 0;
            int spaceCount = 0;
            for (int letter = 0; letter < regPass.length(); letter++) {
                char passwordLetter = regPass.charAt(letter);
                if (Character.isUpperCase(passwordLetter)) {
                    capsCount++;
                }
                if (Character.isLowerCase(passwordLetter)) {
                    smallCount++;
                }
                if (Character.isDigit(passwordLetter)) {
                    numCount++;
                }
                if (passwordLetter >= 33 && passwordLetter <= 46 || passwordLetter == 64) {
                    specialCount++;
                }
                if (Character.isSpaceChar(passwordLetter)) {
                    spaceCount++;
                }
            }
            if (specialCount == 0 || capsCount == 0 || smallCount == 0 || numCount == 0 || spaceCount != 0) {
                passwordFlag = false;
            }
        }

        return passwordFlag;
    }


}
