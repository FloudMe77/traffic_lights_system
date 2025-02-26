package org.example.SimulationTools.SimulationUtils;

import org.example.IOHandlers.Command;

public class Vehicle {
    private final Direction startDirection;
    private final Direction endDirection;
    private final String id;
    private final int stepCountAdded;
    private final Lane onLane;

    public Vehicle(String id, Direction start_road, Direction end_road, int stepCountAdded, Lane onLane) {
        this.startDirection = start_road;
        this.endDirection = end_road;
        this.id = id;
        this.stepCountAdded = stepCountAdded;
        this.onLane = onLane;
    }

    public Vehicle(Command command, int stepCountAdded, Lane onLane) {
        this.id = command.id();
        this.startDirection = Direction.parseStringToDirection(command.startRoad());
        this.endDirection = Direction.parseStringToDirection(command.endRoad());
        this.stepCountAdded = stepCountAdded;
        this.onLane = onLane;
    }

    public Direction getStartDirection() {
        return startDirection;
    }

    public Direction getEndDirection() {
        return endDirection;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id: " + id + " from: " + startDirection + " to: " + endDirection;
    }

    public Lane getOnLane() {
        return onLane;
    }

    public int getStepCountAdded() {
        return stepCountAdded;
    }
}
