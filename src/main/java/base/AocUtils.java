package base;


import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumSet;
import java.util.Objects;

public class AocUtils {

    public static class CellWithDirection {
        private int iCoord;
        private int jCoord;
        private Direction direction;

        public CellWithDirection(int iCoord, int jCoord, Direction direction) {
            this.iCoord = iCoord;
            this.jCoord = jCoord;
            this.direction = direction;
        }

        public CellWithDirection(Pair<Integer, Integer> startPos, Direction direction) {
            iCoord = startPos.getLeft();
            jCoord = startPos.getRight();
            this.direction = direction;
        }

        public Pair<Integer, Integer> getPosition() {
            return Pair.of(iCoord, jCoord);
        }

        public void setPosition(Pair<Integer, Integer> position) {
            this.iCoord = position.getLeft();
            this.jCoord = position.getRight();
        }

        public int getiCoord() {
            return iCoord;
        }

        public void setiCoord(int iCoord) {
            this.iCoord = iCoord;
        }

        public int getjCoord() {
            return jCoord;
        }

        public void setjCoord(int jCoord) {
            this.jCoord = jCoord;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CellWithDirection that = (CellWithDirection) o;
            return iCoord == that.iCoord && jCoord == that.jCoord && direction == that.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(iCoord, jCoord, direction);
        }
    }

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
        public static Direction getDirectionFromStep(int iStep, int jStep) {
            return EnumSet.allOf(Direction.class).stream().filter(e -> e.getiStep() == iStep && e.getjStep() == jStep).findAny().orElse(null);
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
        public Direction getNextClockwiseDirection() {
            return switch (token) {
                case '^' -> RIGHT;
                case '>' -> DOWN;
                case 'v' -> LEFT;
                case '<' -> UP;
                default -> null;
            };
        }

        public Direction getOppositeDirection() {
            return switch (token) {
                case '^' -> DOWN;
                case '>' -> LEFT;
                case 'v' -> UP;
                case '<' -> RIGHT;
                default -> null;
            };
        }

        public boolean isHorizontal() {
            return iStep == 0;
        }
    }

}
