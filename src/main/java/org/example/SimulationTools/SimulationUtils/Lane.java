package org.example.SimulationTools.SimulationUtils;

import org.example.SimulationTools.SignalColor;

import java.util.*;

public class Lane {
    private final Direction startRoad;
    private final List<Direction> endRoad;
    private final Queue<Vehicle> queue = new ArrayDeque<>();
    private final Map<Direction, SignalColor> signalColors = new HashMap<>();

    public Lane(Direction startRoad, List<Direction> endRoad) {
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        for(Direction direction : endRoad) {
                signalColors.put(direction, null);
        }

    }
    public Vehicle getFirst(){
        return queue.peek();
    }
    public Vehicle getFirstAndPop(){
        return queue.poll();
    }

    public void addVehicle(Vehicle vehicle){
        queue.add(vehicle);
    }

    public int getVehiclesSize(){
        return queue.size();
    }

    public List<Direction> getEndRoad() {
        return endRoad;
    }

    public Direction getStartRoad() {
        return startRoad;
    }

    public boolean isEndRoad(Direction direction){
        return endRoad.contains(direction);
    }
    @Override
    public String toString() {
        return endRoad.toString() + signalColors;
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lane lane = (Lane) o;
        return this.startRoad.equals(lane.startRoad) && this.endRoad.equals(lane.endRoad);
    }
    @Override
    public int hashCode() {
        int result = startRoad.hashCode();
        result = 31 * result + endRoad.hashCode();
        return result;
    }

    public Set<Map.Entry<Direction, SignalColor>> getSignalColors() {
        return signalColors.entrySet();
    }

    public SignalColor getSignalColor(Direction direction){
        return signalColors.get(direction);
    }

    public void setSignalColor(Direction direction, SignalColor color){
        signalColors.put(direction, color);
    }
    // ustawienie następnego koloru cyklu
    public void setNextSignalColor(Direction direction){
        signalColors.put(direction,signalColors.get(direction).next());
    }
    // czy można zastąpić przez jeden sygnalizator
    public boolean sameAllLights(){
        return signalColors.values().stream().distinct().count() <= 1;
    }
    public String toStringLight() {
        return signalColors.toString();
    }
}
