package year2024;

import base.AoCDay;
import com.google.common.collect.Sets;
import org.javatuples.Triplet;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class LANParty extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 23, false, 0);
        Map<String, Set<String>> graph = buildGraph(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(graph);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(graph);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private String solvePartTwo(Map<String, Set<String>> graph) {
        Set<String> possibleClique = new HashSet<>();
        Set<String> partial = graph.keySet();
        Set<String> excluded = new HashSet<>();
        Set<String> maximumClique = new HashSet<>();
        runBronKerbosch(graph, possibleClique, partial, excluded, maximumClique);
        return maximumClique.stream().sorted().collect(Collectors.joining(","));
    }

    private void runBronKerbosch(Map<String, Set<String>> graph, Set<String> possibleClique, Set<String> partial, Set<String> excluded, Set<String> maximumClique) {
        if (partial.isEmpty() && excluded.isEmpty() && possibleClique.size() > maximumClique.size()) {
            maximumClique.clear();
            maximumClique.addAll(possibleClique);
        }
        for (String node : partial) {
            runBronKerbosch(graph, Sets.union(possibleClique, Set.of(node)), Sets.intersection(graph.get(node), partial), Sets.intersection(graph.get(node), excluded), maximumClique);
            partial = partial.stream().filter(e -> !e.equals(node)).collect(Collectors.toSet());
            excluded = Sets.union(excluded, Set.of(node));
        }
    }

    private int solvePartOne(Map<String, Set<String>> graph) {
        Set<Triplet<String, String, String>> triplets = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : graph.entrySet()) {
            if (entry.getKey().startsWith("t")) triplets.addAll(findTriplets(entry, graph));
        }
        return triplets.size();
    }

    private Set<Triplet<String, String, String>> findTriplets(Map.Entry<String, Set<String>> entry, Map<String, Set<String>> graph) {
        Set<Triplet<String, String, String>> output = new HashSet<>();
        for (String neighbor : entry.getValue()) {
            for (String doubleNeighbor : graph.get(neighbor)) {
                if (graph.get(doubleNeighbor).contains(entry.getKey())) {
                    output.add(Triplet.fromCollection(List.of(entry.getKey(), neighbor, doubleNeighbor).stream().sorted().collect(Collectors.toList())));
                }
            }
        }
        return output;
    }

    private Map<String, Set<String>> buildGraph(List<String> lines) {
        Map<String, Set<String>> output = new HashMap<>();
        for (String line : lines) {
            String[] computers = line.split("-");
            if (output.containsKey(computers[0])) {
                output.get(computers[0]).add(computers[1]);
            } else {
                Set<String> entry = new HashSet<>();
                entry.add(computers[1]);
                output.put(computers[0], entry);
            }
            if (output.containsKey(computers[1])) {
                output.get(computers[1]).add(computers[0]);
            } else {
                Set<String> entry = new HashSet<>();
                entry.add(computers[0]);
                output.put(computers[1], entry);
            }
        }
        return output;
    }
}
