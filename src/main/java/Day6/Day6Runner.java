package Day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class Day6Runner {
    private static final int PUZZLE_SIZE = 130;
    static Map<Direction, boolean[][]> visitedInDirection = new HashMap<>();
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        char[][] puzzle = new char[PUZZLE_SIZE][];
        boolean[][] visited = new boolean[PUZZLE_SIZE][PUZZLE_SIZE];
        resetMap();
        try {
            input = new BufferedReader(new FileReader("inputs/guard-map.txt"));
            int row = 0;
            while ((currentLine = input.readLine()) != null) {
                puzzle[row++] = currentLine.toCharArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                Direction startDirection = Direction.getDirectionByToken(puzzle[i][j]);
                if (startDirection != null) {
                    System.out.println("Starting at position (" + i + ", " + j + ") with direction " + startDirection);
                    visited = traceRoute(puzzle, visited, i, j, startDirection);
                    int obstructionCount = 0;
                    for (int m = 0; m < puzzle.length; m++) {
                        for (int n = 0; n < puzzle.length; n++) {
                            if (puzzle[m][n] == '.' && visited[m][n]) {
                                puzzle[m][n] = '#';
                                if (doesLoop(puzzle, i, j, startDirection)) {
                                    obstructionCount += 1;
                                }
                                puzzle[m][n] = '.';
                                resetMap();
                            }
                        }
                    }
                    System.out.println(obstructionCount);
                }
            }
        }
    }
    private static boolean doesLoop(char[][] puzzle, int i, int j, Direction direction) {
        while (i >= 0 && i < puzzle.length && j >= 0 && j < puzzle[i].length) {
            if (visitedInDirection.get(direction)[i][j]) {
                return true;
            }
            visitedInDirection.get(direction)[i][j] = true;
            if (puzzle[i][j] == '#') {
                i -= direction.getiStep();
                j -= direction.getjStep();
                direction = direction.getNextDirection();
            } else {
                i += direction.getiStep();
                j += direction.getjStep();
            }
        }
        return false;
    }
    private static void resetMap() {
        EnumSet.allOf(Direction.class).forEach(direction -> visitedInDirection.put(direction, new boolean[PUZZLE_SIZE][PUZZLE_SIZE]));
    }
    private static boolean[][] traceRoute(char[][] puzzle, boolean[][] visited, int i, int j, Direction direction) {
        while (i >= 0 && i < puzzle.length && j >= 0 && j < puzzle[i].length) {
            if (puzzle[i][j] == '#') {
                i -= direction.getiStep();
                j -= direction.getjStep();
                System.out.println("New direction " + direction.getNextDirection() + " at position (" + i + ", " + j + ")");
                direction = direction.getNextDirection();
            } else {
                visited[i][j] = true;
                i += direction.getiStep();
                j += direction.getjStep();
            }
        }
        return visited;
    }
}
