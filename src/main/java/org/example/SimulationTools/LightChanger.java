package org.example.SimulationTools;

import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.InOutPairDirection;
import org.example.SimulationTools.SimulationUtils.Road;

import java.util.List;
import java.util.Map;

public class LightChanger {
    private final Map<Direction, Road> roadsMap;

    public LightChanger(Map<Direction, Road> roadsMap) {
        this.roadsMap = roadsMap;
    }

    public void changeLight(List<InOutPairDirection> inOutPairDirections) {
        for (var road : roadsMap.values()) {
            for (var lane : road.getLanes()) {
                for (Map.Entry<Direction, SignalColor> entry : lane.getSignalColors()) {
                    Direction direction = entry.getKey();
                    SignalColor color = entry.getValue();
                    if (color == null) {
                        if (inOutPairDirections.contains(InOutPairDirection.fromDirections(lane.getStartRoad(), direction))) {
                            lane.setSignalColor(direction, SignalColor.GREEN);
                        } else {
                            lane.setSignalColor(direction, SignalColor.RED);
                        }
                    } else if (color.equals(SignalColor.RED_YELLOW)
                            || color.equals(SignalColor.YELLOW)
                            || (color.equals(SignalColor.GREEN) && !inOutPairDirections.contains(InOutPairDirection.fromDirections(lane.getStartRoad(), direction)))
                            || (color.equals(SignalColor.RED) && inOutPairDirections.contains(InOutPairDirection.fromDirections(lane.getStartRoad(), direction)))) {
                        lane.setNextSignalColor(direction);
                    }
                }
            }
        }
    }
    public void arrowChange(List<InOutPairDirection> conditionDirections, List<Direction> PedestrianDirections) {
        roadsMap.values().stream()
                .filter(Road::isRightArrow) // Najpierw sprawdzamy, czy ma strzałkę
                .filter(road -> {
                    boolean inCondition = conditionDirections.contains(
                            InOutPairDirection.fromDirections(road.getDirection(), road.getDirection().prev()))
                            && !PedestrianDirections.contains(road.getDirection().prev());
                    return road.isRightArrowOn() != inCondition; // Jeśli stan jest niezgodny, zmieniamy
                })
                .forEach(Road::changeRightArrowOn);
    }
    public void pedestrianLightChange(List<Direction> directionsOn) {
        roadsMap.values()
                .stream()
                .filter(road -> {
                    boolean inCondition = directionsOn.contains(road.getDirection());
                    return road.isPedestrianLightOn() != inCondition;
                })
                .forEach(Road::changePedestrianOn);
    }
}
