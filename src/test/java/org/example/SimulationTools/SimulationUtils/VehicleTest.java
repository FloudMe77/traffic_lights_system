package org.example.SimulationTools.SimulationUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {
    private Vehicle vehicle;
    private Lane lane;

    @BeforeEach
    void setUp() {
        lane = new Lane(Direction.NORTH, List.of(Direction.SOUTH));
        vehicle = new Vehicle("V1", Direction.NORTH, Direction.SOUTH, 5, lane);
    }

    @Test
    void testGetStartDirection() {
        assertEquals(Direction.NORTH, vehicle.getStartDirection());
    }

    @Test
    void testGetEndDirection() {
        assertEquals(Direction.SOUTH, vehicle.getEndDirection());
    }

    @Test
    void testGetId() {
        assertEquals("V1", vehicle.getId());
    }

    @Test
    void testGetStepCountAdded() {
        assertEquals(5, vehicle.getStepCountAdded());
    }

    @Test
    void testGetOnLane() {
        assertEquals(lane, vehicle.getOnLane());
    }

    @Test
    void testToString() {
        assertEquals("id: V1 from: NORTH to: SOUTH", vehicle.toString());
    }
}