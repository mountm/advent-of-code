package year2024.Day5;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Day5Runner extends AoCDay {
    static Set<Character> mustHave = Set.of('S', 'M');
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        char[][] puzzle = new char[140][];
        try {
            input = new BufferedReader(new FileReader("inputs/wordsearch.txt"));
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
        System.out.println(countMatches(puzzle, "A"));
    }
    private static int countMatches(char[][] puzzle, String pattern) {
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
    private static int countMatchesStartingFromPosition(char[][] puzzle, int iStart, int jStart) {
        try {
            Set<Character> set1 = Set.of(puzzle[iStart+1][jStart+1], puzzle[iStart - 1][jStart - 1]);
            Set<Character> set2 = Set.of(puzzle[iStart+1][jStart-1], puzzle[iStart - 1][jStart + 1]);
            if (set1.containsAll(mustHave) && set2.containsAll(mustHave)) {
                return 1;
            }
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private static int countAdjacentMatchesInDirection(char[][] puzzle, String pattern, int iStart, int jStart, int iStep, int jStep) {
        char nextChar = pattern.charAt(0);
        int newI = iStart + iStep;
        int newJ = jStart + jStep;
        if (newI < 0 || newJ < 0 || newI >= puzzle.length || newJ >= puzzle[newI].length || puzzle[newI][newJ] != nextChar) {
            return 0;
        }
        if (pattern.length() == 1) {
            return 1;
        }
        String newPattern = pattern.substring(1);
        return countAdjacentMatchesInDirection(puzzle, newPattern, newI, newJ, iStep, jStep);
    }
}
