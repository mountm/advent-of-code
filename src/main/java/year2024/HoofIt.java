package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class HoofIt extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 10, false, 0);
        int puzzleSize = lines.get(0).length();
        int[] puzzle = new int[puzzleSize * puzzleSize];
        List<Integer> peakPositions = new ArrayList<>();
        int offset = 0;
        for (String line : lines) {
            for (int i = 0; i < line.length(); i++) {
                char nextChar = line.charAt(i);
                if (nextChar >= '0' && nextChar <= '9') {
                    puzzle[offset + i] = Integer.parseInt(String.valueOf(line.charAt(i)), 10);
                } else {
                    puzzle[offset + i] = 11;
                }
                if (puzzle[offset + i] == 9) {
                    peakPositions.add(offset + i);
                }
            }
            offset += puzzleSize;
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getTrailheadScores(puzzle, peakPositions, puzzleSize);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getTrailheadRatings(puzzle, puzzleSize);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int getTrailheadScores(int[] puzzle, List<Integer> peakPositions, int puzzleSize) {
        int sum = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                sum += getTrailheadScore(puzzle, i, peakPositions, puzzleSize);
            }
        }
        return sum;
    }

    private int getTrailheadRatings(int[] puzzle, int puzzleSize) {
        int sum = 0;
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                sum += getTrailheadRating(puzzle, i, puzzleSize);
            }
        }
        return sum;
    }

    private int getTrailheadRating(int[] puzzle, int startLoc, int puzzleSize) {
        return ratingClimb(puzzle, startLoc, 1, 0, puzzleSize);
    }

    private int getTrailheadScore(int[] puzzle, int startLoc, List<Integer> peakPositions, int puzzleSize) {
        int count = 0;
        for (boolean peak : climb(puzzle, startLoc, 1, peakPositions, new boolean[peakPositions.size()], puzzleSize)) {
            if (peak) count++;
        }
        return count;
    }

    private int ratingClimb(int[] puzzle, int startLoc, int nextHeight, int totalScore, int puzzleSize) {
        List<Integer> adjacent = List.of(startLoc - 1, startLoc + 1, startLoc - puzzleSize, startLoc + puzzleSize);
        for (int nextLoc : adjacent) {
            if (nextLoc >= 0 && nextLoc < puzzle.length && puzzle[nextLoc] == nextHeight && Math.abs((startLoc % puzzleSize) - (nextLoc % puzzleSize)) < 2) {
                if (nextHeight == 9) {
                    totalScore += 1;
                } else {
                    totalScore = ratingClimb(puzzle, nextLoc, nextHeight + 1, totalScore, puzzleSize);
                }
            }
        }
        return totalScore;
    }

    private boolean[] climb(int[] puzzle, int startLoc, int nextHeight, List<Integer> peakPositions, boolean[] reachedPeaks, int puzzleSize) {
        boolean reachedAllPeaks = true;
        for (boolean reached : reachedPeaks) {
            if (!reached) {
                reachedAllPeaks = false;
                break;
            }
        }
        if (reachedAllPeaks) return reachedPeaks;
        List<Integer> adjacent = List.of(startLoc - 1, startLoc + 1, startLoc - puzzleSize, startLoc + puzzleSize);
        for (int nextLoc : adjacent) {
            if (nextLoc >= 0 && nextLoc < puzzle.length && puzzle[nextLoc] == nextHeight && Math.abs((startLoc % puzzleSize) - (nextLoc % puzzleSize)) < 2) {
                if (nextHeight == 9) {
                    reachedPeaks[peakPositions.indexOf(nextLoc)] = true;
                } else {
                    reachedPeaks = climb(puzzle, nextLoc, nextHeight + 1, peakPositions, reachedPeaks, puzzleSize);
                }
            }
        }
        return reachedPeaks;
    }
}
