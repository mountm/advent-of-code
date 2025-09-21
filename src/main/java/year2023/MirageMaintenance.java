package year2023;

import base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MirageMaintenance extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 9, false, 0);
        List<List<Integer>> histories = lines.stream().map(l -> Arrays.stream(l.split(" +")).map(Integer::parseInt).toList()).toList();
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = addHistoryValues(histories, false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = addHistoryValues(histories, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long addHistoryValues(List<List<Integer>> lines, boolean goBackward) {
        return lines.stream().mapToLong(l -> findNextValue(l, goBackward)).sum();
    }

    private long findNextValue(List<Integer> list, boolean goBackward) {
        if (list.isEmpty()) return 0;
        if (list.stream().allMatch(e -> Objects.equals(e, list.get(0)))) return list.get(0);
        List<Integer> diffList = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            diffList.add(list.get(i+1) - list.get(i));
        }
        if (goBackward) {
            return list.get(0) - findNextValue(diffList, true);
        }
        return list.get(list.size()-1) + findNextValue(diffList, false);
    }
}
