package com.example.user.projectdemo;

public class Turf { private String turfname, turfimage, turfamenities, turfid, TurfLink, turfphone;

    public Turf() {
    }

    public Turf(String turfname, String turfimage, String turfamenities, String turfid, String TurfLink, String turfphone) {
        this.turfname = turfname;
        this.turfimage = turfimage;
        this.turfamenities = turfamenities;
        this.turfid = turfid;
        this.TurfLink = TurfLink;
        this.turfphone = turfphone;
    }

    public String getTurfname() {
        return turfname;
    }

    public void setTurfname(String turfname) {
        this.turfname = turfname;
    }

    public String getTurfimage() {
        return turfimage;
    }

    public void setTurfimage(String turfimage) {
        this.turfimage = turfimage;
    }

    public String getTurfamenities() {
        return turfamenities;
    }

    public void setTurfamenities(String turfamenities) {
        this.turfamenities = turfamenities;
    }

    public String getTurfid() {
        return turfid;
    }

    public void setTurfid(String turfid) {
        this.turfid = turfid;
    }

    public String getTurfLink() {
        return TurfLink;
    }

    public void setTurfLink(String turfLink) {
        TurfLink = turfLink;
    }

    public String getTurfphone() {
        return turfphone;
    }

    public void setTurfphone(String turfphone) {
        this.turfphone = turfphone;
    }
}
