package com.example.buddycopwow.Uploads;

public class UploadFir {
    String firNo, victimName, description, place, lat, lan, witness;

    public UploadFir() {
    }

    public UploadFir(String firNo, String victimName, String description, String place,
                     String lat, String lan, String witness) {
        this.firNo = firNo;
        this.victimName = victimName;
        this.description = description;
        this.place = place;
        this.lat = lat;
        this.lan = lan;
        this.witness = witness;
    }

    public String getFirNo() {
        return firNo;
    }

    public void setFirNo(String firNo) {
        this.firNo = firNo;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getWitness() {
        return witness;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }
}
