package org.example;

import org.example.SimulationTools.Simulation;
import org.example.SimulationTools.SimulationUtils.Direction;

import java.util.List;
import java.util.Map;

public class Main {
    //    public static final String DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "json", "main", "resources", "configs").toString();
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("please provide input and output json file path");
            return;
        }

        Config config = new Config(Map.of(Direction.NORTH, List.of(0, 0, 0, 0, 0, 1),
                Direction.SOUTH, List.of(0, 0, 0, 0, 0, 1),
                Direction.EAST, List.of(0, 0, 0, 0, 0, 1),
                Direction.WEST, List.of(0, 0, 0, 0, 0, 1)));
        try{
            ConfigChecker.check(config);
        } catch (IncorrectConfigException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        var simulation = new Simulation(config, args[1]);
        simulation.start(args[0]);
    }
}