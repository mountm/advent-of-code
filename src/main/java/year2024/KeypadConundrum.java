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

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 21, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePartOne(List<String> lines) {
        return lines.stream().map(this::getLineComplexity).reduce(Integer::sum).orElse(-1);
    }

    private int getLineComplexity(String line) {
        return Integer.parseInt(line.substring(0, 3), 10) * countSteps(line);
    }

    private int countSteps(String line) {
        Set<String> validCommandStrings = getCommandSequencesFromNumPadString(line);
        return getCommandSequenceFromDirPadStrings(getCommandSequenceFromDirPadStrings(validCommandStrings)).stream().map(String::length).min(Comparator.naturalOrder()).orElse(-1);
    }

    private Set<String> getCommandSequencesFromNumPadString(String input) {
        Set<String> results = new HashSet<>();
        results.add("");
        Pair<Integer, Integer> currentPos = numPadCells.get('A');
        for (char c : input.toCharArray()) {
            Pair<Integer, Integer> nextPos = numPadCells.get(c);
            results = Sets.cartesianProduct(results, generateNumPadMoves(currentPos, nextPos)).stream().map(e -> e.stream().reduce(String::concat).orElse("")).collect(Collectors.toSet());
            currentPos = nextPos;
        }
        return results;
    }

    private Set<String> getCommandSequenceFromDirPadStrings(Set<String> inputs) {
        Set<String> output = new HashSet<>();
        for (String input : inputs) {
            Set<String> results = new HashSet<>();
            results.add("");
            Pair<Integer, Integer> currentPos = dirPadCells.get('A');
            for (char c : input.toCharArray()) {
                Pair<Integer, Integer> nextPos = dirPadCells.get(c);
                results = Sets.cartesianProduct(results, generateDirPadMoves(currentPos, nextPos)).stream().map(e -> e.stream().reduce(String::concat).orElse("")).collect(Collectors.toSet());
                currentPos = nextPos;
            }
            output.addAll(results);
        }
        return output;
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

    private Set<String> generateNumPadMoves(Pair<Integer, Integer> currentPos, Pair<Integer, Integer> endPos) {
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
}
