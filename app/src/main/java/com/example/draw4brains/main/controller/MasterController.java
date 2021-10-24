package com.example.draw4brains.main.controller;

public class MasterController {

    // Will call other controllers

    public static AuthenticationController authenticationController = new AuthenticationController();
    public static PasswordResetController passwordResetController = new PasswordResetController();
    public static RegisterController registerController = new RegisterController();

}
