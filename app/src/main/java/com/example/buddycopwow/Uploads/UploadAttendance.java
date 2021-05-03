package com.example.buddycopwow.Uploads;

public class UploadAttendance {
    String takenBy, takenOf, sectorName, sectorHeadName, date, time, lat, lan, status, takenOfUid;

    public UploadAttendance() {
    }

    public UploadAttendance(String takenBy, String takenOf, String sectorName,
                            String sectorHeadName, String date, String time, String lat,
                            String lan, String status, String takenOfUid) {
        this.takenBy = takenBy;
        this.takenOf = takenOf;
        this.sectorName = sectorName;
        this.sectorHeadName = sectorHeadName;
        this.date = date;
        this.time = time;
        this.lat = lat;
        this.lan = lan;
        this.status = status;
        this.takenOfUid = takenOfUid;
    }

    public String getTakenBy() {
        return takenBy;
    }

    public void setTakenBy(String takenBy) {
        this.takenBy = takenBy;
    }

    public String getTakenOf() {
        return takenOf;
    }

    public void setTakenOf(String takenOf) {
        this.takenOf = takenOf;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTakenOfUid() {
        return takenOfUid;
    }

    public void setTakenOfUid(String takenOfUid) {
        this.takenOfUid = takenOfUid;
    }
}
