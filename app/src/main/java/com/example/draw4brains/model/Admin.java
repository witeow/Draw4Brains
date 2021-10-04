package com.example.draw4brains.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin {
    private String adminName;
    private String adminPhone;
    private String adminEmail;
    private String adminPass;
    private String adminId;
    private String[] adminUserId;

    private static Admin AdminInstance = new Admin();

    public Admin(String emailAddress){
        FirebaseDatabase adminDb = FirebaseDatabase.getInstance("https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference adminRef = adminDb.getReference("Admin");
        Query query = adminRef.orderByChild("adminEmail").equalTo(emailAddress);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String adminUid = ds.getKey();
                        setAdminId(adminUid);
                        setAdminName(ds.child(adminUid).child("adminName").toString());
                        setPhoneNo(ds.child(adminUid).child("adminMobile").toString());
                        setEmailAddress(ds.child(adminUid).child("adminEmail").toString());;
                        setAdminPass(ds.child(adminUid).child("adminPassword").toString());
                        String dbArrayUser = ds.child(adminUid).child("arrayUserId").toString();
                        String[] arrayUserId = TextUtils.split(dbArrayUser, ",");
                        setAdminUserId(arrayUserId);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public String[] getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String[] adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
