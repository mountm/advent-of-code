package year2023;

import base.AoCDay;
import base.AocUtils.Direction;
import org.apache.commons.lang3.tuple.Triple;

import java.time.Instant;
import java.util.*;

public class LavaFloor extends AoCDay {

    // outer key: mirror type
    // inner key: incoming direction
    // inner value: outgoing direction
    Map<Character, Map<Direction, Direction>> mirrorTurns = Map.of(
            '/', Map.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.LEFT, Direction.DOWN),
            '\\', Map.of(Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP)
    );

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2023, 16, false, 0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = countEnergizedCells(grid, Triple.of(0, 0, Direction.RIGHT));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = calculateMaxEnergizedCount(grid);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int calculateMaxEnergizedCount(char[][] grid) {
        int maxCount = 0;
        for (int i = 0; i < grid.length; i++) {
            maxCount = Math.max(maxCount, countEnergizedCells(grid, Triple.of(i, 0, Direction.RIGHT)));
            maxCount = Math.max(maxCount, countEnergizedCells(grid, Triple.of(i, grid.length - 1, Direction.LEFT)));
        }
        for (int j = 0; j < grid.length; j++) {
            maxCount = Math.max(maxCount, countEnergizedCells(grid, Triple.of(0, j, Direction.DOWN)));
            maxCount = Math.max(maxCount, countEnergizedCells(grid, Triple.of(grid.length - 1, j, Direction.UP)));
        }
        return maxCount;
    }

    private int countEnergizedCells(char[][] grid, Triple<Integer, Integer, Direction> initialBeamPoint) {
        boolean[][] energized = new boolean[grid.length][grid.length];
        Map<Direction, boolean[][]> visited = new HashMap<>();
        Arrays.stream(Direction.values()).forEach(d -> visited.put(d, new boolean[grid.length][grid.length]));
        int count = 0;
        Queue<Triple<Integer, Integer, Direction>> floodQueue = new LinkedList<>();
        floodQueue.add(initialBeamPoint);
        while (!floodQueue.isEmpty()) {
            Triple<Integer, Integer, Direction> currStep = floodQueue.poll();
            if (isSafeCoord(currStep.getLeft(), currStep.getMiddle(), grid.length)) {
                Direction currentDirection = currStep.getRight();
                if (!visited.get(currentDirection)[currStep.getLeft()][currStep.getMiddle()]) {
                    visited.get(currentDirection)[currStep.getLeft()][currStep.getMiddle()] = true;
                    if (!energized[currStep.getLeft()][currStep.getMiddle()]) {
                        energized[currStep.getLeft()][currStep.getMiddle()] = true;
                        count++;
                    }
                    switch(grid[currStep.getLeft()][currStep.getMiddle()]) {
                        case '.' :
                            floodQueue.add(Triple.of(currStep.getLeft() + currentDirection.getiStep(), currStep.getMiddle() + currentDirection.getjStep(), currentDirection));
                            break;
                        case '/' :
                        case '\\' :
                            Direction newDirection = mirrorTurns.get(grid[currStep.getLeft()][currStep.getMiddle()]).get(currentDirection);
                            floodQueue.add(Triple.of(currStep.getLeft() + newDirection.getiStep(), currStep.getMiddle() + newDirection.getjStep(), newDirection));
                            break;
                        case '|' :
                            if (currentDirection.getjStep() == 0) {
                                floodQueue.add(Triple.of(currStep.getLeft() + currentDirection.getiStep(), currStep.getMiddle(), currentDirection));
                            } else {
                                floodQueue.add(Triple.of(currStep.getLeft(), currStep.getMiddle(), Direction.UP));
                                floodQueue.add(Triple.of(currStep.getLeft(), currStep.getMiddle(), Direction.DOWN));
                            }
                            break;
                        case '-' :
                            if (currentDirection.getiStep() == 0) {
                                floodQueue.add(Triple.of(currStep.getLeft(), currStep.getMiddle() + currentDirection.getjStep(), currentDirection));
                            } else {
                                floodQueue.add(Triple.of(currStep.getLeft(), currStep.getMiddle(), Direction.LEFT));
                                floodQueue.add(Triple.of(currStep.getLeft(), currStep.getMiddle(), Direction.RIGHT));
                            }
                    }
                }
            }
        }
        return count;
    }
}
