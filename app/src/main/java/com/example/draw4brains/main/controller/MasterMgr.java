package com.example.draw4brains.main.controller;

import com.example.draw4brains.games.connectthedots.controller.DatabaseMgr;

public class MasterMgr {

    // Will call other controllers

    public static AuthenticationMgr authenticationMgr = new AuthenticationMgr();
    public static PasswordResetMgr passwordResetMgr = new PasswordResetMgr();
    public static RegisterMgr registerMgr = new RegisterMgr();
    public static DatabaseMgr databaseMgr = new DatabaseMgr();

}
