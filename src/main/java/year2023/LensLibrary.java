package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;

public class LensLibrary extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 15, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = Arrays.stream(lines.get(0).split(",")).mapToInt(this::hashSteps).sum();
        timeMarkers[2] = Instant.now().toEpochMilli();
        Map<Integer, List<Pair<String, Integer>>> boxes = addLenses(lines.get(0).split(","));
        part2Answer = boxes.entrySet().stream().mapToInt(this::getFocusingPower).sum();
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int getFocusingPower(Map.Entry<Integer, List<Pair<String, Integer>>> entry) {
        int sum = 0;
        List<Pair<String, Integer>> lenses = entry.getValue();
        for (int i = 0; i < lenses.size(); i++) {
            sum += (i + 1) * lenses.get(i).getRight();
        }
        return sum * (entry.getKey() + 1);
    }


    private Map<Integer, List<Pair<String, Integer>>> addLenses(String[] steps) {
        Map<Integer, List<Pair<String, Integer>>> map = new HashMap<>();
            for (String step : steps) {
                int key = hashSteps(step.split("[-=]")[0]);
                if (step.endsWith("-") && map.containsKey(key)) {
                    List<Pair<String, Integer>> lenses = map.get(key);
                    lenses.removeIf(p -> p.getLeft().equals(step.replace("-", "")));
                }
                if (step.contains("=")) {
                    Pair<String, Integer> newLens = Pair.of(step.split("=")[0], Integer.parseInt(step.split("=")[1], 10));
                    if (map.containsKey(key)) {
                        List<Pair<String, Integer>> lenses = map.get(key);
                        boolean replaced = false;
                        for (int i = 0; i < lenses.size(); i++) {
                            if (lenses.get(i).getLeft().equals(newLens.getLeft())) {
                                lenses.set(i, newLens);
                                replaced = true;
                            }
                        }
                        if (!replaced) lenses.add(newLens);
                    } else {
                        List<Pair<String, Integer>> lenses = new LinkedList<>();
                        lenses.add(newLens);
                        map.put(key, lenses);
                    }
                }
            }
        return map;
    }

    private int hashSteps(String s) {
        return s.chars().reduce(0, this::applyHash);
    }

    private int applyHash(int currentVal, int nextCode) {
        currentVal += nextCode;
        currentVal *= 17;
        return currentVal % 256;
    }
}
