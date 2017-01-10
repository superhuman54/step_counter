package com.kimkihwan.me.stepcounter.model;

/**
 * Created by jamie on 10/2/16.
 */

public class StepCounter {

    private String startedDate;
    private int steps;
    private double distance;
    private String updateDatetime;

    public StepCounter setStartedDate(String startedDate) {
        this.startedDate = startedDate;
        return this;
    }

    public StepCounter setSteps(int steps) {
        this.steps = steps;
        return this;
    }

    public StepCounter setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public StepCounter setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
        return this;
    }

    public String getStartedDate() {
        return startedDate;
    }

    public int getSteps() {
        return steps;
    }

    public double getDistance() {
        return distance;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }
}
