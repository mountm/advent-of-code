package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;


public class RestroomRedoubt extends AoCDay {
    private final int PUZZLE_WIDTH = 101;
    private final int PUZZLE_HEIGHT = 103;
    private final int MAX_ITERATIONS = Math.max(PUZZLE_WIDTH, PUZZLE_HEIGHT) * 3;


    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 14, false, 0);
        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots = lines.stream().map(this::makeRobot).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = calculateSafetyFactor(robots, 100, PUZZLE_WIDTH, PUZZLE_HEIGHT);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePart2(robots, PUZZLE_WIDTH, PUZZLE_HEIGHT);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePart2(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots, int width, int height) {
        Map<Integer, Integer> valsInDensestRow = new HashMap<>();
        Map<Integer, Integer> valsInDensestColumn = new HashMap<>();
        int maxValsInDensestRow = 0;
        int maxValsInDensestColumn = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            int finalI = i;
            List<Pair<Integer, Integer>> endCoords = robots.stream().map(r -> getEndingCoords(r, finalI, width, height)).collect(Collectors.toList());
            valsInDensestRow.put(i, getModeCount(endCoords.stream().map(Pair::getRight).collect(Collectors.toList())));
            valsInDensestColumn.put(i, getModeCount(endCoords.stream().map(Pair::getLeft).collect(Collectors.toList())));
            if (valsInDensestRow.get(i) > maxValsInDensestRow) {
                maxValsInDensestRow = valsInDensestRow.get(i);
            } if (valsInDensestColumn.get(i) > maxValsInDensestColumn) {
                maxValsInDensestColumn = valsInDensestColumn.get(i);
            }
        }
        Pair<Integer, Integer> horzOffsetAndPeriod = findOffsetAndPeriod(valsInDensestRow);
        Pair<Integer, Integer> vertOffsetAndPeriod = findOffsetAndPeriod(valsInDensestColumn);

        return findIntersection(horzOffsetAndPeriod, vertOffsetAndPeriod);
    }

    private int findIntersection(Pair<Integer, Integer> horzOffsetAndPeriod, Pair<Integer, Integer> vertOffsetAndPeriod) {
        int longerPeriod, longerPeriodOffset, shorterPeriod, shorterPeriodOffset;
        if (horzOffsetAndPeriod.getRight() > vertOffsetAndPeriod.getRight()) {
            longerPeriod = horzOffsetAndPeriod.getRight();
            longerPeriodOffset = horzOffsetAndPeriod.getLeft();
            shorterPeriod = vertOffsetAndPeriod.getRight();
            shorterPeriodOffset = vertOffsetAndPeriod.getLeft();
        } else {
            longerPeriod = vertOffsetAndPeriod.getRight();
            longerPeriodOffset = vertOffsetAndPeriod.getLeft();
            shorterPeriod = horzOffsetAndPeriod.getRight();
            shorterPeriodOffset = horzOffsetAndPeriod.getLeft();
        }
        // final iteration count must equal the longer period offset plus a multiple of the longer period
        // so it is part of the sequence (offset, offset + period, offset + 2*period, ...)
        // it suffices to check that sequence for the first value that yields the correct remainder modulo the shorter period
        return Stream.iterate(longerPeriodOffset, n -> n + longerPeriod).filter(e -> e % shorterPeriod == shorterPeriodOffset).findFirst().orElse(0);
    }

    private Pair<Integer, Integer> findOffsetAndPeriod(Map<Integer, Integer> varianceMap) {
        int highestVal = varianceMap.values().stream().max(Comparator.naturalOrder()).orElse(0);
        List<Integer> firstTwoOccurrences = varianceMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).filter(e -> e.getValue() == highestVal).map(Map.Entry::getKey).limit(2).collect(Collectors.toList());
        int offset = firstTwoOccurrences.get(0);
        int period = firstTwoOccurrences.get(1) - firstTwoOccurrences.get(0);
        return Pair.of(offset, period);
    }

    private int getModeCount(List<Integer> values) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int value : values) {
            freqMap.put(value, freqMap.getOrDefault(value, 0) + 1);
        }
        return freqMap.values().stream().max(Comparator.naturalOrder()).orElse(0);
    }

    private Pair<Integer, Integer> getEndingCoords(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robot, int steps, int width, int height) {
        int finalX = (robot.getLeft().getLeft() + robot.getRight().getLeft() * steps) % width;
        int finalY = (robot.getLeft().getRight() + robot.getRight().getRight() * steps) % height;
        if (finalX < 0) finalX += width;
        if (finalY < 0) finalY += height;
        return Pair.of(finalX, finalY);
    }

    private int calculateSafetyFactor(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots, int steps, int width, int height) {
        int[] quadrantCount = new int[4];
        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robot : robots) {
            Pair<Integer, Integer> endingCoords = getEndingCoords(robot, steps, width, height);
            if (endingCoords.getLeft() < width / 2) {
                if (endingCoords.getRight() < height / 2) {
                    quadrantCount[0]++;
                } else if (endingCoords.getRight() > (height / 2)) {
                    quadrantCount[1]++;
                }
            } else if (endingCoords.getLeft() > (width / 2)) {
                if (endingCoords.getRight() < height / 2) {
                    quadrantCount[2]++;
                } else if (endingCoords.getRight() > (height / 2)) {
                    quadrantCount[3]++;
                }
            }
        }
        return Arrays.stream(quadrantCount).reduce(1, (prev, next) -> prev * next);
    }

    private Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> makeRobot(String line) {
        Pair<Integer, Integer> position = Pair.of(parseInt(line.split("=")[1].split(",")[0], 10), parseInt(line.split(",")[1].split(" ")[0], 10));
        Pair<Integer, Integer> velocity = Pair.of(parseInt(line.split("=")[2].split(",")[0], 10), parseInt(line.split(",")[2], 10));
        return Pair.of(position, velocity);
    }
}
