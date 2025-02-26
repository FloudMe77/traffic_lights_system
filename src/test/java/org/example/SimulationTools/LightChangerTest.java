package org.example.SimulationTools;

import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.InOutPairDirection;
import org.example.SimulationTools.SimulationUtils.Lane;
import org.example.SimulationTools.SimulationUtils.Road;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LightChangerTest {
    private LightChanger lightChanger;
    private Map<Direction, Road> roadsMap;
    private Road road;

    @BeforeEach
    void setUp() {
        road = new Road(Direction.NORTH, List.of(1, 1, 1));
        roadsMap = new HashMap<>();
        roadsMap.put(Direction.NORTH, road);
        lightChanger = new LightChanger(roadsMap);
    }

    @Test
    void testChangeLight() {
        InOutPairDirection pair = InOutPairDirection.fromDirections(Direction.NORTH, Direction.SOUTH);
        lightChanger.changeLight(List.of(pair));

        for (Lane lane : road.getLanes()) {
            for (Map.Entry<Direction, SignalColor> entry : lane.getSignalColors()) {
                if (entry.getKey() == Direction.SOUTH) {
                    assertEquals(SignalColor.GREEN, entry.getValue());
                } else {
                    assertEquals(SignalColor.RED, entry.getValue());
                }
            }
        }
    }

    @Test
    void testArrowChange() {
        InOutPairDirection pair = InOutPairDirection.fromDirections(Direction.NORTH, Direction.WEST);
        lightChanger.arrowChange(List.of(pair), List.of());
        assertTrue(road.isRightArrowOn());
    }

    @Test
    void testPedestrianLightChange() {
        lightChanger.pedestrianLightChange(List.of(Direction.NORTH));
        assertTrue(road.isPedestrianLightOn());
    }
}
