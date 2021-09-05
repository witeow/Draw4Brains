package com.example.draw4brains.model;

public class Admin {
    private String AdminName;
    private String phoneNo;
    private String emailAddress;

    private static Admin AdminInstance = new Admin();

    /**
     * Constructor of Admin.
     * Made private.
     */
    private Admin() {
    }

    /**
     * Gets instance of Admin
     * @return instance of Admin
     */
    public static Admin getInstance() {
        return AdminInstance;
    }
    public static void setInstance(Admin Admin){
        AdminInstance = Admin;
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

    public String getAdminName() {
        return AdminName;
    }
    public void setAdminName(String AdminName) {
        this.AdminName = AdminName;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "AdminName='" + AdminName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
