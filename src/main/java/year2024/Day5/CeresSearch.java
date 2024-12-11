package year2024.Day5;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class CeresSearch extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        char[][] puzzle = convertToCharGrid(readResourceFile(2024, 4, false, 0));

        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePart1(puzzle);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePart2(puzzle);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePart1(char[][] puzzle) {
        int count = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (puzzle[i][j] == 'X') {
                    for(int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            count += countAdjacentMatchesInDirection(puzzle, "MAS", i, j, m, n);
                        }
                    }
                }
            }
        }
        return count;
    }

    private int solvePart2(char[][] puzzle) {
        return countMatches(puzzle, "A");
    }

    private int countMatches(char[][] puzzle, String pattern) {
        int count = 0;
        char firstChar = pattern.charAt(0);
        for (int i = 1; i < puzzle.length - 1; i++) {
            for (int j = 1; j < puzzle[i].length - 1; j++) {
                if (puzzle[i][j] == firstChar) {
                    count += countMatchesStartingFromPosition(puzzle, i, j);
                }
            }
        }
        return count;
    }
    private int countMatchesStartingFromPosition(char[][] puzzle, int iStart, int jStart) {
        Set<Character> mustHave = Set.of('S', 'M');
        try {
            Set<Character> set1 = Set.of(puzzle[iStart+1][jStart+1], puzzle[iStart - 1][jStart - 1]);
            Set<Character> set2 = Set.of(puzzle[iStart+1][jStart-1], puzzle[iStart - 1][jStart + 1]);
            if (set1.containsAll(mustHave) && set2.containsAll(mustHave)) {
                return 1;
            }
        } catch(IllegalArgumentException ignored) {

        }
        return 0;
    }
    private int countAdjacentMatchesInDirection(char[][] puzzle, String pattern, int iStart, int jStart, int iStep, int jStep) {
        char nextChar = pattern.charAt(0);
        int newI = iStart + iStep;
        int newJ = jStart + jStep;
        if (!isSafeCoord(newI, newJ, puzzle.length) || puzzle[newI][newJ] != nextChar) {
            return 0;
        }
        if (pattern.length() == 1) {
            return 1;
        }
        String newPattern = pattern.substring(1);
        return countAdjacentMatchesInDirection(puzzle, newPattern, newI, newJ, iStep, jStep);
    }
}
