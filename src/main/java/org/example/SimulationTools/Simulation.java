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

    // start symulacji
    public void start(String fileName) {
        String filePath = Paths.get(System.getProperty("user.dir"), fileName).toString();
        // pobieranie komendy i wykonywanie ruchu
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

    // dodanie pieszego do drogi z komendy
    private void addPedestrianToRoad(Command command) {
        Direction startDirection = Direction.parseStringToDirection(command.startRoad());
        Road road = roadsMap.get(startDirection);
        road.addPedestrian(new Pedestrian(command.id(), road, actualStepCount));
    }

    // dodanie pojazdu do pasa z komendy
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
        // krok symulacji

        // przepisanie do bufora ostatniego ułożenia przejazdu na światłach
        List<InOutPairDirection> allowedDirections = lastAllowedDirections;
        List<InOutPairDirection> conditionDirections = lastConditionDirections;
        // deklaracja listy kierunków, gdzie przechodzą piesi
        List<Direction> pedestrianAllowDirections = new ArrayList<>();
        // jeżeli trzeba zmienić świtało
        if (lightSystem.shouldChangeLights(actualStepCount)) {
            // poprawiam listy
            updateDirectionsLists();
            // pobieram drogi gdzie piesi przechodzą
            pedestrianAllowDirections = lightSystem.getActualPedestrianDirections();
            // przy pierwszym krok po prostu przepisuje
            if (actualStepCount == 1) {
                allowedDirections = lastAllowedDirections;
                conditionDirections = lastConditionDirections;
            }
            // zmieniam świtła zgodnie ze zwróconymi kierunkami
            lightChanger.changeLight(allowedDirections);
            lightChanger.arrowChange(conditionDirections, pedestrianAllowDirections);
            lightChanger.pedestrianLightChange(pedestrianAllowDirections);
        }

        // przeprowadzam usuwanie samochodów z pasów i dodanie ich do listy
        List<Vehicle> vehicleList = processVehicles(allowedDirections, conditionDirections, pedestrianAllowDirections);
        // zapis do pliku
        jsonStepLogger.appendStep(vehicleList);
        // print świteł
        lightPrinter.printLight(roadsMap.values().stream().toList());
        // zwiększenie licznika kroków
        actualStepCount++;
    }

    private void updateDirectionsLists() {
        // update list
        List<List<InOutPairDirection>> pairPossibleDirection = lightSystem.getDirectionsToChange(actualStepCount);
        lastAllowedDirections = pairPossibleDirection.get(0);
        lastConditionDirections = pairPossibleDirection.get(1);
    }

    private List<Vehicle> processVehicles(List<InOutPairDirection> allowedDirections, List<InOutPairDirection> conditionDirections, List<Direction> pedestrianAllowDirections) {
        // lista kierunków, gdzie nie można warunkowo skręcić
        List<Direction> usedExits = new ArrayList<>();
        // lista aut, które przejechały
        List<Vehicle> vehicleList = new ArrayList<>();
        // lista ludzi którzy przeszli
        List<Pedestrian> pedastrianList = new ArrayList<>();

        //  najpierw samochody z głównych świateł
        for (InOutPairDirection inOutPairDirection : allowedDirections) {
            processLaneVehicles(inOutPairDirection, usedExits, vehicleList);
        }
        // potem piesi
        for( Direction direction : pedestrianAllowDirections) {
            pedastrianList.addAll(roadsMap.get(direction).getAndPopPedestrians());
            usedExits.add(direction);
        }
        // na końcu ci, co warunkowo skręcają
        for (InOutPairDirection conditionDirection : conditionDirections) {
            processConditionalLaneVehicles(conditionDirection, usedExits, vehicleList);
        }

        return vehicleList;
    }

    private void processLaneVehicles(InOutPairDirection direction, List<Direction> usedExits, List<Vehicle> vehicleList) {
        // rozpatruje pierwszy pojazd na pasie
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
        // rozpatruje pierwszy pojazd na pasie
        for (Lane lane : roadsMap.get(direction.getStartDirection()).getLanes()) {
            if (isVehicleMovable(direction, lane, usedExits)) {
                Vehicle removedVehicle = lane.getFirstAndPop();
                vehicleList.add(removedVehicle);
                lightSystem.decreaseRankedLane(actualStepCount, removedVehicle);
            }
        }
    }

    private boolean isVehicleMovable(InOutPairDirection direction, Lane lane, List<Direction> usedExits) {
        // czy może przejechać
        return !lane.isEmpty() &&
                !usedExits.contains(lane.getFirst().getEndDirection())
                && roadsMap.get(direction.getStartDirection()).isRightArrow() &&
                lane.getFirst().getEndDirection().equals(direction.getEndDirection());
    }


}
