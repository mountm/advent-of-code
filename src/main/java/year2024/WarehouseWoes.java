package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;
import year2024.Day6.Direction;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseWoes extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 15, false, 0);
        List<Direction> moves = getMoves(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(getPartOneGrid(lines), moves);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(getPartTwoGrid(lines), moves);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long solvePartTwo(char[][] grid, List<Direction> moves) {
        Pair<Integer, Integer> startLoc = findCharInGrid(grid, '@');
        attemptAllMoves(grid, moves, startLoc.getLeft(), startLoc.getRight(), true);
        return countLocations(grid, '[');
    }


    private long solvePartOne(char[][] grid, List<Direction> moves) {
        Pair<Integer, Integer> startLoc = findCharInGrid(grid, '@');
        attemptAllMoves(grid, moves, startLoc.getLeft(), startLoc.getRight(), false);
        return countLocations(grid, '0');
    }

    private long countLocations(char[][] grid, char match) {
        long result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == match) result += (100L * i) + j;
            }
        }
        return result;
    }

    private void attemptAllMoves(char[][] grid, List<Direction> moves, int iPos, int jPos, boolean doubleWidth) {
        boolean didMove;
        for (Direction move : moves) {
            if (!doubleWidth || move.isHorizontal()) {
                didMove = attemptMove(move, grid, iPos, jPos);
            } else {
                didMove = attemptDoubleWidthMove(move, grid, iPos, jPos);
            }
            if (didMove) {
                iPos = iPos + move.getiStep();
                jPos = jPos + move.getjStep();
            }
        }
    }

    private boolean attemptDoubleWidthMove(Direction move, char[][] grid, int iPos, int jPos) {
        List<Pair<Integer, Integer>> coordsRequiringUpdates = checkDoubleWidthObstacles(move, grid, iPos, jPos);
        for (Pair<Integer, Integer> coord : coordsRequiringUpdates) {
            executeMove(move, grid, coord.getLeft(), coord.getRight());
        }
        return !coordsRequiringUpdates.isEmpty();
    }

    private List<Pair<Integer, Integer>> checkDoubleWidthObstacles(Direction move, char[][] grid, int iPos, int jPos) {
        // must insert in reverse order so that you can update the results iteratively
        // each element of the outer list represents a row of elements that would move if this action succeeds (i.e. boxes or the robot)
        // in order for the action to work, every cell above those elements must be either free or movable
        List<Set<Pair<Integer, Integer>>> checking = new LinkedList<>();
        checking.add(Set.of(Pair.of(iPos, jPos)));
        boolean checkNextRow = true;
        while (checkNextRow) {
            Set<Pair<Integer, Integer>> nextRowSet = new HashSet<>();
            for (Pair<Integer, Integer> coord : checking.get(0)) {
                char nextCell = grid[coord.getLeft() + move.getiStep()][coord.getRight()];
                if (nextCell == '[') {
                    nextRowSet.add(Pair.of(coord.getLeft() + move.getiStep(), coord.getRight()));
                    nextRowSet.add(Pair.of(coord.getLeft() + move.getiStep(), coord.getRight() + 1));
                }
                if (nextCell == ']') {
                    nextRowSet.add(Pair.of(coord.getLeft() + move.getiStep(), coord.getRight()));
                    nextRowSet.add(Pair.of(coord.getLeft() + move.getiStep(), coord.getRight() - 1));
                }
                if (nextCell == '#') {
                    checking.clear();
                    nextRowSet.clear();
                    break;
                }
            }
            if (nextRowSet.isEmpty()) {
                checkNextRow = false;
            } else {
                checking.add(0, nextRowSet);
            }
        }
        return checking.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private boolean attemptMove(Direction move, char[][] grid, int iPos, int jPos) {
        List<Character> obstacles = checkObstacles(move, grid, iPos, jPos);
        if (!obstacles.contains('#')) {
            for (int k = obstacles.size(); k >= 0; k--) {
                executeMove(move, grid, iPos + k * move.getiStep(), jPos + k * move.getjStep());
            }
            return true;
        }
        return false;
    }

    private void executeMove(Direction move, char[][] grid, int iPos, int jPos) {
        grid[iPos + move.getiStep()][jPos + move.getjStep()] = grid[iPos][jPos];
        grid[iPos][jPos] = '.';
    }

    private List<Character> checkObstacles(Direction move, char[][] grid, int iPos, int jPos) {
        List<Character> chars = new ArrayList<>();
        int i = iPos + move.getiStep(), j = jPos + move.getjStep();
        while (i >= 0 && i < grid.length && j >= 0 && j < grid[i].length && grid[i][j] != '.') {
            chars.add(grid[i][j]);
            i += move.getiStep();
            j += move.getjStep();
        }
        return chars;
    }

    private List<Direction> getMoves(List<String> lines) {
        int i = 0;
        while(!lines.get(i).isEmpty()) i++;
        i++;
        return lines.subList(i, lines.size()).stream().reduce("", (prev, next) -> prev + next).chars().mapToObj(c -> (char) c).map(Direction::getDirectionByToken).collect(Collectors.toList());
    }

    private char[][] getPartOneGrid(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                return convertToCharGrid(lines.subList(0, i));
            }
        }
        return new char[1][1];
    }
    private char[][] getPartTwoGrid(List<String> lines) {
        char[][] grid = new char[lines.get(0).length()][lines.get(0).length() * 2];
        for (int i = 0; i < grid.length; i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case 'O' -> {
                        grid[i][2 * j] = '[';
                        grid[i][2 * j + 1] = ']';
                    }
                    case '@' -> {
                        grid[i][2 * j] = '@';
                        grid[i][2 * j + 1] = '.';
                    }
                    default -> {
                        grid[i][2 * j] = line.charAt(j);
                        grid[i][2 * j + 1] = line.charAt(j);
                    }
                }
            }
        }
        return grid;
    }
}
