package year2024;

import base.AoCDay;
import base.AocUtils.Direction;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
        Set<Pair<Integer, Integer>> cellsInCurrentSolution = getCellLocations(aStarSearch(grid, startPos, endPos).getRight(), endPos);
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

    private Set<Pair<Integer, Integer>> getCellLocations(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> map, Pair<Integer, Integer> endingCell) {
        Set<Pair<Integer, Integer>> cellLocations = new HashSet<>();
        cellLocations.add(endingCell);
        Queue<Pair<Integer, Integer>> cellsToCheck = new LinkedList<>();
        cellsToCheck.add(map.get(endingCell));
        while(!cellsToCheck.isEmpty()) {
            Pair<Integer, Integer> nextCell = cellsToCheck.poll();
            cellLocations.add(nextCell);
            if (map.containsKey(nextCell)) cellsToCheck.add(map.get(nextCell));
        }
        return cellLocations;
    }

    private int solvePartOne(char[][] grid, Pair<Integer, Integer> startPos, Pair<Integer, Integer> endPos) {
        return aStarSearch(grid, startPos, endPos).getLeft();
    }

    private Pair<Integer, Map<Pair<Integer, Integer>, Pair<Integer, Integer>>> aStarSearch(char[][] grid, Pair<Integer, Integer> startPos, Pair<Integer, Integer> endPos) {
        Map<Pair<Integer, Integer>, Integer> nodeCosts = new HashMap<>();
        nodeCosts.put(startPos, 0);
        PriorityQueue<Pair<Integer, Integer>> frontier = new PriorityQueue<>(Comparator.comparingInt(o -> nodeCosts.get(o) + getManhattanDistance(o, endPos)));
        frontier.add(startPos);
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> prevNode = new HashMap<>();

        while (!frontier.isEmpty()) {
            Pair<Integer, Integer> current = frontier.poll();

            if (current.equals(endPos)) return Pair.of(nodeCosts.get(current), prevNode);
            for (Pair<Integer, Integer> next : getNeighbors(current, grid)) {
                int newCost = nodeCosts.get(current) + 1;
                if (!nodeCosts.containsKey(next) || newCost < nodeCosts.get(next)) {
                    nodeCosts.put(next, newCost);
                    frontier.add(next);
                    prevNode.put(next, current);
                }
            }
        }
        return Pair.of(-1, Map.of());
    }

    private Set<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> current, char[][] grid) {
        return EnumSet.allOf(Direction.class).stream().map(d -> moveInDirection(current, d)).filter(point -> isSafeCoord(point.getLeft(), point.getRight(), grid.length) && grid[point.getLeft()][point.getRight()] != '#').collect(Collectors.toSet());
    }

    private Pair<Integer, Map<Pair<Integer, Integer>, Pair<Integer, Integer>>> dijkstra(Map<Pair<Integer, Integer>, Integer> nodeDistances, Pair<Integer, Integer> endPos) {
        Map<Pair<Integer, Integer>, Pair<Integer, Integer>> prevNode = new HashMap<>();
        while(!nodeDistances.isEmpty()) {
            Map.Entry<Pair<Integer, Integer>, Integer> currentEntry = nodeDistances.entrySet().stream().min(Map.Entry.comparingByValue()).orElseThrow();
            if (currentEntry.getKey().equals(endPos)) {
                return Pair.of(currentEntry.getValue(), prevNode);
            }
            Pair<Integer, Integer> currentCell = currentEntry.getKey();
            nodeDistances.entrySet().stream().filter(e -> getManhattanDistance(e.getKey(), currentCell) == 1).forEach(nextEntry -> {
                int newDistance = currentEntry.getValue() + 1;
                if (newDistance < nextEntry.getValue()) {
                    nextEntry.setValue(newDistance);
                    prevNode.put(nextEntry.getKey(), currentCell);
                }
            });
            nodeDistances.remove(currentEntry.getKey());
        }
        return Pair.of(-1, Map.of());
    }
}
