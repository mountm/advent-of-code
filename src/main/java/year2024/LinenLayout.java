package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.*;

public class LinenLayout extends AoCDay {
    
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 19, false, 0);
        List<String> availablePatterns = List.of(lines.get(0).split(", "));
        Set<String> wantedDesigns = new HashSet<>(lines.subList(2, lines.size()));
        timeMarkers[1] = Instant.now().toEpochMilli();
        Set<String> validDesigns = getPossibleDesigns(availablePatterns, wantedDesigns);
        part1Answer = validDesigns.size();
        timeMarkers[2] = Instant.now().toEpochMilli();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private Set<String> getPossibleDesigns(List<String> availablePatterns, Set<String> wantedDesigns) {
        Set<String> validDesigns = new HashSet<>();
        Deque<String> frontier = new LinkedList<>();
        frontier.add("");
        Set<String> reached = new HashSet<>();
        reached.add("");

        while (!frontier.isEmpty()) {
            String current = frontier.pollFirst();
            if (wantedDesigns.remove(current)) {
                validDesigns.add(current);
            }
            if (wantedDesigns.isEmpty()) break;
            if (wantedDesigns.stream().noneMatch(e -> e.startsWith(current))) continue;
            for (String pattern : availablePatterns) {
                if (!reached.contains(current + pattern)) {
                    frontier.addLast(current + pattern);
                    reached.add(current + pattern);
                }
            }
        }
        return validDesigns;
    }

}
