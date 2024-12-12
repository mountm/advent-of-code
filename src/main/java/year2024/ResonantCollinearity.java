package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResonantCollinearity extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2024, 8, false, 0));
        Map<Character, List<Pair<Integer, Integer>>> antennas = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != '.') {
                    if (antennas.containsKey(grid[i][j])) {
                        antennas.get(grid[i][j]).add(Pair.of(i, j));
                    } else {
                        List<Pair<Integer, Integer>> entryList = new ArrayList<>();
                        entryList.add(Pair.of(i, j));
                        antennas.put(grid[i][j], entryList);
                    }
                }
            }
        }
        antennas.entrySet().removeIf(e -> e.getValue().size() < 2);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findAntiNodes(antennas, grid.length);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findMoreAntiNodes(antennas, grid.length);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int findAntiNodes(Map<Character, List<Pair<Integer, Integer>>> antennas, int puzzleSize) {
        boolean[][] hasAntiNode = new boolean[puzzleSize][puzzleSize];
        for (List<Pair<Integer, Integer>> list : antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i+1; j < list.size(); j++) {
                    int vertStep = list.get(j).getLeft() - list.get(i).getLeft();
                    int horzStep = list.get(j).getRight() - list.get(i).getRight();
                    List<Pair<Integer, Integer>> possiblePoints = List.of(
                            Pair.of(list.get(i).getLeft() + vertStep, list.get(i).getRight() + horzStep),
                            Pair.of(list.get(i).getLeft() - vertStep, list.get(i).getRight() - horzStep),
                            Pair.of(list.get(j).getLeft() + vertStep, list.get(j).getRight() + horzStep),
                            Pair.of(list.get(j).getLeft() - vertStep, list.get(j).getRight() - horzStep)
                    );
                    for (Pair<Integer, Integer> possiblePoint: possiblePoints) {
                        if (!possiblePoint.equals(list.get(i)) && !possiblePoint.equals(list.get(j)) && isSafeCoord(possiblePoint.getLeft(), possiblePoint.getRight(), puzzleSize)) {
                            hasAntiNode[possiblePoint.getLeft()][possiblePoint.getRight()] = true;
                        }
                    }
                }
            }
        }
        return countAntiNodes(hasAntiNode);
    }

    private int findMoreAntiNodes(Map<Character, List<Pair<Integer, Integer>>> antennas, int puzzleSize) {
        boolean[][] hasAntiNode = new boolean[puzzleSize][puzzleSize];
        for (List<Pair<Integer, Integer>> list : antennas.values()) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = i+1; j < list.size(); j++) {
                    int vertStep = list.get(j).getLeft() - list.get(i).getLeft();
                    int horzStep = list.get(j).getRight() - list.get(i).getRight();
                    Pair<Integer, Integer> newPoint = Pair.of(list.get(i).getLeft() + vertStep, list.get(i).getRight() + horzStep);
                    while(isSafeCoord(newPoint.getLeft(), newPoint.getRight(), puzzleSize)) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() + vertStep, newPoint.getRight() + horzStep);
                    }
                    newPoint = Pair.of(list.get(i).getLeft() - vertStep, list.get(i).getRight() - horzStep);
                    while(isSafeCoord(newPoint.getLeft(), newPoint.getRight(), puzzleSize)) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() - vertStep, newPoint.getRight() - horzStep);
                    }
                    newPoint = Pair.of(list.get(j).getLeft() + vertStep, list.get(j).getRight() + horzStep);
                    while(isSafeCoord(newPoint.getLeft(), newPoint.getRight(), puzzleSize)) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() + vertStep, newPoint.getRight() + horzStep);
                    }
                    newPoint = Pair.of(list.get(j).getLeft() - vertStep, list.get(j).getRight() - horzStep);
                    while(isSafeCoord(newPoint.getLeft(), newPoint.getRight(), puzzleSize)) {
                        hasAntiNode[newPoint.getLeft()][newPoint.getRight()] = true;
                        newPoint = Pair.of(newPoint.getLeft() - vertStep, newPoint.getRight() - horzStep);
                    }
                }
            }
        }
        return countAntiNodes(hasAntiNode);
    }

    private int countAntiNodes(boolean[][] hasAntiNode) {
        int count = 0;
        for (boolean[] row : hasAntiNode) {
            for (boolean entry : row) {
                if (entry) count++;
            }
        }
        return count;
    }
}
