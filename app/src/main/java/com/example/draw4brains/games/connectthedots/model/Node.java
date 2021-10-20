package com.example.draw4brains.games.connectthedots.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import java.io.Serializable;

public class Node implements Serializable {

    private String name;
    private int geometric_x;
    private int geometric_y;
    private int center_x;
    private int center_y;
    private int radius;
    private ImageView nodeImage;
    boolean selected;


    public Node(String name, int geometric_x, int geometric_y) {
        this.name = name;
        this.geometric_x = geometric_x;
        this.geometric_y= geometric_y;
        this.selected = false;
    }

    public Node(int name, int geometric_x, int geometric_y) {
        this.name = String.valueOf(name);
        this.geometric_x = geometric_x;
        this.geometric_y= geometric_y;
    }

    public void updateNodeWithScale(float scaleFactor) {
        int scaledX = Math.round(this.geometric_x * scaleFactor);
        int scaledY = Math.round(this.geometric_y * scaleFactor);
        this.setCenter(scaledX,scaledY);
    }

    public int getGeometric_x() {
        return geometric_x;
    }

    public int getGeometric_y() {
        return geometric_y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCenter_x() {
        return center_x;
    }


    public int getCenter_y() {
        return center_y;
    }

    public String getCenterCoordinates() {
        String coordinates = String.format("(%d,%d)", this.getCenter_x(), this.getCenter_y());
        return coordinates;
    }

    public void setCenter(int center_x, int center_y) {
        this.center_y = center_y;
        this.center_x= center_x;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ImageView getNodeImage() {
        return nodeImage;
    }

    public void setNodeImage(ImageView nodeImage) { // Not used since image is initialized during creation of notes
        this.nodeImage = nodeImage;
    }

    public float getNodeImagePosX() {
        return nodeImage.getX();
    }

    public float getNodeImagePosY() {
        return nodeImage.getY();
    }

    public void setNodeBackgroundColor(@ColorInt int color) {
        this.nodeImage.setBackgroundColor(color);
    };


}
