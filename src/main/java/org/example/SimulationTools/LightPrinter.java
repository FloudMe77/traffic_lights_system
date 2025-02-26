package org.example.SimulationTools;

import org.example.SimulationTools.SimulationUtils.Lane;
import org.example.SimulationTools.SimulationUtils.Road;

import java.util.List;

public class LightPrinter {
    // wypisuje stan świateł na skrzyżowaniu
    public void printLight(List<Road> roads) {
        for (Road road : roads) {
            for (Lane lane : road.getLanes()) {
                System.out.print(lane.getStartRoad() +": " + lane.toStringLight() + " ");
            }
            if (road.isRightArrow()) {
                System.out.println("IsRightArrowOn: " + road.isRightArrowOn() + " IsPedestrainLightOn: " + road.isPedestrianLightOn());
            }

        }
        System.out.println();
    }

}
