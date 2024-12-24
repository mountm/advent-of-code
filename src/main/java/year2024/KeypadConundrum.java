package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class KeypadConundrum extends AoCDay {

    private final Map<Character, Pair<Integer, Integer>> numPadCells = Map.ofEntries(
            Map.entry('7', Pair.of(0, 0)),
            Map.entry('8', Pair.of(0, 1)),
            Map.entry('9', Pair.of(0, 2)),
            Map.entry('4', Pair.of(1, 0)),
            Map.entry('5', Pair.of(1, 1)),
            Map.entry('6', Pair.of(1, 2)),
            Map.entry('1', Pair.of(2, 0)),
            Map.entry('2', Pair.of(2, 1)),
            Map.entry('3', Pair.of(2, 2)),
            Map.entry('0', Pair.of(3, 1)),
            Map.entry('A', Pair.of(3, 2))
    );

    private final Map<Character, Pair<Integer, Integer>> dirPadCells = Map.of(
            '^', Pair.of(0, 1),
            'A', Pair.of(0, 2),
            '<', Pair.of(1, 0),
            'v', Pair.of(1, 1),
            '>', Pair.of(1, 2)
    );

    // how many manual button presses does it take to move from one char to the next, N keypads deep (including manual activation)?
    private final Map<Pair<Integer, Pair<Character, Character>>, Long> distanceMap = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 21, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getTotalComplexity(lines, 3);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getTotalComplexity(lines, 26);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long getTotalComplexity(List<String> lines, int dirPadCount) {
        return lines.stream().map(line -> getLineComplexity(line, dirPadCount)).reduce(Long::sum).orElse(-1L);
    }

    private long getLineComplexity(String line, int dirPadCount) {
        return Long.parseLong(line.substring(0, 3), 10) * countSteps(line, dirPadCount, dirPadCount);
    }

    private long countSteps(String line, int dirPadCount, int totalDirPadCount) {
        long sum = 0;
        for (int i = 0; i < line.length(); i++) {
            sum += getShortestPathBetween((i == 0 ? 'A' : line.charAt(i-1)), line.charAt(i), dirPadCount, totalDirPadCount);
        }
        return sum;
    }

    private long getShortestPathBetween(char start, char end, int dirPadCount, int totalDirPadCount) {
        // dirPadCount includes the manually operated one
        // add one at the end to account for pressing A
        if (dirPadCount == 1) return getManhattanDistance(dirPadCells.get(start), dirPadCells.get(end)) + 1;
        Pair<Integer, Pair<Character, Character>> key = Pair.of(dirPadCount, Pair.of(start, end));
        if (distanceMap.containsKey(key)) return distanceMap.get(key);
        Set<String> possibleMoves = (dirPadCount == totalDirPadCount ? generateNumPadMoves(numPadCells.get(start), numPadCells.get(end)) : generateDirPadMoves(dirPadCells.get(start), dirPadCells.get(end)));
        long sum = possibleMoves.stream().map(line -> countSteps(line, dirPadCount - 1, totalDirPadCount)).min(Comparator.naturalOrder()).orElse(0L);
        distanceMap.put(key, sum);
        return sum;
    }


    private Set<String> generateNumPadMoves(Pair<Integer, Integer> currentPos, Pair<Integer, Integer> endPos) {
        // not sure if moving horizontally or vertically first is optimal
        // just check both (if they are both valid, i.e. avoiding empty cells)
        boolean vertFirstForced = (currentPos.getLeft() == 3 && endPos.getRight() == 0);
        boolean horzFirstForced = (currentPos.getRight() == 0 && endPos.getLeft() == 3);
        Set<String> result = new HashSet<>();
        if (!vertFirstForced) {
            result.add(generateMoves(currentPos, endPos, false));
        }
        if (!horzFirstForced) {
            result.add(generateMoves(currentPos, endPos, true));
        }
        return result;
    }

    private Set<String> generateDirPadMoves(Pair<Integer, Integer> currentPos, Pair<Integer, Integer> endPos) {
        // not sure if moving horizontally or vertically first is optimal
        // just check both (if they are both valid, i.e. avoiding empty cells)
        boolean vertFirstForced = (currentPos.getLeft() == 3 && endPos.getRight() == 0);
        boolean horzFirstForced = (currentPos.getRight() == 0 && endPos.getLeft() == 3);
        Set<String> result = new HashSet<>();
        if (!vertFirstForced) {
            result.add(generateMoves(currentPos, endPos, false));
        }
        if (!horzFirstForced) {
            result.add(generateMoves(currentPos, endPos, true));
        }
        return result;
    }

    private String generateMoves(Pair<Integer, Integer> currentPos, Pair<Integer, Integer> endPos, boolean vertFirst) {
        String result = "";
        if (vertFirst) {
            int vertDiff = endPos.getLeft() - currentPos.getLeft();
            while (vertDiff < 0) {
                result += "^";
                vertDiff++;
            }
            while (vertDiff > 0) {
                result += "v";
                vertDiff--;
            }
        }
        int horzDiff = endPos.getRight() - currentPos.getRight();
        while (horzDiff < 0) {
            result += '<';
            horzDiff++;
        }
        while (horzDiff > 0) {
            result += '>';
            horzDiff--;
        }
        if (!vertFirst) {
            int vertDiff = endPos.getLeft() - currentPos.getLeft();
            while (vertDiff < 0) {
                result += "^";
                vertDiff++;
            }
            while (vertDiff > 0) {
                result += "v";
                vertDiff--;
            }
        }
        return result + 'A';
    }
}
