package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class PlutonianPebbles extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 11, false, 0);
        List<Long> stones = Arrays.stream(lines.get(0).split(" ")).map(Long::parseLong).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getStoneCount(stones, 25);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = getStoneCount(stones, 75);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long getStoneCount(List<Long> stones, int blinkCount) {
        Map<Long, Long> stoneCount = new HashMap<>();
        for (Long val : new HashSet<>(stones)) {
            stoneCount.put(val, stones.stream().filter((e -> e.equals(val))).count());
        }

        for (int i = 0; i < blinkCount; i++) {
            Map<Long, Long> newStoneCount = new HashMap<>();
            for (Map.Entry<Long, Long> entry: stoneCount.entrySet()) {
                long key = entry.getKey();
                if (key == 0L) {
                    newStoneCount.merge(1L, entry.getValue(), Long::sum);
                } else if (String.valueOf(key).length() % 2 == 0) {
                    long splitPoint = 1;
                    for (int j = 0; j < String.valueOf(key).length() / 2; j++) {
                        splitPoint *= 10;
                    }
                    newStoneCount.merge(key / splitPoint, entry.getValue(), Long::sum);
                    newStoneCount.merge(key % splitPoint, entry.getValue(), Long::sum);
                } else {
                    newStoneCount.merge(key * 2024, entry.getValue(), Long::sum);
                }
                stoneCount = newStoneCount;
            }
        }
        return getSize(stoneCount);
    }

    private long getSize(Map<Long, Long> stoneCount) {
        return stoneCount.values().stream().reduce(0L, Long::sum);
    }
}
