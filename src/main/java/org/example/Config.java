package org.example;

import org.example.SimulationTools.SimulationUtils.Direction;

import java.util.List;
import java.util.Map;

public record Config(Map<Direction, List<Integer>> lineNumbers) {
}
