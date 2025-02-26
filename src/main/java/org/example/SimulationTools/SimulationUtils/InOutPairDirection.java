package org.example.SimulationTools.SimulationUtils;

import java.util.HashMap;
import java.util.Map;

public enum InOutPairDirection {
    // enum agregujący pary kierunków (wjazd, wyjazd)
    N_S(Direction.NORTH, Direction.SOUTH),
    S_N(Direction.SOUTH, Direction.NORTH),
    E_W(Direction.EAST, Direction.WEST),
    W_E(Direction.WEST, Direction.EAST),
    N_E(Direction.NORTH, Direction.EAST),
    E_N(Direction.EAST, Direction.NORTH),
    N_W(Direction.NORTH, Direction.WEST),
    W_N(Direction.WEST, Direction.NORTH),
    S_E(Direction.SOUTH, Direction.EAST),
    E_S(Direction.EAST, Direction.SOUTH),
    S_W(Direction.SOUTH, Direction.WEST),
    W_S(Direction.WEST, Direction.SOUTH);

    private static final Map<Map.Entry<Direction, Direction>, InOutPairDirection> LOOKUP_MAP = new HashMap<>();

    static {
        for (InOutPairDirection direction : values()) {
            LOOKUP_MAP.put(Map.entry(direction.startDirection, direction.endDirection), direction);
        }
    }

    private final Direction startDirection;
    private final Direction endDirection;

    InOutPairDirection(Direction startDirection, Direction endDirection) {
        this.startDirection = startDirection;
        this.endDirection = endDirection;
    }

    public Direction getStartDirection() {
        return startDirection;
    }

    public Direction getEndDirection() {
        return endDirection;
    }

    public static InOutPairDirection fromDirections(Direction start, Direction end) {
        return LOOKUP_MAP.get(Map.entry(start, end));
    }

}
