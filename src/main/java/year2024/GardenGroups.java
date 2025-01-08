package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class GardenGroups extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2024, 12, false, 0));
        int[][] regionMap = determineRegions(grid);
        Map<Integer, Integer> areas = new HashMap<>();
        for (int[] rows : regionMap) {
            for (int val : rows) {
                areas.merge(val, 1, Integer::sum);
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getFencePricing(regionMap, areas);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getFenceSidePricing(regionMap, areas);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int[][] determineRegions(char[][] grid) {
        int[][] result = new int[grid.length][grid.length];
        int nextRegionCode = 1;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (result[i][j] == 0) {
                    result = fillCode(result, grid, i, j, nextRegionCode++);
                }
            }
        }
        return result;
    }

    private int[][] fillCode(int[][] regionMap, char[][] grid, int i, int j, int code) {
        regionMap[i][j] = code;
        char matchChar = grid[i][j];
        for (int m = -1; m <= 1; m++) {
            for (int n = -1; n <= 1; n++) {
                if (Math.abs(m) + Math.abs(n) < 2) {
                    if (isSafeCoord(i + m, n + j, regionMap.length) && grid[i+m][j + n] == matchChar && regionMap[i+m][j+n] == 0) {
                        regionMap = fillCode(regionMap, grid, i+m, j+n, code);
                    }
                }
            }
        }
        return regionMap;
    }

    private int getFencePricing(int[][] regionMap, Map<Integer, Integer> areas) {
        Map<Integer, Integer> perimeters = new HashMap<>();
        for (int i = 0; i < regionMap.length; i++) {
            for (int j = 0; j < regionMap[i].length; j++) {
                perimeters.merge(regionMap[i][j], getPerimeter(regionMap, i, j), Integer::sum);
            }
        }

        return calculateTotal(areas, perimeters);
    }

    private int getFenceSidePricing(int[][] regionMap, Map<Integer, Integer> areas) {
        Map<Integer, Integer> fenceSides = new HashMap<>();
        for (int rotationCount = 0; rotationCount < 4; rotationCount++) {
            for (int i = 0; i < regionMap.length; i++) {
                for (int j = 0; j < regionMap[i].length; j++) {
                    fenceSides.merge(regionMap[i][j], countCorners(regionMap, i, j), Integer::sum);
                }
            }
            if (rotationCount < 3) regionMap = rotateIntGridClockwise(regionMap);
        }

        return calculateTotal(areas, fenceSides);
    }

    private int getPerimeter(int[][] regionMap, int i, int j) {
        int perimeter = 0;
        int cellVal = regionMap[i][j];
        for (int m = -1; m <= 1; m++) {
            for (int n = -1; n <= 1; n++) {
                if (Math.abs(m) + Math.abs(n) < 2) {
                    if (!isSafeCoord(i+m, j + n, regionMap.length)) perimeter++;
                    else if (regionMap[i+m][j + n] != cellVal) perimeter++;
                }
            }
        }
        return perimeter;
    }

    private Integer countCorners(int[][] regionMap, int i, int j) {
        int count = 0;
        // if you're in a corner of the map, that counts as a corner
        if (i == 0 && j == 0) count++;
        // check interior corner
        if (isSafeCoord(i - 1, j - 1, regionMap.length) && regionMap[i-1][j] == regionMap[i][j] && regionMap[i][j-1] == regionMap[i][j] && regionMap[i-1][j-1] != regionMap[i][j]) count++;
        // check exterior corner
        if (isSafeCoord(i - 1, j - 1, regionMap.length) && regionMap[i-1][j] != regionMap[i][j] && regionMap[i][j-1] != regionMap[i][j]) count++;
        if (i == 0 && j > 0 && regionMap[i][j-1] != regionMap[i][j]) count++;
        if (i > 0 && j == 0 && regionMap[i-1][j] != regionMap[i][j]) count++;

        return count;
    }

    private int calculateTotal(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        int sum = 0;
        for (int key : map1.keySet()) {
            sum += map1.get(key) * map2.get(key);
        }
        return sum;
    }

}
