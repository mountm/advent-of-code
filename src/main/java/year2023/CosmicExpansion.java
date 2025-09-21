package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CosmicExpansion extends AoCDay {

    private Set<Integer> emptyRows = new HashSet<>();
    private Set<Integer> emptyColumns = new HashSet<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2023, 11, false, 0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        List<Pair<Integer, Integer>> points = findPoints(grid);
        markEmptyRowsAndColumns(points, grid.length);
        part1Answer = totalDistances(points, 1L);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = totalDistances(points, 999999L);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void markEmptyRowsAndColumns(List<Pair<Integer, Integer>> points, int maxVal) {
        Set<Integer> nonEmptyRows = points.stream().map(Pair::getLeft).collect(Collectors.toSet());
        Set<Integer> nonEmptyColumns = points.stream().map(Pair::getRight).collect(Collectors.toSet());
        IntStream.range(0, maxVal).filter(i -> !nonEmptyRows.contains(i)).forEach(emptyRows::add);
        IntStream.range(0, maxVal).filter(i -> !nonEmptyColumns.contains(i)).forEach(emptyColumns::add);
    }

    private long totalDistances(List<Pair<Integer, Integer>> points, long costFactor) {
        long sum = 0;
        for (int i = 0; i < points.size()-1; i++) {
            Pair<Integer, Integer> point = points.get(i);
            sum += points.subList(i+1, points.size()).stream().mapToLong(p -> findDistance(point, p, costFactor)).sum();
        }
        return sum;
    }

    private long findDistance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2, long costFactor) {
        int manhattanDistance = getManhattanDistance(p1, p2);
        long extraRowCost = emptyRows.stream().filter(r -> (p1.getLeft() < r && p2.getLeft() > r) || (p1.getLeft() > r && p2.getLeft() < r)).count() * costFactor;
        long extraColumnCost = emptyColumns.stream().filter(c -> (p1.getRight() < c && p2.getRight() > c) || (p1.getRight() > c && p2.getRight() < c)).count() * costFactor;
        return extraRowCost + extraColumnCost + manhattanDistance;
    }

    private List<Pair<Integer, Integer>> findPoints(char[][] expandedGrid) {
        List<Pair<Integer, Integer>> points = new ArrayList<>();
        for (int i = 0; i < expandedGrid.length; i++) {
            for (int j = 0; j < expandedGrid[i].length; j++) {
                if (expandedGrid[i][j] == '#') points.add(Pair.of(i, j));
            }
        }
        return points;
    }
}
