package com.example.buddycopwow.Uploads;

public class UploadSector {
    String sectorName, sectorHeadName, lat, lan;

    public UploadSector() {
    }

    public UploadSector(String sectorName, String sectorHeadName, String lat, String lan) {
        this.sectorName = sectorName;
        this.sectorHeadName = sectorHeadName;
        this.lat = lat;
        this.lan = lan;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorHeadName() {
        return sectorHeadName;
    }

    public void setSectorHeadName(String sectorHeadName) {
        this.sectorHeadName = sectorHeadName;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    @Override
    public String toString() {
        return sectorName;
    }
}
