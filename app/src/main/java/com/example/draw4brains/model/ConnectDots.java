package com.example.draw4brains.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectDots {
    String dotImageId;
    Image dotImage;
    private static ConnectDots DotsInstance = new ConnectDots();

    /**
     * Constructor for Schedule class.
     * Made private.
     */
    private ConnectDots() {
    }

    /**
     * Used to get the instance of schedule.
     * @return the one schedule instance in the application
     */
    public static ConnectDots getInstance() {
        return DotsInstance;
    }

    public Image getImage(){
        return dotImage;
    }
    public void setDotImage(Image dotImage){
        this.dotImage = dotImage;
    }

    /*URL iconURL = new URL("");
    // iconURL is null when not found
    ImageIcon icon = new ImageIcon(iconURL);
    Image i = icon.getImage();
     */
}
