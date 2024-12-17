package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ChronospatialComputer extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 17, false, 0);
        long regA = 0L;
        List<Integer> program = new LinkedList<>();
        for (String line : lines) {
            if (line.contains("A")) {
                regA = Long.parseLong(line.split(": ")[1], 10);
            }
            else if (line.contains("Program")) {
                program.addAll(Arrays.stream(line.split(": ")[1].split(",")).map(e -> Integer.parseInt(e, 10)).collect(Collectors.toList()));
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOneCleverly(regA).stream().map(String::valueOf).collect(Collectors.joining(","));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(program);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long solvePartTwo(List<Integer> program) {
        // DFS. Iterate backwards, so you are starting with the values that end up in the most significant bits
        // stack values are pairs (L, R) such that a value R will produce all of the program steps from L to the end of the program.
        Deque<Pair<Integer, Long>> stack = new LinkedList<>();
        for (int test = 0; test <= 7; test++) {
            if (decompiledInstructions(test) == program.get(program.size() - 1)) {
                stack.add(Pair.of(program.size() - 1, (long) test));
            }
        }
        while(!stack.isEmpty()) {
            Pair<Integer, Long> entry = stack.removeFirst();
            Long val = entry.getRight();
            if (entry.getLeft() == 0) {
                return entry.getRight();
            }
            int valToMatch = program.get(entry.getLeft() - 1);
            for (int test = 0; test <= 7; test++) {
                if (decompiledInstructions((val << 3) + test) == valToMatch) {
                    stack.add(Pair.of(entry.getLeft() - 1, (val << 3) + test));
                }
            }
        }
        return -1;
    }

    private List<Long> solvePartOneCleverly(long regA) {
        List<Long> output = new ArrayList<>();
        while(regA > 0) {
            output.add(decompiledInstructions((int) regA));
            regA /= 8;
        }
        return output;
    }

    private long decompiledInstructions(long val) {
        return (((((val & 7) ^ 2) ^ 3) ^ (val >> ((val & 7) ^ 2))) & 7);
    }
}
