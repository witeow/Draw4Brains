package com.example.draw4brains.controller;

import com.example.draw4brains.model.ConnectDots;

import java.util.ArrayList;

public class FeedbackMgr {
    ArrayList<ConnectDots> feedbackTarget = new ArrayList<>();
    boolean like;
    int likeCount;

    public void giveFeedback(ArrayList<ConnectDots> feedbackTarget, boolean like){
        if (like == true) {
            likeCount++;
        }
        else {
            likeCount--;
        }
    }
}
