package org.example.SimulationTools.SimulationUtils;

public class Pedestrian {
    private final Road crossroad;
    private final String id;
    private final int stepCountAdded;


    public Pedestrian(String id, Road crossroad, int stepCountAdded) {
        this.crossroad = crossroad;
        this.id = id;
        this.stepCountAdded = stepCountAdded;
    }
    public Road getCrossroad() {
        return crossroad;
    }
    public String getId() {
        return id;
    }
    public int getStepCountAdded() {
        return stepCountAdded;
    }
}
