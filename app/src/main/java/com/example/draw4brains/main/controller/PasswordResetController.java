package com.example.draw4brains.main.controller;

import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetController {

    FirebaseAuth auth;

    public PasswordResetController() {
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
