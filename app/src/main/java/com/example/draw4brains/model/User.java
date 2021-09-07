package com.example.draw4brains.model;

public class User {
    private String userName;
    private String gender;
    private Time birthday = new Time();
    private String phoneNo;
    private String emailAddress;
    private String caretaker_email;
    private Boolean is_admin;

    private static User userInstance = new User();

    /**
     * Constructor of User.
     * Made private.
     */
    private User() {
    }

    public User(String userName, String gender, String phoneNo, String emailAddress, String caretaker_email,Boolean Is_Admin){
        setUserName(userName);
        setGender(gender);
        //setBirthday(birthday);
        setPhoneNo(phoneNo);
        setEmailAddress(emailAddress);
        setCaretaker_email(caretaker_email);
        setIs_admin(Is_Admin);
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

    public void setCaretaker_email (String caretaker_email) {this.caretaker_email = caretaker_email;}

    public String getCaretaker_email () {return caretaker_email;}

    public Boolean getIs_admin(){return is_admin;}

    public void setIs_admin(Boolean is_admin){this.is_admin = is_admin;}

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
}
