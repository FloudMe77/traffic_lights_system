package org.example.LightStrategy;

import org.example.SimulationTools.LightPrinter;
import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.Lane;
import org.example.SimulationTools.SimulationUtils.Road;
import org.example.SimulationTools.SimulationUtils.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GreedyLightModeTest {
    private GreedyLightMode greedyLightMode;
    private Map<Direction, Road> roadsMap;
    private Road northRoad, southRoad, eastRoad, westRoad;
    private Lane northLane, southLane;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {

        northRoad = new Road(Direction.NORTH, List.of(0,0,0,0,0,1));
        southRoad = new Road(Direction.SOUTH, List.of(0,0,0,0,0,1));
        eastRoad = new Road(Direction.EAST, List.of(0,0,0,0,0,0));
        westRoad = new Road(Direction.WEST, List.of(0,0,0,0,0,0));
        northLane = northRoad.getLanes().getFirst();
        southLane = southRoad.getLanes().getFirst();
        roadsMap = Map.of(Direction.NORTH, northRoad,
                Direction.SOUTH, southRoad,
                Direction.EAST, eastRoad,
                Direction.WEST, westRoad);
        greedyLightMode = new GreedyLightMode(roadsMap);
        vehicle = new Vehicle("V1", Direction.NORTH, Direction.SOUTH, 3, northLane);
        northLane.addVehicle(vehicle);

    }

    @Test
    void testShouldChangeLights() {
        boolean changed = greedyLightMode.shouldChangeLights(1);
        var lightPrinter = new LightPrinter();
        lightPrinter.printLight(roadsMap.values().stream().toList());
        assertTrue(changed);

    }

    @Test
    void testDecreaseRankedLane() {
        greedyLightMode.decreaseRankedLane(5, vehicle);
        assertTrue(greedyLightMode.shouldChangeLights(6));
    }

    @Test
    void testGetActualPedestrianDirections() {
        List<Direction> pedestrianDirections = greedyLightMode.getActualPedestrianDirections();
        assertNotNull(pedestrianDirections);
    }
}
