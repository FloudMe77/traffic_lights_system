package org.example.SimulationTools.SimulationUtils;

import java.util.*;

public class Road {
    // reprezentuje jedną z drug do skrzyżowania

    // liczba ludzi którzy przechodzą w ciągu jednego zielonego światła
    private static final int NUMBER_OF_PEDESTRIAN_GET = 5;
    // lista pasów, które są na tej drodze
    private final List<Lane> laneList= new ArrayList<>();
    // kolejka osób do przejścia
    private final Queue<Pedestrian> pedestrianQueue = new ArrayDeque<>();
    // z której strony idzie droga
    private final Direction direction;
    // czy sygnalizator ma strzałke warunkową
    private boolean rightArrow = true;
    // czy strzałka jest włączona
    private boolean isRightArrowOn = false;
    // czy zapalone jest światło dla pieszych
    private boolean isPedestrianLightOn = false;

    public Road(Direction direction,List<Integer> lineNumbers) {
        this.direction = direction;
        int cnt = 0;
        for (int lineNumber : lineNumbers) {
            for(int i=0;i<lineNumber;i++) {
                // rozszyfrowanie danych z config, każdy numer reprezentuje jaka jest możliwość skrętu z tego pasa
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

    // pobieram ludzi oczekujących na przejście
    public List<Pedestrian> getPedestrians() {
        List<Pedestrian> pedestrians = new ArrayList<>();
        Iterator<Pedestrian> iterator = pedestrianQueue.iterator();

        for (int i = 0; i < NUMBER_OF_PEDESTRIAN_GET && iterator.hasNext(); i++) {
            pedestrians.add(iterator.next());
        }

        return pedestrians;
    }

    // pobieram ludzi oczekujących na przejście i usuwam ich z kolejki
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
    // obliczam wartość priorytetu przejścia przez pasy w trakcie jednego zielonego świtła
    public int getPedestrianScore(int actualStepCount) {
        return getPedestrians()
                .stream()
                .mapToInt(pedestrian -> actualStepCount - pedestrian.getStepCountAdded() + 1)
                .sum();
    }
}
