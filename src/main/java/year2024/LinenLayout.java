package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.*;

public class LinenLayout extends AoCDay {

    Map<String, Long> waysToReach = new HashMap<>();
    
    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 19, false, 0);
        Set<String> availablePatterns = Set.of(lines.get(0).split(", "));
        Set<String> wantedDesigns = new HashSet<>(lines.subList(2, lines.size()));
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = wantedDesigns.stream().filter(design -> countWaysToReach(design, availablePatterns) > 0).count();
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = wantedDesigns.stream().map(design -> countWaysToReach(design, availablePatterns)).reduce(Long::sum).orElse(-1L);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long countWaysToReach(String design, Set<String> availablePatterns) {
        if (design.equals("")) return 1;
        if (!waysToReach.containsKey(design)) {
            long count = availablePatterns.stream().filter(design::startsWith).map(e -> countWaysToReach(design.substring(e.length()), availablePatterns)).reduce(Long::sum).orElse(0L);
            waysToReach.put(design, count);
            return count;
        }
        return waysToReach.get(design);
    }
}
