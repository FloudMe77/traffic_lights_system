package org.example.IOHandlers;

import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.Lane;
import org.example.SimulationTools.SimulationUtils.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonStepLoggerTest {
    private JsonStepLogger jsonStepLogger;
    private List<Vehicle> vehicles;
    private static final String FILE_PATH = "test.json";

    @BeforeEach
    void setUp() {
        jsonStepLogger = new JsonStepLogger(FILE_PATH);
        var startDirection = Direction.NORTH;
        var endDirections = List.of(Direction.EAST, Direction.SOUTH);
        var lane = new Lane(startDirection, endDirections);
        vehicles = List.of(new Vehicle("n1", Direction.NORTH,Direction.EAST,1,lane), new Vehicle("n2",Direction.NORTH,Direction.SOUTH,1,lane));
    }

    @Test
    void testAppendStepCreatesJsonFile() throws IOException {
        jsonStepLogger.appendStep(vehicles);
        assertTrue(Files.exists(Paths.get(FILE_PATH)));
    }

    @Test
    void testAppendStepWritesCorrectData() throws IOException {
        jsonStepLogger.appendStep(vehicles);
        String jsonContent = Files.readString(Paths.get(FILE_PATH));
        assertTrue(jsonContent.contains("n1"));
        assertTrue(jsonContent.contains("n2"));
    }

    @Test
    void testReadStepStatusesHandlesEmptyFile() throws IOException {
        Files.writeString(Paths.get(FILE_PATH), "");
        List<Map<String, List<String>>> steps = jsonStepLogger.readStepStatuses();
        assertTrue(steps.isEmpty());
    }
}

