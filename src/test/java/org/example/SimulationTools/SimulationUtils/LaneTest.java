package org.example.SimulationTools.SimulationUtils;

import org.example.SimulationTools.SignalColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LaneTest {
    private Lane lane;
    private Direction startDirection;
    private List<Direction> endDirections;
    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        startDirection = Direction.NORTH;
        endDirections = List.of(Direction.EAST, Direction.SOUTH);
        lane = new Lane(startDirection, endDirections);
        vehicle1 = new Vehicle("n1",Direction.NORTH,Direction.EAST,1,lane);
        vehicle2 = new Vehicle("n2",Direction.NORTH,Direction.SOUTH,1,lane);
    }

    @Test
    void testConstructor() {
        assertEquals(startDirection, lane.getStartRoad());
        assertEquals(endDirections, lane.getEndRoad());
        assertTrue(lane.isEmpty());
        assertTrue(lane.getSignalColors().stream().allMatch(entry -> entry.getValue() == null));
    }

    @Test
    void testAddAndRetrieveVehicle() {
        lane.addVehicle(vehicle1);
        assertEquals(1, lane.getVehiclesSize());
        assertEquals(vehicle1, lane.getFirst());
    }

    @Test
    void testGetFirstAndPop() {
        lane.addVehicle(vehicle1);
        lane.addVehicle(vehicle2);
        assertEquals(vehicle1, lane.getFirstAndPop());
        assertEquals(1, lane.getVehiclesSize());
        assertEquals(vehicle2, lane.getFirst());
    }

    @Test
    void testIsEndRoad() {
        assertTrue(lane.isEndRoad(Direction.EAST));
        assertFalse(lane.isEndRoad(Direction.WEST));
    }

    @Test
    void testSetAndGetSignalColor() {
        lane.setSignalColor(Direction.EAST, SignalColor.RED);
        assertEquals(SignalColor.RED, lane.getSignalColor(Direction.EAST));
    }

    @Test
    void testSetNextSignalColor() {
        lane.setSignalColor(Direction.SOUTH, SignalColor.RED);
        lane.setNextSignalColor(Direction.SOUTH);
        assertEquals(SignalColor.RED_YELLOW, lane.getSignalColor(Direction.SOUTH));
    }

    @Test
    void testSameAllLights() {
        lane.setSignalColor(Direction.EAST, SignalColor.RED);
        lane.setSignalColor(Direction.SOUTH, SignalColor.RED);
        assertTrue(lane.sameAllLights());

        lane.setSignalColor(Direction.SOUTH, SignalColor.GREEN);
        assertFalse(lane.sameAllLights());
    }

    @Test
    void testToStringMethods() {
        assertNotNull(lane.toString());
        assertNotNull(lane.toStringLight());
    }

    @Test
    void testEqualsAndHashCode() {
        Lane sameLane = new Lane(startDirection, endDirections);
        assertEquals(lane, sameLane);
        assertEquals(lane.hashCode(), sameLane.hashCode());

        Lane differentLane = new Lane(Direction.WEST, List.of(Direction.NORTH));
        assertNotEquals(lane, differentLane);
    }
}
