package com.example.user.projectdemo;

public class tourneylist {
    private String tourneyimg;
    private String tourneygoogledoc;

    public tourneylist() {
    }

    public tourneylist(String tourneyimg, String tourneygoogledoc) {
        this.tourneyimg = tourneyimg;
        this.tourneygoogledoc = tourneygoogledoc;
    }

    public String getTourneyimg() {
        return tourneyimg;
    }

    public void setTourneyimg(String tourneyimg) {
        this.tourneyimg = tourneyimg;
    }

    public String getTourneygoogledoc() {
        return tourneygoogledoc;
    }

    public void setTourneygoogledoc(String tourneygoogledoc) {
        this.tourneygoogledoc = tourneygoogledoc;
    }
}
