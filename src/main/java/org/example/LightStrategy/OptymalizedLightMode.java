package org.example.LightStrategy;

import org.example.SimulationTools.SimulationUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptymalizedLightMode implements ILightSystem {
    // deklaracja dostępnych ustawień świateł
    private static final List<List<InOutPairDirection>> ALLOWED_DIRECTIONS_CYCLE = List.of(
            // STRAITH LINE
            List.of(InOutPairDirection.N_S, InOutPairDirection.S_N),
            List.of(InOutPairDirection.E_W, InOutPairDirection.W_E),
            // TURN LEFT
            List.of(InOutPairDirection.W_N, InOutPairDirection.E_S, InOutPairDirection.N_W, InOutPairDirection.S_E),
            List.of(InOutPairDirection.S_W, InOutPairDirection.N_E, InOutPairDirection.W_S, InOutPairDirection.E_N)
    );
    private static final List<List<InOutPairDirection>> CONDITIONAL_DIRECTIONS_CYCLE = List.of(
            // STRAITH LINE
            List.of(InOutPairDirection.W_S, InOutPairDirection.E_N, InOutPairDirection.N_W, InOutPairDirection.S_E),
            List.of(InOutPairDirection.N_W, InOutPairDirection.S_E, InOutPairDirection.E_N, InOutPairDirection.W_S),
            // TURN LEFT
            List.of(InOutPairDirection.S_E, InOutPairDirection.N_W),
            List.of(InOutPairDirection.E_N, InOutPairDirection.W_S)
    );
    private static final List<List<Direction>> CONDITIONAL_PEDESTRIAN_CYCLE = List.of(
            // STRAITH LINE
            List.of(Direction.WEST, Direction.EAST),
            List.of(Direction.NORTH, Direction.SOUTH),
            // TURN LEFT
            List.of(),
            List.of()

    );

    private final Map<Lane, Integer> rankedLanes = new HashMap<>();

    public OptymalizedLightMode(Map<Direction, Road> roadsMap) {
        for (Road road : roadsMap.values()) {
            for (Lane lane : road.getLanes()) {
                rankedLanes.put(lane, 0);
            }
        }
    }

    @Override
    public List<List<InOutPairDirection>> getDirectionsToChange(int actualStepCount) {
        // zwiększam priorytet za każde auto na pasie
        rankedLanes.replaceAll((l, v) -> rankedLanes.get(l) + l.getVehiclesSize());
        // wybieram pas najbardziej "oblężony"
        Lane bestLane = rankedLanes.entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Wybiera wpis o największej wartości
                .map(Map.Entry::getKey)            // Pobiera klucz
                .orElse(null);               // Jeśli mapa pusta, zwraca null
        assert bestLane != null;

        // wybieram ułożenie świateł pod kątem najbardziej "oblężonego" pasa
        Direction neededFreeWay = bestLane.getFirst().getEndDirection();
        if (neededFreeWay.equals(bestLane.getFirst().getStartDirection())) {
            return switch (bestLane.getFirst().getEndDirection()) {
                case NORTH, SOUTH ->
                        List.of(ALLOWED_DIRECTIONS_CYCLE.get(1), CONDITIONAL_DIRECTIONS_CYCLE.get(1));
                case EAST, WEST ->
                        List.of(ALLOWED_DIRECTIONS_CYCLE.get(3), CONDITIONAL_DIRECTIONS_CYCLE.get(3));
            };
        }
        return switch (bestLane.getFirst().getEndDirection()) {
            case NORTH, SOUTH ->
                    List.of(ALLOWED_DIRECTIONS_CYCLE.get(0), CONDITIONAL_DIRECTIONS_CYCLE.get(0));
            case EAST, WEST ->
                    List.of(ALLOWED_DIRECTIONS_CYCLE.get(2), CONDITIONAL_DIRECTIONS_CYCLE.get(2));
        };
    }

    @Override
    public void decreaseRankedLane(int actualStepCount, Vehicle vehicle) {
        Lane lane = vehicle.getOnLane();
        rankedLanes.put(lane, rankedLanes.get(lane) - (-vehicle.getStepCountAdded() + actualStepCount + 1));
    }

    @Override
    public boolean shouldChangeLights(int actualStepCount) {
        return false;
    }

    @Override
    public List<Direction> getActualPedestrianDirections() {
        return List.of();
    }

}
