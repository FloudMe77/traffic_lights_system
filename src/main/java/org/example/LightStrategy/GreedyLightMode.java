package org.example.LightStrategy;

import org.example.SimulationTools.SimulationUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyLightMode implements ILightSystem {
    // Symulacja każdego ruchu w celu znalezienia najlepszego efektu
    private static final List<List<InOutPairDirection>> ALLOWED_DIRECTIONS_CYCLE = List.of(
            // STRAITH LINE
            List.of(InOutPairDirection.N_S, InOutPairDirection.S_N),
            List.of(InOutPairDirection.E_W, InOutPairDirection.W_E),
            // TURN LEFT
            List.of(InOutPairDirection.W_N, InOutPairDirection.E_S, InOutPairDirection.N_W, InOutPairDirection.S_E),
            List.of(InOutPairDirection.S_W, InOutPairDirection.N_E, InOutPairDirection.W_S, InOutPairDirection.E_N),
            // MAIN ROAD
            List.of(InOutPairDirection.S_W, InOutPairDirection.S_N, InOutPairDirection.W_S), // S
            List.of(InOutPairDirection.W_N, InOutPairDirection.W_E, InOutPairDirection.N_W), // W
            List.of(InOutPairDirection.N_S, InOutPairDirection.N_E, InOutPairDirection.E_N), // N
            List.of(InOutPairDirection.E_W, InOutPairDirection.E_S, InOutPairDirection.S_E) // E
    );
    private static final List<List<InOutPairDirection>> CONDITIONAL_DIRECTIONS_CYCLE = List.of(
            // STRAITH LINE
            List.of(InOutPairDirection.W_S, InOutPairDirection.E_N, InOutPairDirection.N_W, InOutPairDirection.S_E),
            List.of(InOutPairDirection.N_W, InOutPairDirection.S_E, InOutPairDirection.E_N, InOutPairDirection.W_S),
            // TURN LEFT
            List.of(InOutPairDirection.S_E, InOutPairDirection.N_W),
            List.of(InOutPairDirection.E_N, InOutPairDirection.W_S),
            // MAIN ROAD
            List.of(InOutPairDirection.E_N, InOutPairDirection.S_E), // S
            List.of(InOutPairDirection.S_E, InOutPairDirection.W_S), // W
            List.of(InOutPairDirection.W_S, InOutPairDirection.N_W), // N
            List.of(InOutPairDirection.N_W, InOutPairDirection.E_S) // E
    );
    private static final List<List<Direction>> CONDITIONAL_PEDESTRIAN_CYCLE = List.of(
            // STRAITH LINE
            List.of(Direction.WEST, Direction.EAST),
            List.of(Direction.NORTH, Direction.SOUTH),
            // TURN LEFT
            List.of(),
            List.of(),
            // MAIN ROAD
            List.of(Direction.EAST), // S
            List.of(Direction.SOUTH), // W
            List.of(Direction.WEST), // N
            List.of(Direction.NORTH) // E

    );

    private final Map<Lane, Integer> rankedLanes = new HashMap<>();
    private final Map<Direction, Road> roadsMap;
    private List<Direction> actualPedestrianDirections = new ArrayList<>();
    private int bestIndex = 0;
    private int prevIndex = -1;
    int actualStepCount = 0;

    public GreedyLightMode(Map<Direction, Road> roadsMap) {
        this.roadsMap = roadsMap;
        for (Road road : roadsMap.values()) {
            for (Lane lane : road.getLanes()) {
                rankedLanes.put(lane, 0);
            }
        }
    }

    public boolean shouldChangeLights(int actualStepCount) {
        this.actualStepCount = actualStepCount;
        rankedLanes.replaceAll((lane, value) -> value + lane.getVehiclesSize());

        int maxScore = 0;
        int index = 0;

        for (var allowedDirections : ALLOWED_DIRECTIONS_CYCLE) {
            List<Direction> usedExits = new ArrayList<>();
            List<Direction> pedestrianAllowedDirections = new ArrayList<>();
            int score = allowedDirections.stream()
                    .mapToInt(dir -> getScoreFromAllowedDirection(dir, usedExits))
                    .sum();

            int finalIndex = index;
            score += CONDITIONAL_DIRECTIONS_CYCLE.get(index).stream()
                    .mapToInt(dir -> getScoreFromConditionDirection(dir, usedExits, CONDITIONAL_PEDESTRIAN_CYCLE.get(finalIndex), pedestrianAllowedDirections))
                    .sum();

            if (score > maxScore) {
                maxScore = score;
                bestIndex = index;
                if(!pedestrianAllowedDirections.isEmpty()) {
                    System.out.println(index+" "+pedestrianAllowedDirections);
                }
                actualPedestrianDirections = pedestrianAllowedDirections;
            }
            index++;
        }

        boolean hasChanged = (bestIndex != prevIndex);
        prevIndex = bestIndex;
        System.out.println(rankedLanes);
        return hasChanged;
    }

    @Override
    public List<List<InOutPairDirection>> getDirectionsToChange(int actualStepCount) {
        return List.of(ALLOWED_DIRECTIONS_CYCLE.get(bestIndex), CONDITIONAL_DIRECTIONS_CYCLE.get(bestIndex));
    }

    private int getScoreFromAllowedDirection(InOutPairDirection inOutPairDirection, List<Direction> usedExits) {
        return roadsMap.get(inOutPairDirection.getStartDirection()).getLanes().stream()
                .filter(lane -> !lane.isEmpty() && lane.getFirst().getEndDirection().equals(inOutPairDirection.getEndDirection()))
                .peek(lane -> usedExits.add(lane.getFirst().getEndDirection()))
                .mapToInt(rankedLanes::get)
                .sum();
    }

    private int getPedestrianScore(Direction direction) {
        return roadsMap.get(direction).getPedestrianScore(actualStepCount);
    }

    private int getScoreFromConditionDirection(InOutPairDirection conditionInOutPair,
                                               List<Direction> usedExits,
                                               List<Direction> pedestrianDirections,
                                               List<Direction> pedestrianAllowedDirections) {
        int pedestrianScore = 0;
        if (pedestrianDirections.contains(conditionInOutPair.getEndDirection())) {
            pedestrianScore = getPedestrianScore(conditionInOutPair.getEndDirection());
        }


        int carsGoScore = roadsMap.get(conditionInOutPair.getStartDirection()).getLanes().stream()
                .filter(lane -> !lane.isEmpty()
                        && lane.getFirst().getEndDirection().equals(conditionInOutPair.getEndDirection())
                        && !usedExits.contains(conditionInOutPair.getEndDirection())
                        && roadsMap.get(conditionInOutPair.getStartDirection()).isRightArrow())
                .mapToInt(rankedLanes::get)
                .sum();

        if (carsGoScore >= pedestrianScore) {
            return carsGoScore;
        } else {
            pedestrianAllowedDirections.add(conditionInOutPair.getEndDirection());
            return pedestrianScore;
        }
    }

    public void decreaseRankedLane(int actualStepCount, Vehicle vehicle) {
        Lane lane = vehicle.getOnLane();
        rankedLanes.put(lane, rankedLanes.get(lane) - (-vehicle.getStepCountAdded() + actualStepCount + 1));
    }
    
    public List<Direction> getActualPedestrianDirections() {
        return actualPedestrianDirections;
    }

}