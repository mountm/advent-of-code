package year2024;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrintQueue extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 5, false, 0);
        Map<Integer, Set<Integer>> rules = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();
        for (String line : lines) {
            if (line.contains("|")) {
                String[] currentLine = line.split("\\|");
                int val1 = Integer.parseInt(currentLine[0], 10);
                if (rules.containsKey(val1)) {
                    rules.get(val1).add(Integer.parseInt(currentLine[1], 10));
                } else {
                    Set<Integer> entry = new HashSet<>();
                    entry.add(Integer.parseInt(currentLine[1], 10));
                    rules.put(val1, entry);
                }
            } else if (line.length() > 0) {
                updates.add(Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = sumValidUpdates(copyNestedList(updates), rules);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = sumFixableUpdates(updates, rules);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int sumFixableUpdates(List<List<Integer>> updates, Map<Integer, Set<Integer>> rules) {
        int sum = 0;
        for (List<Integer> update : updates) {
            boolean didModify = false;
            for (int i = 1; i < update.size(); i++) {
                int val = update.get(i);
                Set<Integer> subset = new HashSet<>(update.subList(0, i));
                subset.retainAll(rules.getOrDefault(val, Set.of()));
                if (!subset.isEmpty()) {
                    didModify = true;
                    int lowestIdx = subset.stream().map(update::indexOf).filter(e-> e >= 0).min(Comparator.naturalOrder()).orElse(-1);
                    update.remove(i);
                    update.add(lowestIdx, val);
                    i = 0;
                }
            }
            if (didModify) {
                sum += (update.get(update.size() / 2));
            }
        }
        return sum;
    }

    private int sumValidUpdates(List<List<Integer>> updates, Map<Integer, Set<Integer>> rules) {
        int sum = 0;
        for (List<Integer> update : updates) {
            boolean didModify = false;
            for (int i = 1; i < update.size(); i++) {
                int val = update.get(i);
                Set<Integer> subset = new HashSet<>(update.subList(0, i));
                subset.retainAll(rules.getOrDefault(val, Set.of()));
                if (!subset.isEmpty()) {
                    didModify = true;
                    break;
                }
            }
            if (!didModify) {
                sum += (update.get(update.size() / 2));
            }
        }
        return sum;
    }
}