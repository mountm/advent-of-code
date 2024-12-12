package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistorianHysteria extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<String> lines = readResourceFile(2024, 1, false, 0);
        for (String line : lines) {
            String[] vals = line.split(" {3}");
            list1.add(Integer.parseInt(vals[0], 10));
            list2.add(Integer.parseInt(vals[1], 10));
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getDiffs(new ArrayList<>(list1), new ArrayList<>(list2));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = calculateSimilarity(list1, list2);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int getDiffs(List<Integer> list1, List<Integer> list2) {
        Collections.sort(list1);
        Collections.sort(list2);
        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            sum += Math.abs(list1.get(i) - list2.get(i));
        }
        return sum;
    }

    private int calculateSimilarity(List<Integer> list1, List<Integer> list2) {
        Map<Integer, Long> list2Counts = list2.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        int sum = 0;
        for(int i : list1) {
            sum += i * list2Counts.getOrDefault(i, 0L);
        }
        return sum;
    }
}
