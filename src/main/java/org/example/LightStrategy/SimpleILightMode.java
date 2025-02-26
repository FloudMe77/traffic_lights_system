package org.example.LightStrategy;

import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.InOutPairDirection;
import org.example.SimulationTools.SimulationUtils.Road;
import org.example.SimulationTools.SimulationUtils.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleILightMode implements ILightSystem {
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
    private static final int MINIMAL_PEDESTRIAN_SCORE = 5;
    private final Map<Direction, Road> roadsMap;

    public SimpleILightMode(Map<Direction, Road> roadsMap) {
        this.roadsMap = roadsMap;
    }
    private final int interval = 2;
    private int actualStepCount;

    @Override
    public List<List<InOutPairDirection>> getDirectionsToChange(int actualStepCount) {
        this.actualStepCount = actualStepCount;
        int index = (actualStepCount % (ALLOWED_DIRECTIONS_CYCLE.size() * interval)) / interval;
        return List.of(ALLOWED_DIRECTIONS_CYCLE.get(index), CONDITIONAL_DIRECTIONS_CYCLE.get(index));
    }

    @Override
    public void decreaseRankedLane(int actualStepCount, Vehicle vehicle) {
    }

    @Override
    public boolean shouldChangeLights(int actualStepCount) {
        return actualStepCount % interval == 0;
    }

    @Override
    public List<Direction> getActualPedestrianDirections() {
        List<Direction> pedestrianDirections = new ArrayList<>();
        int index = (actualStepCount % (ALLOWED_DIRECTIONS_CYCLE.size() * interval)) / interval;
        for(var direction : CONDITIONAL_PEDESTRIAN_CYCLE.get(index)) {
            if(roadsMap.get(direction).getPedestrianScore(actualStepCount)>MINIMAL_PEDESTRIAN_SCORE){
                pedestrianDirections.add(direction);
            }
        }
        return pedestrianDirections;
    }
}
