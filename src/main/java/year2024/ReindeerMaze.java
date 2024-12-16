package year2024;

import base.AoCDay;
import base.AocUtils.CellWithDirection;
import base.AocUtils.Direction;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class ReindeerMaze extends AoCDay {
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2024, 16, false, 0));
        Pair<Integer, Integer> startPos = findCharInGrid(grid, 'S');
        Pair<Integer, Integer> endPos = findCharInGrid(grid, 'E');
        Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> dijkstraOutput = dijkstraWithDirections(grid, new CellWithDirection(startPos, Direction.RIGHT), endPos);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findShortestPath(dijkstraOutput, endPos);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePart2((int) part1Answer, dijkstraOutput, endPos);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int findShortestPath(Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> dijkstraOutput, Pair<Integer, Integer> endPos) {
        return dijkstraOutput.getLeft().entrySet().stream().filter(e -> e.getKey().getPosition().equals(endPos)).map(Map.Entry::getValue).min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
    }

    private int solvePart2(int part1Answer, Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> dijkstraOutput, Pair<Integer, Integer> endPos) {
        CellWithDirection endingCell = Objects.requireNonNull(dijkstraOutput.getLeft().entrySet().stream().filter(e -> e.getKey().getPosition().equals(endPos) && e.getValue() == part1Answer).findFirst().orElse(null)).getKey();
        return countCellLocations(dijkstraOutput.getRight(), endingCell);
    }

    private int countCellLocations(Map<CellWithDirection, Set<CellWithDirection>> map, CellWithDirection endingCell) {
        Set<Pair<Integer, Integer>> cellLocations = new HashSet<>();
        cellLocations.add(endingCell.getPosition());
        Queue<CellWithDirection> cellsToCheck = new LinkedList<>(map.get(endingCell));
        while(!cellsToCheck.isEmpty()) {
            CellWithDirection nextCell = cellsToCheck.poll();
            cellLocations.add(nextCell.getPosition());
            cellsToCheck.addAll(map.getOrDefault(nextCell, Set.of()));
        }
        return cellLocations.size();
    }

    private Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> dijkstraWithDirections(char[][] grid, CellWithDirection startPos, Pair<Integer, Integer> endPos) {
        Map<CellWithDirection, Integer> finalizedNodes = new HashMap<>();
        Map<CellWithDirection, Integer> nodeDistances = new HashMap<>();
        Map<CellWithDirection, Set<CellWithDirection>> prevNodes = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == startPos.getiCoord() && j == startPos.getjCoord()) {
                    nodeDistances.put(startPos, 0);
                    for (Direction dir : EnumSet.allOf(Direction.class)) {
                        if (!dir.equals(startPos.getDirection())) {
                            nodeDistances.put(new CellWithDirection(startPos.getPosition(), dir), Integer.MAX_VALUE);
                        }
                    }
                } else if (grid[i][j] != '#'){
                    for (Direction dir : EnumSet.allOf(Direction.class)) {
                        nodeDistances.put(new CellWithDirection(i, j, dir), Integer.MAX_VALUE);
                    }
                }
            }
        }
        int solCost = Integer.MAX_VALUE;
        while(!nodeDistances.isEmpty()) {
            Map.Entry<CellWithDirection, Integer> currentEntry = nodeDistances.entrySet().stream().min(Map.Entry.comparingByValue()).orElseThrow();
            if (currentEntry.getValue() > solCost) break;
            if (currentEntry.getKey().getPosition().equals(endPos)) {
                solCost = currentEntry.getValue();
            }
            CellWithDirection currentCell = currentEntry.getKey();
            nodeDistances.entrySet().stream().filter(e -> isNeighbor(currentCell, e.getKey())).forEach(nextEntry -> {
                int newDistance = currentEntry.getValue() + calculateDistance(currentCell, nextEntry.getKey());
                if (newDistance == nextEntry.getValue()) {
                    // equally valid path
                    prevNodes.get(nextEntry.getKey()).add(currentCell);
                }
                if (newDistance < nextEntry.getValue()) {
                    nextEntry.setValue(newDistance);
                    Set<CellWithDirection> prevNodeEntry = new HashSet<>();
                    prevNodeEntry.add(currentCell);
                    prevNodes.put(nextEntry.getKey(), prevNodeEntry);
                }
            });
            finalizedNodes.put(currentEntry.getKey(), nodeDistances.remove(currentEntry.getKey()));
        }
        return Pair.of(finalizedNodes, prevNodes);
    }

    private boolean isNeighbor(CellWithDirection current, CellWithDirection next) {
        if (getManhattanDistance(current.getPosition(), next.getPosition()) == 1) {
            return (current.getiCoord() + current.getDirection().getiStep() == next.getiCoord() && current.getjCoord() + current.getDirection().getjStep() == next.getjCoord() && current.getDirection().equals(next.getDirection()));
        }
        return (current.getPosition().equals(next.getPosition()) && !current.equals(next) && !current.getDirection().getOppositeDirection().equals(next.getDirection()));
    }

    private int calculateDistance(CellWithDirection current, CellWithDirection next) {
        return (current.getPosition().equals(next.getPosition())) ? 1000 : 1;
    }
}
