package com.example.draw4brains.controller;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetMgr {

    FirebaseAuth auth;

    public PasswordResetMgr() {
        auth = FirebaseAuth.getInstance();
    }

    ;

    public void resetPassword(String resetEmail) {
        if (resetEmail != null) {
            auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(resetEmail);
        }
    }

}
