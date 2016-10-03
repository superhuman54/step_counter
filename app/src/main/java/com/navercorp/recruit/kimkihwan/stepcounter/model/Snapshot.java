package com.navercorp.recruit.kimkihwan.stepcounter.model;

/**
 * Created by jamie on 10/2/16.
 */

public class Snapshot {

    private StepCounter stepCounter;
    private Footprint footprint;

    public StepCounter getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(StepCounter stepCounter) {
        this.stepCounter = stepCounter;
    }

    public Footprint getFootprint() {
        return footprint;
    }

    public void setFootprint(Footprint footprint) {
        this.footprint = footprint;
    }
}
