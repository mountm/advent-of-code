package year2023;

import base.AoCDay;
import base.AocUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class PipeMaze extends AoCDay {

    private final Set<Character> validPipes = Set.of('|', '-', 'L', 'J', '7', 'F');
    private final Map<Character, List<AocUtils.Direction>> allowedMovements = Map.of(
            '|', List.of(AocUtils.Direction.DOWN, AocUtils.Direction.UP),
            '-', List.of(AocUtils.Direction.RIGHT, AocUtils.Direction.LEFT),
            'L', List.of(AocUtils.Direction.UP, AocUtils.Direction.RIGHT),
            'J', List.of(AocUtils.Direction.UP, AocUtils.Direction.LEFT),
            '7', List.of(AocUtils.Direction.LEFT, AocUtils.Direction.DOWN),
            'F', List.of(AocUtils.Direction.DOWN, AocUtils.Direction.RIGHT)
    );

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] grid = convertToCharGrid(readResourceFile(2023, 10, false, 0));
        timeMarkers[1] = Instant.now().toEpochMilli();
        boolean[][] partOfLoop = findPointsInLoop(grid);
        part1Answer = findFurthestPointDistance(partOfLoop);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = countEnclosedTiles(grid, partOfLoop);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int countEnclosedTiles(char[][] grid, boolean[][] partOfLoop) {
        replaceStartingPoint(grid, partOfLoop);
        List<Pair<Integer, Integer>> points = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (!partOfLoop[i][j]) continue;
                visited[i][j] = true;
                points.add(Pair.of(i, j));
                AocUtils.Direction direction = allowedMovements.get(grid[i][j]).get(0);
                while (direction != null) {
                    i += direction.getiStep();
                    j += direction.getjStep();
                    visited[i][j] = true;
                    points.add(Pair.of(i, j));
                    int finalI = i;
                    int finalJ = j;
                    direction = allowedMovements.get(grid[i][j]).stream().filter(d -> !visited[finalI + d.getiStep()][finalJ + d.getjStep()]).findAny().orElse(null);
                }
                return findInnerPoints(points);
            }
        }
        return 0;
    }

    private int findInnerPoints(List<Pair<Integer, Integer>> points) {
        // https://en.wikipedia.org/wiki/Pick%27s_theorem
        return findArea(points) + 1 - points.size() / 2;
    }

    private int findArea(List<Pair<Integer, Integer>> points) {
        // https://en.wikipedia.org/wiki/Shoelace_formula
        points.add(points.get(0));
        int doubleArea = 0;
        for (int i = 0; i < points.size()-1; i++) {
            doubleArea += (points.get(i).getRight() + points.get(i+1).getRight()) * (points.get(i).getLeft() - points.get(i+1).getLeft());
        }
        assert doubleArea % 2 == 0;
        return doubleArea / 2;
    }

    private void replaceStartingPoint(char[][] grid, boolean[][] partOfLoop) {
        // change the S to the correct pipe character
        // this will be important for the shoelace formula
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 'S') {
                    Set<AocUtils.Direction> validDirections = new HashSet<>();
                    for (AocUtils.Direction direction : EnumSet.allOf(AocUtils.Direction.class)) {
                        if (isSafeCoord(i + direction.getiStep(), j + direction.getjStep(), grid.length)
                                && partOfLoop[i + direction.getiStep()][j + direction.getjStep()]
                                && allowedMovements.get(grid[i + direction.getiStep()][j + direction.getjStep()]).contains(direction.getOppositeDirection())
                        ) {
                            validDirections.add(direction);
                        }
                    }
                    assert validDirections.size() == 2;
                    grid[i][j] = allowedMovements.entrySet().stream().filter(e -> new HashSet<>(e.getValue()).containsAll(validDirections)).map(Map.Entry::getKey).findAny().orElse('X');
                }
            }
        }
    }


    private boolean[][] findPointsInLoop(char[][] grid) {
        /*
        1. find the first unvisited pipe in the maze
        2. flood fill until you can't connect to any new tiles, or you find S
        3. if you can't connect to any new tiles, go back to step 1 and keep a "non-global" seen-tiles check for your next pass
        4. when you find S, continue to flood fill from the "other end" of the loop until you have identified all tiles in loop
         */
        boolean[][] visited = new boolean[grid.length][grid.length];
        boolean foundS = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (!visited[i][j] && isPipe(i, j, grid)) {
                    boolean[][] visitedThisPass = new boolean[grid.length][grid.length];
                    visited[i][j] = true;
                    visitedThisPass[i][j] = true;
                    Queue<Pair<Integer, Integer>> floodQueue = new LinkedList<>();
                    for (AocUtils.Direction direction : allowedMovements.get(grid[i][j])) {
                        if (isSafeCoord(i + direction.getiStep(), j + direction.getjStep(), grid.length)
                                && ((isPipe(i + direction.getiStep(), j + direction.getjStep(), grid) && canConnect(i, j, direction, grid)) || grid[i+direction.getiStep()][j+ direction.getjStep()] == 'S')
                        ) floodQueue.add(Pair.of(i + direction.getiStep(), j + direction.getjStep()));
                    }
                    while (!floodQueue.isEmpty()) {
                        Pair<Integer, Integer> nextTile = floodQueue.poll();
                        if (!visited[nextTile.getLeft()][nextTile.getRight()]) {
                            visited[nextTile.getLeft()][nextTile.getRight()] = true;
                            visitedThisPass[nextTile.getLeft()][nextTile.getRight()] = true;
                            if (grid[nextTile.getLeft()][nextTile.getRight()] == 'S') {
                                foundS = true;
                            } else {
                                for (AocUtils.Direction direction : allowedMovements.get(grid[nextTile.getLeft()][nextTile.getRight()])) {
                                    if (isSafeCoord(nextTile.getLeft() + direction.getiStep(), nextTile.getRight() + direction.getjStep(), grid.length)
                                            && ((isPipe(nextTile.getLeft() + direction.getiStep(), nextTile.getRight() + direction.getjStep(), grid) && canConnect(nextTile.getLeft(), nextTile.getRight(), direction, grid))
                                            || grid[nextTile.getLeft() + direction.getiStep()][nextTile.getRight() + direction.getjStep()] == 'S')
                                    ) floodQueue.add(Pair.of(nextTile.getLeft() + direction.getiStep(), nextTile.getRight() + direction.getjStep()));
                                }
                            }
                        }
                    }
                    if (foundS) {
                        return visitedThisPass;
                    }
                }

            }
        }
        return null;
    }

    private int findFurthestPointDistance(boolean[][] visited) {
        return countVisited(visited) / 2;
    }

    private int countVisited(boolean[][] visitedThisPass) {
        int count = 0;
        for (boolean[] thisPass : visitedThisPass) {
            for (int j = 0; j < visitedThisPass.length; j++) {
                if (thisPass[j]) count++;
            }
        }
        return count;
    }

    private boolean canConnect(int i, int j, AocUtils.Direction direction, char[][] grid) {
        return allowedMovements.get(grid[i + direction.getiStep()][j + direction.getjStep()]).contains(direction.getOppositeDirection());
    }

    private boolean isPipe(int i, int j, char[][] grid) {
        return validPipes.contains(grid[i][j]);
    }

}
