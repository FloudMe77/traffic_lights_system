package org.example.SimulationTools.SimulationUtils;

import java.util.*;

public class Road {
    private static final int NUMBER_OF_PEDESTRIAN_GET = 5;
    private final List<Lane> laneList= new ArrayList<>();
    private final Queue<Pedestrian> pedestrianQueue = new ArrayDeque<>();
    private final Direction direction;
    private boolean rightArrow = true;
    private boolean isRightArrowOn = false;
    private boolean isPedestrianLightOn = false;

    public Road(Direction direction,List<Integer> lineNumbers) {
        this.direction = direction;
        int cnt = 0;
        for (int lineNumber : lineNumbers) {
            for(int i=0;i<lineNumber;i++) {
                Lane newLane=switch (cnt){
                    case 0 -> new Lane(direction,List.of(direction.next())); // left
                    case 1 -> new Lane(direction,List.of(direction.opposite())); // forword
                    case 2 -> new Lane(direction,List.of(direction.prev())); // right
                    case 3 -> new Lane(direction,List.of(direction.next(),direction.opposite())); // left and forword
                    case 4 -> new Lane(direction,List.of(direction.opposite(),direction.prev())); // forword and right
                    case 5 -> new Lane(direction,List.of(direction.next(),direction.opposite(),direction.prev())); // left, forword and right
                    default -> throw new RuntimeException();
                };
                laneList.add(newLane);
            }
            cnt++;
        }
    }

    public Road(Direction direction,List<Integer> lineNumbers, boolean rightArrow) {
        this(direction,lineNumbers);
        this.rightArrow = rightArrow;
    }


    public void addPedestrian(Pedestrian pedestrian) {
        pedestrianQueue.add(pedestrian);
    }

    public List<Pedestrian> getPedestrians() {
        List<Pedestrian> pedestrians = new ArrayList<>();
        Iterator<Pedestrian> iterator = pedestrianQueue.iterator();

        for (int i = 0; i < NUMBER_OF_PEDESTRIAN_GET && iterator.hasNext(); i++) {
            pedestrians.add(iterator.next());
        }

        return pedestrians;
    }

    public List<Pedestrian> getAndPopPedestrians() {
        List<Pedestrian> pedestrians = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PEDESTRIAN_GET && !pedestrianQueue.isEmpty(); i++) {
            pedestrians.add(pedestrianQueue.poll());
        }
        return pedestrians;
    }

    public boolean isRightArrow() {
        return rightArrow;
    }

    public List<Lane> getLanes(){
        return laneList;
    }

    public Direction getDirection(){
        return direction;
    }
    @Override
    public String toString() {
        return laneList.toString();
    }

    public boolean isRightArrowOn() {
        return isRightArrowOn;
    }
    public void changeRightArrowOn() {
        isRightArrowOn = !isRightArrowOn;
    }
    public void changePedestrianOn() {
        isPedestrianLightOn = !isPedestrianLightOn;
    }
    public boolean isPedestrianLightOn() {
        return isPedestrianLightOn;
    }
    public int getPedestrianScore(int actualStepCount) {
        return getPedestrians()
                .stream()
                .mapToInt(pedestrian -> actualStepCount - pedestrian.getStepCountAdded() + 1)
                .sum();
    }
}
