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
        Map<Pair<Integer, Integer>, Integer> nodeDistances = calculateDistances(lines.subList(0, 1024));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(nodeDistances);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(lines, nodeDistances);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private String solvePartTwo(List<String> lines, Map<Pair<Integer, Integer>, Integer> nodeDistances) {
        Pair<Integer, Integer> endPos = Pair.of(PUZZLE_SIZE - 1, PUZZLE_SIZE - 1);
        Set<Pair<Integer, Integer>> cellsInCurrentSolution = getCellLocations(dijkstra(new HashMap<>(nodeDistances), endPos).getRight(), endPos);
        for (int i = 1024; i < lines.size(); i++) {
            Pair<Integer, Integer> nextCorruptedBlock = Pair.of(parseInt(lines.get(i).split(",")[1], 10), parseInt(lines.get(i).split(",")[0], 10));
            nodeDistances.remove(nextCorruptedBlock);
            if (cellsInCurrentSolution.contains(nextCorruptedBlock)) {
                Pair<Integer, Map<Pair<Integer, Integer>, Pair<Integer, Integer>>> dijkstraOutput = dijkstra(new HashMap<>(nodeDistances), endPos);
                if (dijkstraOutput.getLeft() == Integer.MAX_VALUE || dijkstraOutput.getLeft() < 0) return lines.get(i);
                cellsInCurrentSolution = getCellLocations(dijkstraOutput.getRight(), endPos);
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

    private Map<Pair<Integer, Integer>, Integer> calculateDistances(List<String> lines) {
        Map<Pair<Integer, Integer>, Integer> nodeDistances = new HashMap<>();
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (i == 0 && j == 0) {
                    nodeDistances.put(Pair.of(i, j), 0);
                } else if (!lines.contains(j + "," + i)) {
                    nodeDistances.put(Pair.of(i, j), Integer.MAX_VALUE);
                }
            }
        }
        return nodeDistances;
    }

    private int solvePartOne(Map<Pair<Integer, Integer>, Integer> nodeDistances) {
        return dijkstra(new HashMap<>(nodeDistances), Pair.of(PUZZLE_SIZE - 1, PUZZLE_SIZE - 1)).getLeft();
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
