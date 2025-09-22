package year2023;

import base.AoCDay;
import base.AocUtils.Direction;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.time.Instant;
import java.util.*;

public class ClumsyCrucible extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        int[][] grid = convertToIntGrid(readResourceFile(2023, 17, false, 0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = findMinHeatLoss(grid, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = findMinHeatLoss(grid, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int findMinHeatLoss(int[][] grid, boolean ultra) {
        // A* requiring turns
        Pair<Integer, Integer> endPos = Pair.of(grid.length-1, grid.length-1);
        Map<Triple<Integer, Integer, Direction>, Integer> nodeCosts = new HashMap<>();
        nodeCosts.put(Triple.of(0, 0, Direction.DOWN), 0);
        nodeCosts.put(Triple.of(0, 0, Direction.RIGHT), 0);
        PriorityQueue<Triple<Integer, Integer, Direction>> frontier = new PriorityQueue<>(Comparator.comparingInt(o -> nodeCosts.get(o) + getManhattanDistance(Pair.of(o.getLeft(), o.getMiddle()), endPos)));
        frontier.add(Triple.of(0, 0, Direction.DOWN));
        frontier.add(Triple.of(0, 0, Direction.RIGHT));
        while (!frontier.isEmpty()) {
            Triple<Integer, Integer, Direction> current = frontier.poll();
            if (Pair.of(current.getLeft(), current.getMiddle()).equals(endPos)) return nodeCosts.get(current);
            for (Triple<Integer, Integer, Direction> next : getNeighbors(current, grid, ultra)) {
                int newCost = nodeCosts.get(current) + calculateCost(current, next, grid);
                if (!nodeCosts.containsKey(next) || newCost < nodeCosts.get(next)) {
                    nodeCosts.put(next, newCost);
                    frontier.add(next);
                }
            }
        }
        return -1;
    }

    private int calculateCost(Triple<Integer, Integer, Direction> current, Triple<Integer, Integer, Direction> next, int[][] grid) {
        int cost = 0;
        Direction direction = next.getRight();
        Pair<Integer, Integer> currPoint = Pair.of(current.getLeft(), current.getMiddle());
        while (!Objects.equals(currPoint.getLeft(), next.getLeft()) || !Objects.equals(currPoint.getRight(), next.getMiddle())) {
            currPoint = Pair.of(currPoint.getLeft() + direction.getiStep(), currPoint.getRight() + direction.getjStep());
            cost += grid[currPoint.getLeft()][currPoint.getRight()];
        }
        return cost;
    }

    private Set<Triple<Integer, Integer, Direction>> getNeighbors(Triple<Integer, Integer, Direction> current, int[][] grid, boolean ultra) {
        Set<Direction> allowedDirections = Set.of(current.getRight().getNextClockwiseDirection(), current.getRight().getNextClockwiseDirection().getOppositeDirection());
        Set<Triple<Integer, Integer, Direction>> neighbors = new HashSet<>();
        int minMovement = ultra ? 4 : 1;
        int maxMovement = ultra ? 10 : 3;
        for (int n = minMovement; n <= maxMovement; n++) {
            for (Direction direction : allowedDirections) {
                int i = current.getLeft() + n * direction.getiStep();
                int j = current.getMiddle() + n * direction.getjStep();
                if (isSafeCoord(i, j, grid.length)) {
                    neighbors.add(Triple.of(i, j, direction));
                }
            }
        }
        return neighbors;
    }
}
