package Day10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10Runner {
    public static final int PUZZLE_SIZE = 40;
    public static final int[] puzzle = new int[PUZZLE_SIZE * PUZZLE_SIZE];
    public static final List<Integer> peakPositions = new ArrayList<>();
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        int offset = 0;
        try {
            input = new BufferedReader(new FileReader("inputs/topographic-map.txt"));
            while ((currentLine = input.readLine()) != null) {
                for (int i = 0; i < PUZZLE_SIZE; i++) {
                    char nextChar = currentLine.charAt(i);
                    if (nextChar >= '0' && nextChar <= '9') {
                        puzzle[offset + i] = Integer.parseInt(String.valueOf(currentLine.charAt(i)), 10);
                    }
                    else {
                        puzzle[offset + i] = 11;
                    }
                    if (puzzle[offset + i] == 9) {
                        peakPositions.add(offset + i);
                    }
                }
                offset += PUZZLE_SIZE;
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
        int sum = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                sum += getTrailheadScore(i);
            }
        }
        System.out.println(sum);
    }

    private static int getTrailheadScore(int startLoc) {
        return climb(startLoc, 1, 0);
    }

    private static int climb(int startLoc, int nextHeight, int totalScore) {
        List<Integer> adjacent = List.of(startLoc - 1, startLoc + 1, startLoc - PUZZLE_SIZE, startLoc + PUZZLE_SIZE);
        for (int nextLoc : adjacent) {
            if (nextLoc >= 0 && nextLoc < puzzle.length && puzzle[nextLoc] == nextHeight && Math.abs((startLoc % PUZZLE_SIZE) - (nextLoc % PUZZLE_SIZE)) < 2) {
                if (nextHeight == 9) {
                    totalScore += 1;
                } else {
                    totalScore = climb(nextLoc, nextHeight + 1, totalScore);
                }
            }
        }
        return totalScore;
    }
}
