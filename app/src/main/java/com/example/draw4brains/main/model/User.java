package com.example.draw4brains.main.model;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String userName;
    private String gender;
    private String birthday;
    private String phoneNo;
    private String houseNo;
    private String address;
    private String emailAddress;
    private String userPassword;
    private String caretaker_email;
    private Boolean is_admin;
    private int score;
    private String scores;
    private int totalScore;
    private int number_played;
    private String userID;
    private String nokName;
    private String nokNum;
    public Score userScore;

    private static User userInstance = new User();

    /**
     * Constructor of User.
     * Made private.
     */
    public User() {
    }

    public User(String emailAddress){
        Log.d("UserDEBUG", "Accessing Firebase");
        FirebaseDatabase userDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference userRef = userDb.getReference("User");
        Query query = userRef.orderByChild("userEmail").equalTo(emailAddress);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("TAG", snapshot.getKey());
//                Log.d("Snapshot Children", snapshot.getChildren().toString());
//                Log.d("Snapshot String", snapshot.toString());
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String userUid = ds.getKey();
                    setAddress(snapshot.child(userUid).child("userAddress").getValue().toString());
                    setCaretaker_email(snapshot.child(userUid).child("userAdmin").getValue().toString());
                    if (TextUtils.isEmpty(getCaretaker_email())){
                        setIs_admin(false);
                    }
                    else{
                        setIs_admin(true);
                    }
                    setBirthday(snapshot.child(userUid).child("userBirthday").getValue().toString());
                    setEmailAddress(snapshot.child(userUid).child("userEmail").getValue().toString());
                    setGender(snapshot.child(userUid).child("userGender").getValue().toString());
                    setHouseNo(snapshot.child(userUid).child("userHouseNum").getValue().toString());
                    setNokName(snapshot.child(userUid).child("userNokName").getValue().toString());
                    setNokNum(snapshot.child(userUid).child("userNokNum").getValue().toString());
                    setNumber_played(Integer.parseInt(snapshot.child(userUid).child("userNumGamesPlayed").getValue().toString()));
                    setPhoneNo(snapshot.child(userUid).child("userPhoneNum").getValue().toString());
                    setscore(Integer.parseInt(snapshot.child(userUid).child("userScore").getValue().toString()));
                    setUserID(userUid);
                    setUserName(emailAddress);
                    setUserPassword(snapshot.child(userUid).child("userPassword").getValue().toString());
                    setTotalScore(getscore(), getNumber_played());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public User(String userName, String gender, String phoneNo, String emailAddress, String caretaker_email,Boolean Is_Admin, int score){
        setUserName(userName);
        setGender(gender);
        setBirthday(birthday);
        setPhoneNo(phoneNo);
        setEmailAddress(emailAddress);
        setCaretaker_email(caretaker_email);
        setIs_admin(Is_Admin);
        setscore(score);
    }

    public User(String userName, String gender, String phoneNo, String emailAddress, String caretaker_email,Boolean Is_Admin, String scores, int number_played){
        setUserName(userName);
        setGender(gender);
        //setBirthday(birthday);
        setPhoneNo(phoneNo);
        setEmailAddress(emailAddress);
        setCaretaker_email(caretaker_email);
        setIs_admin(Is_Admin);

        setNumber_played(number_played);

        setTotalScore(Integer.parseInt(scores), number_played);
//        setTotalScore(scores);

    }

    /**
     * Gets instance of User
     * @return instance of User
     */
    public static User getInstance() {
        return userInstance;
    }
    public static void setInstance(User user){
        userInstance = user;
    }

    public String getUserID(){return userID;}

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCaretaker_email (String caretaker_email) {this.caretaker_email = caretaker_email;}

    public String getCaretaker_email () {return caretaker_email;}

    public Boolean getIs_admin(){return is_admin;}

    public void setIs_admin(Boolean is_admin){this.is_admin = is_admin;}

    public int getscore(){return score;}

    public void setscore(int score){this.score = score;}



    public int getTotalScore(){return totalScore;}

    public void setTotalScore(int userScore, int gamesPlayed){this.totalScore = userScore*gamesPlayed;}

    public int getNumber_played(){return number_played;}

    public void setNumber_played(int number_played){this.number_played = number_played;}

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", caretaker_email='" + caretaker_email + '\'' +
                '}';
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNokName() {
        return nokName;
    }

    public void setNokName(String nokName) {
        this.nokName = nokName;
    }

    public String getNokNum() {
        return nokNum;
    }

    public void setNokNum(String nokNum) {
        this.nokNum = nokNum;
    }

    public Score getScore() {
        return this.userScore;
    }

    public void setScore(Score userScore) {
        this.userScore = userScore;
    }
}
