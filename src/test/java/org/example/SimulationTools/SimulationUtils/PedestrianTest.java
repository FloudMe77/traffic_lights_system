package org.example.SimulationTools.SimulationUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedestrianTest {
    private Pedestrian pedestrian;
    private Road crossroad;

    @BeforeEach
    void setUp() {
        crossroad = new Road(Direction.NORTH, List.of(0,0,0,0,0,1),true);
        pedestrian = new Pedestrian("P1", crossroad, 5);
    }

    @Test
    void testConstructor() {
        assertEquals("P1", pedestrian.getId());
        assertEquals(crossroad, pedestrian.getCrossroad());
        assertEquals(5, pedestrian.getStepCountAdded());
    }
}
