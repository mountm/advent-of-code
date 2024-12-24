package year2024;

import base.AoCDay;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

    private final Map<Pair<Character, Character>, Set<String>> numPadMoves = new HashMap<>();
    private final Map<Pair<Character, Character>, Set<String>> dirPadMoves = new HashMap<>();

    // how many manual button presses does it take to execute a given sequence of moves, N keypads deep?
    private final Map<Pair<Integer, String>, Long> moveCache = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 21, false, 0);
        findNumPadMoves();
        findDirPadMoves();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getTotalComplexity(lines, 2);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getTotalComplexity(lines, 25);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private void findNumPadMoves() {
        for (char from : numPadCells.keySet()) {
            for (char to : numPadCells.keySet()) {
                if (from != to) {
                    numPadMoves.put(Pair.of(from, to), generateNumPadMoves(numPadCells.get(from), numPadCells.get(to)));
                } else {
                    numPadMoves.put(Pair.of(from, to), Set.of("A"));
                }
            }
        }
    }

    private void findDirPadMoves() {
        for (char from : dirPadCells.keySet()) {
            for (char to : dirPadCells.keySet()) {
                if (from != to) {
                    dirPadMoves.put(Pair.of(from, to), generateDirPadMoves(dirPadCells.get(from), dirPadCells.get(to)));
                } else {
                    dirPadMoves.put(Pair.of(from, to), Set.of("A"));
                }
            }
        }
    }

    private long getTotalComplexity(List<String> lines, int dirPadCount) {
        return lines.stream().map(line -> getLineComplexity(line, dirPadCount)).reduce(Long::sum).orElse(-1L);
    }

    private long getLineComplexity(String line, int dirPadCount) {
        Set<String> possibleNumpadMoves = getPossibleNumpadMoves(line);
        return Long.parseLong(line.substring(0, 3), 10) * possibleNumpadMoves.stream().map(instructions -> countSteps(instructions, dirPadCount)).min(Comparator.naturalOrder()).orElse(0L);
    }

    private Set<String> getPossibleNumpadMoves(String line) {
        Set<String> output = new HashSet<>();
        output.add("");
        for (int i = 0; i < line.length(); i++) {
            output = Sets.cartesianProduct(output, numPadMoves.get(Pair.of((i == 0 ? 'A' : line.charAt(i - 1)), line.charAt(i)))).stream().map(e -> e.stream().reduce(String::concat).orElse("")).collect(Collectors.toSet());
        }
        return output;
    }

    private long countSteps(String line, int dirPadCount) {
        if (dirPadCount == 0) return line.length();
        Pair<Integer, String> key = Pair.of(dirPadCount, line);
        if (moveCache.containsKey(key)) return moveCache.get(key);
        long sum = 0;
        String[] moves = line.split("A");
        for (String move : moves) {
            Set<String> output = new HashSet<>();
            output.add("");
            for (int i = 0; i <= move.length(); i++) {
                // losing As in the regex, so you have to put them back in here
                output = Sets.cartesianProduct(output, dirPadMoves.get(
                        Pair.of(
                                (i == 0 ? 'A' : move.charAt(i - 1)),
                                (i == move.length() ? 'A' : move.charAt(i)))
                )).stream().map(e -> e.stream().reduce(String::concat).orElse("")).collect(Collectors.toSet());
            }
            sum += output.stream().map(e -> countSteps(e, dirPadCount - 1)).min(Comparator.naturalOrder()).orElse(0L);
        }
        moveCache.put(key, sum);
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
        boolean vertFirstForced = (currentPos.getLeft() == 0 && endPos.getRight() == 0);
        boolean horzFirstForced = (currentPos.getRight() == 0 && endPos.getLeft() == 0);
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
        StringBuilder result = new StringBuilder();
        if (vertFirst) {
            int vertDiff = endPos.getLeft() - currentPos.getLeft();
            while (vertDiff < 0) {
                result.append("^");
                vertDiff++;
            }
            while (vertDiff > 0) {
                result.append("v");
                vertDiff--;
            }
        }
        int horzDiff = endPos.getRight() - currentPos.getRight();
        while (horzDiff < 0) {
            result.append('<');
            horzDiff++;
        }
        while (horzDiff > 0) {
            result.append('>');
            horzDiff--;
        }
        if (!vertFirst) {
            int vertDiff = endPos.getLeft() - currentPos.getLeft();
            while (vertDiff < 0) {
                result.append("^");
                vertDiff++;
            }
            while (vertDiff > 0) {
                result.append("v");
                vertDiff--;
            }
        }
        return result.toString() + 'A';
    }
}
