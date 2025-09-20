package year2023;

import base.AoCDay;
import org.javatuples.Triplet;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CubeConundrum extends AoCDay {

    Pattern BLUE = Pattern.compile("(\\d+) blue");
    Pattern RED = Pattern.compile("(\\d+) red");
    Pattern GREEN = Pattern.compile("(\\d+) green");
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 2, false, 0);
        Map<Long, Set<Triplet<Long, Long, Long>>> games = parseGames(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = games.entrySet().stream().filter(e -> isValidGame(e.getValue())).mapToLong(Map.Entry::getKey).sum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = games.values().stream().mapToLong(this::getGamePower).sum();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long getGamePower(Set<Triplet<Long, Long, Long>> triplets) {
        Triplet<Long, Long, Long> smallestRequired = triplets.stream().reduce(Triplet.with(0L, 0L, 0L), this::compareTriplets);
        return smallestRequired.getValue0() * smallestRequired.getValue1() * smallestRequired.getValue2();
    }

    private Triplet<Long, Long, Long> compareTriplets(Triplet<Long, Long, Long> t1, Triplet<Long, Long, Long> t2) {
        return Triplet.with(Math.max(t1.getValue0(), t2.getValue0()), Math.max(t1.getValue1(), t2.getValue1()), Math.max(t1.getValue2(), t2.getValue2()));
    }

    private boolean isValidGame(Set<Triplet<Long, Long, Long>> removals) {
        return removals.stream().allMatch(t -> t.getValue0() <= 12 && t.getValue1() <= 13 && t.getValue2() <= 14);
    }

    private Map<Long, Set<Triplet<Long, Long, Long>>> parseGames(List<String> lines) {
        Map<Long, Set<Triplet<Long, Long, Long>>> result = new HashMap<>();
        long id = 1L;
        for (String l : lines) {
            result.put(id++, Arrays.stream(l.split(";")).map(this::getTriplet).collect(Collectors.toSet()));
        }
        return result;
    }

    private Triplet<Long, Long, Long> getTriplet(String s) {
        long blueCount = 0L, redCount = 0L, greenCount = 0L;
        Matcher m = BLUE.matcher(s);
        if (m.find()) {
            blueCount = Long.parseLong(m.group(1), 10);
        }
        m = RED.matcher(s);
        if (m.find()) {
            redCount = Long.parseLong(m.group(1), 10);
        }
        m = GREEN.matcher(s);
        if (m.find()) {
            greenCount = Long.parseLong(m.group(1), 10);
        }
        return Triplet.with(redCount, greenCount, blueCount);
    }
}
