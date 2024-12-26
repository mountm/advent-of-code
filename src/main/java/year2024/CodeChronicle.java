package year2024;

import base.AoCDay;
import com.google.common.collect.Sets;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CodeChronicle extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 25, false, 0);
        Set<List<Integer>> locks = new HashSet<>();
        Set<List<Integer>> keys = new HashSet<>();
        List<Integer> blankRowsIndices = new LinkedList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) blankRowsIndices.add(i);
        }
        for (int i = 0; i < blankRowsIndices.size() - 1; i++) {
            List<String> subList = lines.subList(blankRowsIndices.get(i)+1, blankRowsIndices.get(i+1));
            boolean isLock = subList.get(0).contains("#");
            if (!isLock) {
                Collections.reverse(subList);
            }
            int[] values = new int[5];
            for (int j = 1; j < subList.size(); j++) {
                for (int k = 0; k < values.length; k++) {
                    if (subList.get(j).charAt(k) == '#') values[k]++;
                }
            }
            if (isLock) {
                locks.add(Arrays.stream(values).boxed().collect(Collectors.toList()));
            } else {
                keys.add(Arrays.stream(values).boxed().collect(Collectors.toList()));
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(locks, keys);
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long solvePartOne(Set<List<Integer>> locks, Set<List<Integer>> keys) {
        return Sets.cartesianProduct(locks, keys).stream().filter(listOfLists -> IntStream.range(0, 5).map(i -> listOfLists.get(0).get(i) + listOfLists.get(1).get(i)).max().orElse(Integer.MAX_VALUE) < 6).count();
    }
}
