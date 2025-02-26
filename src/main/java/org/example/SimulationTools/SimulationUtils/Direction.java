package org.example.SimulationTools.SimulationUtils;

public enum Direction {
    // enum reprezentujący kierunki (dróg)
    SOUTH, NORTH, WEST, EAST;

    public static Direction parseStringToDirection(String str) {
        return switch (str) {
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            default -> throw new IllegalArgumentException("Nieznany kierunek: " + str);
        };
    }
    public Direction next(){
        return switch (this){
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case EAST -> Direction.SOUTH;
            case WEST -> Direction.NORTH;
        };
    }
    public Direction prev(){
        return switch (this){
            case NORTH -> Direction.WEST;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
            case WEST -> Direction.SOUTH;
        };
    }
    public Direction opposite(){
        return switch (this){
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;

        };
    }
}
