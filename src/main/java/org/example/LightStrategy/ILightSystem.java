package org.example.LightStrategy;

import org.example.SimulationTools.SimulationUtils.Direction;
import org.example.SimulationTools.SimulationUtils.InOutPairDirection;
import org.example.SimulationTools.SimulationUtils.Vehicle;

import java.util.List;

public interface ILightSystem {
    List<List<InOutPairDirection>> getDirectionsToChange(int actualStepCount);
    public void decreaseRankedLane(int actualStepCount, Vehicle vehicle);
    public boolean shouldChangeLights(int actualStepCount);
    public List<Direction> getActualPedestrianDirections();
}
