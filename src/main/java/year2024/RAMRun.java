package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

import static java.lang.Integer.parseInt;

public class RAMRun extends AoCDay {

    private final int PUZZLE_SIZE = 71;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 18, false, 0);
        Pair<Integer, Integer> startPos = Pair.of(0,0);
        Pair<Integer, Integer> endPos = Pair.of(PUZZLE_SIZE - 1, PUZZLE_SIZE - 1);
        char[][] grid = new char[PUZZLE_SIZE][PUZZLE_SIZE];
        buildGrid(grid, lines.subList(0, 1024));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(grid, startPos, endPos);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(grid, startPos, endPos, lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void buildGrid(char[][] grid, List<String> lines) {
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                grid[i][j] = lines.contains(j + "," + i) ? '#' : '.';
            }
        }
    }

    private String solvePartTwo(char[][] grid, Pair<Integer, Integer> startPos, Pair<Integer, Integer> endPos, List<String> lines) {
        List<Pair<Integer, Integer>> cellsInCurrentSolution = getCellLocations(aStarSearch(grid, startPos, endPos).getRight(), endPos);
        for (int i = 1024; i < lines.size(); i++) {
            Pair<Integer, Integer> nextCorruptedBlock = Pair.of(parseInt(lines.get(i).split(",")[1], 10), parseInt(lines.get(i).split(",")[0], 10));
            grid[nextCorruptedBlock.getLeft()][nextCorruptedBlock.getRight()] = '#';
            if (cellsInCurrentSolution.contains(nextCorruptedBlock)) {
                Pair<Integer, Map<Pair<Integer, Integer>, Pair<Integer, Integer>>> searchOutput = aStarSearch(grid, startPos, endPos);
                if (searchOutput.getLeft() == Integer.MAX_VALUE || searchOutput.getLeft() < 0) return lines.get(i);
                cellsInCurrentSolution = getCellLocations(searchOutput.getRight(), endPos);
            }
        }
        return "-1,-1";
    }

    private int solvePartOne(char[][] grid, Pair<Integer, Integer> startPos, Pair<Integer, Integer> endPos) {
        return aStarSearch(grid, startPos, endPos).getLeft();
    }


}
