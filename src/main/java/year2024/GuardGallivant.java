package year2024;

import base.AoCDay;
import base.AocUtils.Direction;

import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class GuardGallivant extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] puzzle = convertToCharGrid(readResourceFile(2024, 6, false, 0));
        int startY = 0, startX = 0;
        Direction startDirection = null;
        findStart: for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                startDirection = Direction.getDirectionByToken(puzzle[i][j]);
                if (startDirection != null) {
                    startY = i;
                    startX = j;
                    break findStart;
                }
            }
        }
        boolean[][] visited = new boolean[puzzle.length][puzzle.length];
        traceRoute(puzzle, visited, startY, startX, startDirection);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = countVisitedTiles(visited);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = countLoopOptions(puzzle, startY, startX, startDirection, visited);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int countLoopOptions(char[][] puzzle, int startY, int startX, Direction startDirection, boolean[][] visited) {
        int count = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (puzzle[i][j] == '.' && visited[i][j]) {
                    puzzle[i][j] = '#';
                    if (doesLoop(puzzle, startY, startX, startDirection)) {
                        count++;
                    }
                    puzzle[i][j] = '.';
                }
            }
        }
        return count;
    }

    private int countVisitedTiles(boolean[][] visited) {
        int count = 0;
        for (boolean[] row : visited) {
            for (boolean cell : row) {
                if(cell) count++;
            }
        }
        return count;
    }
    private static boolean doesLoop(char[][] puzzle, int i, int j, Direction direction) {
        Map<Direction, boolean[][]> visitedInDirection = new HashMap<>();
        EnumSet.allOf(Direction.class).forEach(d -> visitedInDirection.put(d, new boolean[puzzle.length][puzzle.length]));
        while (i >= 0 && i < puzzle.length && j >= 0 && j < puzzle[i].length) {
            if (visitedInDirection.get(direction)[i][j]) {
                return true;
            }
            visitedInDirection.get(direction)[i][j] = true;
            if (puzzle[i][j] == '#') {
                i -= direction.getiStep();
                j -= direction.getjStep();
                direction = direction.getNextClockwiseDirection();
            } else {
                i += direction.getiStep();
                j += direction.getjStep();
            }
        }
        return false;
    }

    private static void traceRoute(char[][] puzzle, boolean[][] visited, int i, int j, Direction direction) {
        while (i >= 0 && i < puzzle.length && j >= 0 && j < puzzle[i].length) {
            if (puzzle[i][j] == '#') {
                i -= direction.getiStep();
                j -= direction.getjStep();
                direction = direction.getNextClockwiseDirection();
            } else {
                visited[i][j] = true;
                i += direction.getiStep();
                j += direction.getjStep();
            }
        }
    }
}
