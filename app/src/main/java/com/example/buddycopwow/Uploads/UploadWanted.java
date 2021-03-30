package com.example.buddycopwow.Uploads;

public class UploadWanted {
    String img, name, crime, place;

    public UploadWanted() {
    }

    public UploadWanted(String img, String name, String crime, String place) {
        this.img = img;
        this.name = name;
        this.crime = crime;
        this.place = place;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
