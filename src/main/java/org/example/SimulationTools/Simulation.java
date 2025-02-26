package org.example.SimulationTools;

import org.example.Config;
import org.example.IOHandlers.Command;
import org.example.IOHandlers.FileReaderIterator;
import org.example.IOHandlers.JsonStepLogger;
import org.example.LightStrategy.GreedyLightMode;
import org.example.LightStrategy.ILightSystem;
import org.example.SimulationTools.SimulationUtils.*;

import java.nio.file.Paths;
import java.util.*;

public class Simulation {
    private final Map<Direction, Road> roadsMap = new HashMap<>();
    private final LightPrinter lightPrinter = new LightPrinter();

    private List<InOutPairDirection> lastAllowedDirections = new ArrayList<>();
    private List<InOutPairDirection> lastConditionDirections = new ArrayList<>();
    private final JsonStepLogger jsonStepLogger;

    private final LightChanger lightChanger;
    private final ILightSystem lightSystem;

    private int actualStepCount = 1;

    public Simulation(Config config, String outputFileName) {

        this.jsonStepLogger = new JsonStepLogger(outputFileName);
        var directions = List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
        for (var direction : directions) {
            var lineNumbers = config.lineNumbers().get(direction);
            roadsMap.put(direction, new Road(direction, lineNumbers));
        }
        this.lightSystem = new GreedyLightMode(roadsMap);
        this.lightChanger = new LightChanger(roadsMap);
    }

    public void start(String fileName) {
        String filePath = Paths.get(System.getProperty("user.dir"), fileName).toString();
        for (Command command : new FileReaderIterator(filePath)) {

            if (command.type().equals("step")) {
                System.out.println("step");
                stepAction();

            }
            if (command.type().equals("addVehicle")) {
                System.out.println("addVehicle");

                addVehicleToRoad(command);

            }
            if (command.type().equals("addPedestrian")) {
                System.out.println("addPedestrian");

                addPedestrianToRoad(command);

            }
        }
    }

    private void addPedestrianToRoad(Command command) {
        Direction startDirection = Direction.parseStringToDirection(command.startRoad());
        Road road = roadsMap.get(startDirection);
        road.addPedestrian(new Pedestrian(command.id(), road, actualStepCount));
    }

    private void addVehicleToRoad(Command command) {
        Direction startDirection = Direction.parseStringToDirection(command.startRoad());
        Direction endDirection = Direction.parseStringToDirection(command.endRoad());
        var road = roadsMap.get(startDirection);
        var lanes = road.getLanes();
        // wybirany jest najmniej oblegany pas
        var bestLane = lanes.stream()
                .filter(lane -> lane.isEndRoad(endDirection))
                .min(Comparator.comparingInt(Lane::getVehiclesSize))
                .orElse(null);
        assert bestLane != null;
        var newVehicle = new Vehicle(command.id(), startDirection, endDirection, actualStepCount, bestLane);
        bestLane.addVehicle(newVehicle);

    }

    private void stepAction() {
        List<InOutPairDirection> allowedDirections = lastAllowedDirections;
        List<InOutPairDirection> conditionDirections = lastConditionDirections;
        List<Direction> pedestrianAllowDirections = new ArrayList<>();
        if (lightSystem.shouldChangeLights(actualStepCount)) {
            updateDirectionsLists();
            pedestrianAllowDirections = lightSystem.getActualPedestrianDirections();
            if (actualStepCount == 1) {
                allowedDirections = lastAllowedDirections;
                conditionDirections = lastConditionDirections;
            }
            lightChanger.changeLight(allowedDirections);
            lightChanger.arrowChange(conditionDirections, pedestrianAllowDirections);
            lightChanger.pedestrianLightChange(pedestrianAllowDirections);
        }

        List<Vehicle> vehicleList = processVehicles(allowedDirections, conditionDirections, pedestrianAllowDirections);
        jsonStepLogger.appendStep(vehicleList);
        lightPrinter.printLight(roadsMap.values().stream().toList());
        actualStepCount++;
    }

    private void updateDirectionsLists() {
        List<List<InOutPairDirection>> pairPossibleDirection = lightSystem.getDirectionsToChange(actualStepCount);
        lastAllowedDirections = pairPossibleDirection.get(0);
        lastConditionDirections = pairPossibleDirection.get(1);
    }

    private List<Vehicle> processVehicles(List<InOutPairDirection> allowedDirections, List<InOutPairDirection> conditionDirections, List<Direction> pedestrianAllowDirections) {
        List<Direction> usedExits = new ArrayList<>();
        List<Vehicle> vehicleList = new ArrayList<>();
        List<Pedestrian> pedastrianList = new ArrayList<>();

        for (InOutPairDirection inOutPairDirection : allowedDirections) {
            processLaneVehicles(inOutPairDirection, usedExits, vehicleList);
        }

        for( Direction direction : pedestrianAllowDirections) {
            pedastrianList.addAll(roadsMap.get(direction).getAndPopPedestrians());
            usedExits.add(direction);
        }

        for (InOutPairDirection conditionDirection : conditionDirections) {
            processConditionalLaneVehicles(conditionDirection, usedExits, vehicleList);
        }

        return vehicleList;
    }

    private void processLaneVehicles(InOutPairDirection direction, List<Direction> usedExits, List<Vehicle> vehicleList) {
        for (Lane lane : roadsMap.get(direction.getStartDirection()).getLanes()) {
            if (!lane.isEmpty() && lane.getFirst().getEndDirection().equals(direction.getEndDirection())) {
                Vehicle removedVehicle = lane.getFirstAndPop();
                vehicleList.add(removedVehicle);
                lightSystem.decreaseRankedLane(actualStepCount, removedVehicle);
                usedExits.add(direction.getEndDirection());
            }
        }
    }

    private void processConditionalLaneVehicles(InOutPairDirection direction, List<Direction> usedExits, List<Vehicle> vehicleList) {
        for (Lane lane : roadsMap.get(direction.getStartDirection()).getLanes()) {
            if (isVehicleMovable(direction, lane, usedExits)) {
                Vehicle removedVehicle = lane.getFirstAndPop();
                vehicleList.add(removedVehicle);
                lightSystem.decreaseRankedLane(actualStepCount, removedVehicle);
            }
        }
    }

    private boolean isVehicleMovable(InOutPairDirection direction, Lane lane, List<Direction> usedExits) {
        return !lane.isEmpty() &&
                !usedExits.contains(lane.getFirst().getEndDirection())
                && roadsMap.get(direction.getStartDirection()).isRightArrow() &&
                lane.getFirst().getEndDirection().equals(direction.getEndDirection());
    }


}
