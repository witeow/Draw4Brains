package com.example.draw4brains.model;
import java.util.Calendar;

public class Time {

    String year, month, day;
    String hour,min,sec;
    int timestampId;

    public Time(){

    }

    public int getId() {
        return timestampId;
    }

    /**
     * Setter for id of Time class.
     * @param timestampId
     */
    public void setId(int timestampId) {
        this.timestampId = timestampId;
    }
}

