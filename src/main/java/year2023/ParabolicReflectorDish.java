package year2023;

import base.AoCDay;
import base.AocUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParabolicReflectorDish extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2023, 14, false, 0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        grid = slideRocks(grid, AocUtils.Direction.UP);
        part1Answer = calculateLoad(grid);
        timeMarkers[2] = Instant.now().toEpochMilli();
        grid = convertToCharGrid(readResourceFile(2023, 14, false, 0));
        part2Answer = findCycleSequence(grid, 1000000000);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int findCycleSequence(char[][] grid, int count) {
        List<Integer> cycleValues = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            grid = slideRocks(grid, AocUtils.Direction.UP);
            grid = slideRocks(grid, AocUtils.Direction.LEFT);
            grid = slideRocks(grid, AocUtils.Direction.DOWN);
            grid = slideRocks(grid, AocUtils.Direction.RIGHT);
            cycleValues.add(calculateLoad(grid));
        }
        int period = findPeriod(cycleValues.subList(500, 1000));
        while (count >= 1000) count -= period;
        return cycleValues.get(count-1);
    }

    private int findPeriod(List<Integer> values) {
        int valueToMatch = values.get(0);
        for (int i = 2; i < values.size() / 2; i++) {
            int finalI = i;
            if (IntStream.range(0, values.size())
                    .filter(s -> s % finalI == 0)
                    .mapToObj(values::get)
                    .allMatch(v -> v == valueToMatch)
            ) return i;
        }
        return -1;
    }

    private int calculateLoad(char[][] grid) {
        int load = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'O') {
                    load += grid.length - i;
                }
            }
        }
        return load;
    }

    private char[][] slideRocks(char[][] grid, AocUtils.Direction direction) {
        AocUtils.Direction newDirection = direction;
        while (newDirection != AocUtils.Direction.UP) {
            grid = rotateCharGridClockwise(grid);
            newDirection = newDirection.getNextClockwiseDirection();
        }
        for (int i = 1; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 'O' && grid[i-1][j] == '.') {
                    int newI = i-1;
                    while(isSafeCoord(newI, j, grid.length) && grid[newI][j] == '.') {
                        grid[newI][j] = 'O';
                        grid[newI+1][j] = '.';
                        newI--;
                    }
                }
            }
        }
        while (newDirection != direction) {
            grid = rotateCharGridClockwise(grid);
            newDirection = newDirection.getNextClockwiseDirection();
        }
        return grid;
    }
}
