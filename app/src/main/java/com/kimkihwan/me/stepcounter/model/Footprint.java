package com.kimkihwan.me.stepcounter.model;

/**
 * Created by jamie on 10/2/16.
 */

public class Footprint {

    private String startedDate;
    private double latitude;
    private double longitude;
    private String address;
    private String updatedDatetime;

    public String getStartedDate() {
        return startedDate;
    }

    public Footprint setStartedDate(String startedDate) {
        this.startedDate = startedDate;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Footprint setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Footprint setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Footprint setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getUpdatedDatetime() {
        return updatedDatetime;
    }

    public Footprint setUpdatedDatetime(String updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
        return this;
    }
}
