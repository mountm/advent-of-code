package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public class RestroomRedoubt extends AoCDay {
    private final int MAX_ITERATIONS = 10000;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 14, false, 0);
        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots = lines.stream().map(this::makeRobot).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = calculateSafetyFactor(robots, 100, 101, 103);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePart2(robots, 101, 103);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePart2(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots, int width, int height) {
        int lowestSafetyRating = Integer.MAX_VALUE;
        int result = 0;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            int safetyRating = calculateSafetyFactor(robots, i, width, height);
            if (safetyRating < lowestSafetyRating) {
                result = i;
                lowestSafetyRating = safetyRating;
            }
        }
        return result;
    }

    private int calculateSafetyFactor(List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> robots, int steps, int width, int height) {
        int[] quadrantCount = new int[4];
        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> robot : robots) {
            int finalX = (robot.getLeft().getLeft() + robot.getRight().getLeft() * steps) % width;
            int finalY = (robot.getLeft().getRight() + robot.getRight().getRight() * steps) % height;
            if (finalX < 0) finalX += width;
            if (finalY < 0) finalY += height;
            if (finalX < width / 2) {
                if (finalY < height / 2) {
                    quadrantCount[0]++;
                } else if (finalY > (height / 2)) {
                    quadrantCount[1]++;
                }
            } else if (finalX > (width / 2)) {
                if (finalY < height / 2) {
                    quadrantCount[2]++;
                } else if (finalY > (height / 2)) {
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
