package org.example.SimulationTools.SimulationUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoadTest {
    private Road road;
    private Direction direction;

    @BeforeEach
    void setUp() {
        direction = Direction.NORTH;
        road = new Road(direction, List.of(1, 1, 1));
    }

    @Test
    void testConstructor() {
        assertEquals(direction, road.getDirection());
        assertEquals(3, road.getLanes().size());
    }

    @Test
    void testAddAndRetrievePedestrians() {
        Pedestrian p1 = new Pedestrian("P1", road, 3);
        Pedestrian p2 = new Pedestrian("P2", road, 5);
        road.addPedestrian(p1);
        road.addPedestrian(p2);

        List<Pedestrian> pedestrians = road.getPedestrians();
        assertEquals(2, pedestrians.size());
        assertEquals(p1, pedestrians.get(0));
    }

    @Test
    void testGetAndPopPedestrians() {
        Pedestrian p1 = new Pedestrian("P1", road, 3);
        Pedestrian p2 = new Pedestrian("P2", road, 5);
        road.addPedestrian(p1);
        road.addPedestrian(p2);

        List<Pedestrian> pedestrians = road.getAndPopPedestrians();
        assertEquals(2, pedestrians.size());
        assertTrue(road.getPedestrians().isEmpty());
    }

    @Test
    void testRightArrow() {
        assertTrue(road.isRightArrow());
        road.changeRightArrowOn();
        assertTrue(road.isRightArrowOn());
    }

    @Test
    void testPedestrianLight() {
        assertFalse(road.isPedestrianLightOn());
        road.changePedestrianOn();
        assertTrue(road.isPedestrianLightOn());
    }

    @Test
    void testGetPedestrianScore() {
        Pedestrian p1 = new Pedestrian("P1", road, 3);
        Pedestrian p2 = new Pedestrian("P2", road, 5);
        road.addPedestrian(p1);
        road.addPedestrian(p2);
        int score = road.getPedestrianScore(10);
        assertEquals(14, score);
    }
}