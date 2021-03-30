package com.example.buddycopwow.Uploads;

public class UploadDuty {
    String sectorName, role, uid;

    public UploadDuty() {
    }

    public UploadDuty(String sectorName, String role, String uid) {
        this.sectorName = sectorName;
        this.role = role;
        this.uid = uid;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
