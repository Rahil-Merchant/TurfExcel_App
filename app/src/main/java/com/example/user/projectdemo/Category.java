package com.example.user.projectdemo;
public class Category {
    private String TurfTitle;
    private String Image;
    private String TurfAddress;
    private String TurfRating;
    private String TurfDistance;

    public Category()
    {}

    public Category(String turftitle, String image, String turfAddress, String turfRating, String turfDistance) {
        TurfTitle = turftitle;
        Image = image;
        TurfAddress = turfAddress;
        TurfRating = turfRating;
        TurfDistance = turfDistance;
    }



    public String getTurftitle() {
        return TurfTitle;
    }

    public void setTurftitle(String turftitle) {
        TurfTitle = turftitle;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTurfAddress() {
        return TurfAddress;
    }

    public void setTurfAddress(String turfAddress) {
        TurfAddress = turfAddress;
    }

    public String getTurfRating() {
        return TurfRating;
    }

    public void setTurfRating(String turfRating) {
        TurfRating = turfRating;
    }

    public String getTurfDistance() {
        return TurfDistance;
    }

    public void setTurfDistance(String turfDistance) {
        TurfDistance = turfDistance;
    }
}
