package com.example.draw4brains.model;

public class User {
    private String userName;
    private String gender;
    private Time birthday = new Time();
    private String phoneNo;
    private String emailAddress;

    private static User userInstance = new User();

    /**
     * Constructor of User.
     * Made private.
     */
    private User() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Time getBirthday() {
        return birthday;
    }

    public void setBirthday(Time birthday) {
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

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
