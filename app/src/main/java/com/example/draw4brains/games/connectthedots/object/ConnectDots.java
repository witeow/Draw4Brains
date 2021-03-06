package com.example.draw4brains.games.connectthedots.object;

import android.widget.ImageView;

import java.io.Serializable;

public class ConnectDots implements Serializable {

    private String imageName;
    private ImageView imageDisplay;
    private String[] dotsArray;
    private Integer level;
    private String storageStringRef;

    public Node[] getNodesArray() {
        return nodesArray;
    }

    public void setNodesArray(Node[] nodesArray) {
        this.nodesArray = nodesArray;
    }

    private Node[] nodesArray;

//    public static String firebaseStorageUrl = "gs://draw4brains.appspot.com/";
//    public static String firebaseDbUrl = "https://draw4brains-default-rtdb.asia-southeast1.firebasedatabase.app/";
//    public static FirebaseStorage storage = FirebaseStorage.getInstance();
//    public static StorageReference storageReference;

    private static ConnectDots DotsInstance = new ConnectDots();

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ImageView getImageDisplay() {
        return imageDisplay;
    }

    public void setImageDisplay(ImageView imageDisplay) {
        this.imageDisplay = imageDisplay;
    }

    public String[] getDotsArray() {
        return dotsArray;
    }

    public void setDotsArray(String[] dotsArray) {
        this.dotsArray = dotsArray;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getStorageStringRef() {
        return storageStringRef;
    }

    public void setStorageStringRef(String storageStringRef) {
        this.storageStringRef = storageStringRef;
    }

    /**
     * Constructor for Schedule class.
     * Made private.
     */
    private ConnectDots() {
    }

    public ConnectDots(String gameName, String[] dotsArray,
                       Integer level, String firebaseStorageUrl) {
        // set imageName in object
        setImageName(gameName);
        setDotsArray(dotsArray);
        setLevel(level);
        setStorageStringRef(firebaseStorageUrl);
    }

    /**
     * Used to get the instance of schedule.
     *
     * @return the one schedule instance in the application
     */
    public static ConnectDots getInstance() {
        return DotsInstance;
    }

}
