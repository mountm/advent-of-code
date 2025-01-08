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
        Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> searchOutput = searchWithDirections(grid, new CellWithDirection(startPos, Direction.RIGHT), endPos);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findShortestPath(searchOutput, endPos);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePart2((int) part1Answer, searchOutput, endPos);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> searchWithDirections(char[][] grid, CellWithDirection startPos, Pair<Integer, Integer> endPos) {
        Map<CellWithDirection, Set<CellWithDirection>> prevNodes = new HashMap<>();
        Map<CellWithDirection, Integer> nodeCosts = new HashMap<>();
        prevNodes.put(startPos, Set.of());
        nodeCosts.put(startPos, 0);
        PriorityQueue<CellWithDirection> frontier = new PriorityQueue<>(Comparator.comparingInt(o -> nodeCosts.get(o) + getManhattanDistance(o.getPosition(), endPos)));
        frontier.add(startPos);
        while (!frontier.isEmpty()) {
            CellWithDirection current = frontier.poll();
            if (current.getPosition().equals(endPos)) continue;
            for (CellWithDirection next : getNeighbors(current, grid)) {
                int costStep = next.getPosition().equals(current.getPosition()) ? 1000 : 1;
                int newCost = nodeCosts.get(current) + costStep;
                if (!nodeCosts.containsKey(next) || nodeCosts.get(next) > newCost) {
                    nodeCosts.put(next, newCost);
                    frontier.add(next);
                    Set<CellWithDirection> entry = new HashSet<>();
                    entry.add(current);
                    prevNodes.put(next, entry);
                } else if (nodeCosts.containsKey(next) && nodeCosts.get(next) == newCost) {
                    prevNodes.get(next).add(current);
                }
            }
        }
        return Pair.of(nodeCosts, prevNodes);
    }

    private Set<CellWithDirection> getNeighbors(CellWithDirection current, char[][] grid) {
        Set<CellWithDirection> output = new HashSet<>();
        Direction newDirection = current.getDirection().getNextClockwiseDirection();
        output.add(new CellWithDirection(current.getPosition(), newDirection));
        output.add(new CellWithDirection(current.getPosition(), newDirection.getOppositeDirection()));
        Pair<Integer, Integer> newLoc = current.getPositionInFront();
        if (grid[newLoc.getLeft()][newLoc.getRight()] != '#') {
            output.add(new CellWithDirection(newLoc, current.getDirection()));
        }
        return output;
    }

    private int findShortestPath(Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> aStarOutput, Pair<Integer, Integer> endPos) {
        return aStarOutput.getLeft().entrySet().stream().filter(e -> e.getKey().getPosition().equals(endPos)).map(Map.Entry::getValue).min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
    }

    private int solvePart2(int part1Answer, Pair<Map<CellWithDirection, Integer>, Map<CellWithDirection, Set<CellWithDirection>>> aStarOutput, Pair<Integer, Integer> endPos) {
        CellWithDirection endingCell = Objects.requireNonNull(aStarOutput.getLeft().entrySet().stream().filter(e -> e.getKey().getPosition().equals(endPos) && e.getValue() == part1Answer).findFirst().orElse(null)).getKey();
        return countCellLocations(aStarOutput.getRight(), endingCell);
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
}
