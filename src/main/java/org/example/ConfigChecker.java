package org.example;

import org.example.SimulationTools.SimulationUtils.Direction;

import java.util.List;

public class ConfigChecker {
    public static void check(Config config) throws IncorrectConfigException {
        var allDirection = List.of(Direction.values());
        for(var direction : allDirection){
            // jeżeli nie ma zadeklarowanych pasów
            if(config.lineNumbers().get(direction)==null){
                throw new IncorrectConfigException("no defined lanes on " + direction);
            }
            // nie wypełniono liczby wszystkich rodzajów
            if(config.lineNumbers().get(direction).size()<6){
                throw new IncorrectConfigException("too faw declared lanetypes for direction: " + direction);
            }
            // wypełniono za dużo rodzajów
            if(config.lineNumbers().get(direction).size()>6){
                throw new IncorrectConfigException( "too many declared lanetypes for direction: " + direction);
            }
        }
    }
}
