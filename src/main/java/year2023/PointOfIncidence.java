package year2023;

import base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointOfIncidence extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 13, false, 0);
        List<char[][]> grids = makeGrids(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = grids.stream().mapToLong(this::getPatternSummary).sum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = grids.stream().mapToLong(this::getSmudgedPatternSummary).sum();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long getSmudgedPatternSummary(char[][] grid) {
        return 100L * countRowsAboveReflection(grid, 1) + countRowsAboveReflection(rotateCharGridClockwise(grid), 1);
    }

    private long getPatternSummary(char[][] grid) {
        return 100L * countRowsAboveReflection(grid, 0) + countRowsAboveReflection(rotateCharGridClockwise(grid), 0);
    }

    private int countRowsAboveReflection(char[][] grid, int allowedDiffs) {
        printGrid(grid);
        for (int i = 0; i < grid.length - 1; i++) {
            int diffCount = 0;
            for (int offset = 0; offset < grid.length - i - 1; offset++) {
                if (i - offset >= 0) {
                    for (int j = 0; j < grid[i].length; j++) {
                        if (grid[i - offset][j] != grid[i + offset + 1][j]) diffCount++;
                    }
                }
            }
            if (diffCount == allowedDiffs)  {
                System.out.println("Reflection between rows " + i + " and " + (i+1));
                return i+1;
            }
        }
        return 0;
    }


    private List<char[][]> makeGrids(List<String> lines) {
        List<char[][]> grids = new ArrayList<>();
        int startPos = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                grids.add(convertToCharGrid(lines.subList(startPos, i)));
                startPos = i+1;
            }
        }
        return grids;
    }
}
