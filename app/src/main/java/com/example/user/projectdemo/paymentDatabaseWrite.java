package com.example.user.projectdemo;

import android.support.v7.app.AppCompatActivity;

public class paymentDatabaseWrite {
    String booking_date;
    String slot;
    String timestamp;
    String amt;
    String turf;

    public paymentDatabaseWrite(){

    }

    public paymentDatabaseWrite(String amt, String booking_date, String slot, String timestamp, String turf) {
        this.booking_date = booking_date;
        this.slot = slot;
        this.timestamp = timestamp;
        this.amt=amt;
        this.turf=turf;
    }

    public String getbooking_date() {
        return booking_date;
    }

    public String getslot() {
        return slot;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public String getamt() {
        return amt;
    }

    public String getturf() {
        return turf;
    }
}