package com.example.user.projectdemo;

public class profileDatabaseWrite {
    String phone;
    String club;
    String age;
    String emailID;

    public profileDatabaseWrite(){

    }

    public profileDatabaseWrite(String emailID, String phone, String club, String age) {
        this.phone = phone;
        this.club = club;
        this.age = age;
        this.emailID=emailID;
    }

    public String getPhone() {
        return phone;
    }

    public String getClub() {
        return club;
    }

    public String getAge() {
        return age;
    }

    public String getEmailID() {
        return emailID;
    }
}
