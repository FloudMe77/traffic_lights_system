package org.example.SimulationTools;

public enum SignalColor {
    RED,
    RED_YELLOW,
    GREEN,
    YELLOW;

    public SignalColor next(){
        return switch (this){
            case RED -> RED_YELLOW;
            case RED_YELLOW -> GREEN;
            case GREEN -> YELLOW;
            case YELLOW -> RED;
        };
    }
}
