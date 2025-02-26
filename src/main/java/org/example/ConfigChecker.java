package org.example;

import org.example.SimulationTools.SimulationUtils.Direction;

import java.util.List;

public class ConfigChecker {
    public static void check(Config config) throws IncorrectConfigException {
        var allDirection = List.of(Direction.values());
        for(var direction : allDirection){
            if(config.lineNumbers().get(direction)==null){
                throw new IncorrectConfigException("no defined lanes on " + direction);
            }
            if(config.lineNumbers().get(direction).size()<6){
                throw new IncorrectConfigException("too faw declared lanetypes for direction: " + direction);
            }
            if(config.lineNumbers().get(direction).size()>6){
                throw new IncorrectConfigException( "too many declared lanetypes for direction: " + direction);
            }
        }
    }
}
