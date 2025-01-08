package year2024;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RedNosedReports extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 2, false, 0);
        Set<List<Integer>> reports = new HashSet<>();
        lines.forEach(line -> reports.add(convertStringsToInts(List.of(line.split(" ")))));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = countSafeReports(new HashSet<>(reports), false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = countSafeReports(reports, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int countSafeReports(Set<List<Integer>> reports, boolean canDelete) {
        int count = 0;
        for(List<Integer> report : reports) {
            List<Integer> diffs = new ArrayList<>();
            for (int i = 0; i < report.size() - 1; i++) {
                diffs.add(report.get(i+1) - report.get(i));
            }
            if (diffs.stream().allMatch(e -> (e >= 1 && e <= 3)) || diffs.stream().allMatch(e -> (e >= -3 && e<= -1))) {
                count++;
            } else if (canDelete) {
                for (int i = 0; i < report.size(); i++) {
                    List<Integer> reportCopy = new ArrayList<>(report);
                    reportCopy.remove(i);
                    diffs = new ArrayList<>();
                    for (int j = 0; j < reportCopy.size() - 1; j++) {
                        diffs.add(reportCopy.get(j+1) - reportCopy.get(j));
                    }
                    if (diffs.stream().allMatch(e -> (e >= 1 && e <= 3)) || diffs.stream().allMatch(e -> (e >= -3 && e<= -1))) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
}