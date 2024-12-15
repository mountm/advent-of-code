package year2024.Day6;

import java.util.EnumSet;

public enum Direction {
    UP ('^', -1, 0),
    RIGHT('>', 0, 1),
    DOWN('v', 1, 0),
    LEFT('<', 0, -1);
    private final char token;
    private final int iStep, jStep;
    Direction(char token, int iStep, int jStep) {
        this.token = token;
        this.iStep = iStep;
        this.jStep = jStep;
    }
    public static Direction getDirectionByToken(char token) {
        return EnumSet.allOf(Direction.class).stream().filter(e -> e.getToken() == (token)).findAny().orElse(null);
    }
    public char getToken() {
        return token;
    }
    public int getiStep() {
        return iStep;
    }
    public int getjStep() {
        return jStep;
    }
    public Direction getNextDirection() {
        return switch (token) {
            case '^' -> RIGHT;
            case '>' -> DOWN;
            case 'v' -> LEFT;
            case '<' -> UP;
            default -> null;
        };
    }

    public boolean isHorizontal() {
        return iStep == 0;
    }
}
