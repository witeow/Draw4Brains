package com.example.draw4brains.model;

public class Admin {
    private String adminName;
    private String adminPhone;
    private String adminEmail;
    private String adminPass;

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
        return adminPhone;
    }
    public void setPhoneNo(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getEmailAddress() {
        return adminEmail;
    }
    public void setEmailAddress(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminName() {
        return adminName;
    }
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "AdminName='" + adminName + '\'' +
                ", phoneNo='" + adminPhone + '\'' +
                ", emailAddress='" + adminEmail + '\'' +
                '}';
    }
}
