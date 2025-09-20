package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WaitForIt extends AoCDay {

    double TINY_VALUE = 0.000001;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 6, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        Set<Pair<Long, Long>> races = generateRaces(lines);
        part1Answer = countWinningCombos(races);
        timeMarkers[2] = Instant.now().toEpochMilli();
        races = generateBigRace(lines);
        part2Answer = countWinningCombos(races);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Set<Pair<Long, Long>> generateBigRace(List<String> lines) {
        return Set.of(Pair.of(Long.parseLong(lines.get(0).split(": +")[1].replaceAll(" ", ""), 10), Long.parseLong(lines.get(1).split(": +")[1].replaceAll(" ", ""))));
    }

    private long countWinningCombos(Set<Pair<Long, Long>> races) {
        return races.stream().mapToLong(race ->
                Math.round(Math.floor((race.getLeft() + Math.sqrt(race.getLeft() * race.getLeft() - 4 * race.getRight()))/2 - TINY_VALUE)
                        - Math.ceil((race.getLeft() - Math.sqrt(race.getLeft() * race.getLeft() - 4 * race.getRight()))/2 + TINY_VALUE)) + 1
        ).reduce(1L, Math::multiplyExact);
    }

    private Set<Pair<Long, Long>> generateRaces(List<String> lines) {
        Set<Pair<Long, Long>> result = new HashSet<>();
        List<Long> times = Arrays.stream(lines.get(0).split(": +")[1].split(" +")).map(Long::parseLong).toList();
        List<Long> distances = Arrays.stream(lines.get(1).split(": +")[1].split(" +")).map(Long::parseLong).toList();
        for (int i = 0; i < times.size(); i++) {
            result.add(Pair.of(times.get(i), distances.get(i)));
        }
        return result;
    }
}
