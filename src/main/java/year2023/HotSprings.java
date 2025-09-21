package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

public class HotSprings extends AoCDay {

    Map<Pair<String, List<Integer>>, Long> cache = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 12, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = lines.stream().mapToLong(this::countPossibleArrangements).sum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = lines.stream().map(this::unfold).mapToLong(this::countPossibleArrangements).sum();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private String unfold(String line) {
        String condition = line.split(" +")[0];
        String groups = line.split(" +")[1];
        return condition + "?" + condition + "?" + condition + "?" + condition + "?" + condition + " " + groups + "," + groups + "," + groups + "," + groups + "," + groups;
    }

    private long countPossibleArrangements(String line) {
        String conditions = line.split(" +")[0];
        while (conditions.endsWith(".")) conditions = conditions.substring(0, conditions.length() - 1);
        List<Integer> groups = Arrays.stream(line.split(" +")[1].split(",")).map(Integer::parseInt).toList();
        return countPossibleArrangements(conditions, groups);
    }

    private long countPossibleArrangements(String conditions, List<Integer> groups) {
        while (conditions.startsWith(".")) conditions = conditions.substring(1);
        // empty string and no groups left? fine
        if (conditions.isEmpty()) {
            return groups.isEmpty() ? 1L : 0L;
        }
        // empty groups and no broken springs left? fine
        if (groups.isEmpty()) {
            return conditions.contains("#") ? 0L : 1L;
        }
        // not enough springs left for the group? fail
        if (conditions.length() < groups.get(0)) return 0L;
        Pair<String, List<Integer>> key = Pair.of(conditions, groups);
        if (cache.containsKey(key)) return cache.get(key);
        long result = 0L;
        if (conditions.startsWith("?")) {
            result = countPossibleArrangements(conditions.replaceFirst("\\?", "#"), groups) + countPossibleArrangements(conditions.replaceFirst("\\?", "."), groups);
        } else {
            // starts with #
            // must not have any . in the first (group size) characters
            // and after that, must have a . or end of string
            if (!conditions.substring(0, groups.get(0)).contains(".")) {
                if (conditions.length() == groups.get(0)) return groups.size() == 1 ? 1L : 0L;
                if (conditions.charAt(groups.get(0)) == '#') return 0L;
                if (conditions.charAt(groups.get(0)) == '?') {
                    result = countPossibleArrangements(conditions.substring(groups.get(0)).replaceFirst("\\?", "."), groups.subList(1, groups.size()));
                } else {
                    result = countPossibleArrangements(conditions.substring(groups.get(0)), groups.subList(1, groups.size()));
                }
            }
        }
        cache.put(key, result);
        return result;
    }


}
