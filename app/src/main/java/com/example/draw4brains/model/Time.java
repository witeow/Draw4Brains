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
     * //@param timestampId
     **/
    public void setTime(String time){
        //1100,0101,2000
        if(time.length() == 12) {
            min = time.substring(0,2);
            hour = time.substring(2,4);
            day = time.substring(4,6);
            month = time.substring(6,8);
            year = time.substring(8,12);
        }
        //2359
        else if(time.length() == 4){
            hour = time.substring(0,2);
            min = time.substring(2,4);
        }
    }
    public void setId(int timestampId) {
        this.timestampId = timestampId;
    }
}

