package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class LinenLayout extends AoCDay {

    
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 19, false, 0);
        Set<String> availablePatterns = Set.of(lines.get(0).split(", "));
        Set<String> wantedDesigns = new HashSet<>(lines.subList(2, lines.size()));
         Pair<Set<String>, Map<String, Long>> searchOutput = getPossibleDesigns(availablePatterns, wantedDesigns);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = searchOutput.getLeft().size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = countWaysToReach(searchOutput.getLeft(), searchOutput.getRight());
        timeMarkers[3] = Instant.now().toEpochMilli();
    }


    private long countWaysToReach(Set<String> designs, Map<String, Long> waysToReach) {
        return designs.stream().map(waysToReach::get).reduce(Long::sum).orElse(-1L);
    }

    private Pair<Set<String>, Map<String, Long>> getPossibleDesigns(Set<String> availablePatterns, Set<String> wantedDesigns) {
        Set<String> validDesigns = new HashSet<>();
        Map<String, Long> waysToReach = new HashMap<>();
        waysToReach.put("", 1L);
        PriorityQueue<String> frontier = new PriorityQueue<>();
        frontier.add("");
        Set<String> reached = new HashSet<>();
        reached.add("");

        while (!frontier.isEmpty()) {
            String current = frontier.poll();
            if (wantedDesigns.contains(current)) {
                validDesigns.add(current);
            }
            if (wantedDesigns.stream().noneMatch(e -> e.startsWith(current))) continue;
            for (String pattern : availablePatterns) {
                String testPattern = current + pattern;
                if (wantedDesigns.stream().noneMatch(e -> e.startsWith(testPattern))) continue;
                if (!reached.contains(testPattern)) {
                    frontier.add(testPattern);
                    reached.add(testPattern);
                    waysToReach.put(testPattern, waysToReach.get(current));
                } else {
                    waysToReach.merge(testPattern, waysToReach.get(current), Long::sum);
                }
            }
        }
        return Pair.of(validDesigns, waysToReach);
    }

}
