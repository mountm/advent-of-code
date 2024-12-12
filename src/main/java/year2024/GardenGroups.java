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
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getFencePricing(regionMap);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getFenceSidePricing(regionMap);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int getFenceSidePricing(int[][] regionMap) {
        Map<Integer, Integer> areas = new HashMap<>();
        Map<Integer, Integer> fenceSides = new HashMap<>();
        for (int i = 0; i < regionMap.length; i++) {
            for (int j = 0; j < regionMap[i].length; j++) {
                areas.merge(regionMap[i][j], 1, Integer::sum);
                if (!fenceSides.containsKey(regionMap[i][j])) {
                    fenceSides.put(regionMap[i][j], getFenceSides(regionMap, i, j));
                }
            }
        }
        int sum = 0;
        for (int key : areas.keySet()) {
            sum += areas.get(key) * fenceSides.get(key);
        }
        return sum;
    }

    private Integer getFenceSides(int[][] regionMap, int startPosY, int startPosX) {
        int thisRegion = regionMap[startPosY][startPosX];
        boolean[][] foundAbove = new boolean[regionMap.length][regionMap.length];
        boolean[][] foundBelow = new boolean[regionMap.length][regionMap.length];
        boolean[][] foundLeft = new boolean[regionMap.length][regionMap.length];
        boolean[][] foundRight = new boolean[regionMap.length][regionMap.length];
        for (int i = 0; i < regionMap.length; i++) {
            for (int j = 0; j < regionMap.length; j++) {
                if (regionMap[i][j] == thisRegion) {
                    if (i == 0 || regionMap[i-1][j] != thisRegion) foundAbove[i][j] = true;
                    if (i == regionMap.length - 1 || regionMap[i+1][j] != thisRegion) foundBelow[i][j] = true;
                    if (j == 0 || regionMap[i][j-1] != thisRegion) foundLeft[i][j] = true;
                    if (j == regionMap.length - 1 || regionMap[i][j+1] != thisRegion) foundRight[i][j] = true;
                }
            }
        }
        return addSides(foundAbove, false) + addSides(foundBelow, false) + addSides(foundLeft, true) + addSides(foundRight, true);

    }

    private Integer addSides(boolean[][] grid, boolean countVert) {
        int count = 0;
        boolean foundFence = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if ((countVert ? grid[j][i] : grid[i][j]) && !foundFence) {
                    count++;
                    foundFence = true;
                }
                if (!(countVert ? grid[j][i] : grid[i][j])) foundFence = false;
            }
        }
        return count;
    }

    private int getFencePricing(int[][] regionMap) {
        Map<Integer, Integer> areas = new HashMap<>();
        Map<Integer, Integer> perimeters = new HashMap<>();
        for (int i = 0; i < regionMap.length; i++) {
            for (int j = 0; j < regionMap[i].length; j++) {
                areas.merge(regionMap[i][j], 1, Integer::sum);
                perimeters.merge(regionMap[i][j], getPerimeter(regionMap, i, j), Integer::sum);
            }
        }
        int sum = 0;
        for (int key : areas.keySet()) {
            sum += areas.get(key) * perimeters.get(key);
        }
        return sum;
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
}
