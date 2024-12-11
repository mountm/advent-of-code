package year2024.Day4;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day4Runner extends AoCDay {
    static Map<Integer, Set<Integer>> rules = new HashMap<>();
    static List<List<Integer>> updates = new ArrayList<>();
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        try {
            input = new BufferedReader(new FileReader("inputs/safetymanuals.txt"));
            while ((currentLine = input.readLine()) != null) {
                System.out.println("Raw line: " + currentLine);
                if (currentLine.contains("|")) { // this is a rule
                    String[] line = currentLine.split("\\|");
                    int val1 = Integer.parseInt(line[0], 10);
                    if (rules.containsKey(val1)) {
                        rules.get(val1).add(Integer.parseInt(line[1], 10));
                    } else {
                        Set<Integer> entry = new HashSet<>();
                        entry.add(Integer.parseInt(line[1], 10));
                        rules.put(val1, entry);
                    }
                } else if (currentLine.length() > 0 ){ // this is an update
                    updates.add(Arrays.stream(currentLine.split(",")).map(Integer::parseInt).collect(Collectors.toList()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AtomicInteger sum = new AtomicInteger();
        updates.forEach(update -> {
            System.out.println(update);
            boolean didModify = false;
            for (int i = 1; i < update.size(); i++) {
                int val = update.get(i);
                Set<Integer> subset = new HashSet<>(update.subList(0, i));
                subset.retainAll(getRulesForValue(val));
                if (!subset.isEmpty()) {
                    didModify = true;
                    int lowestIdx = subset.stream().map(update::indexOf).filter(e-> e >= 0).min(Comparator.naturalOrder()).orElse(-1);
                    update.remove(i);
                    update.add(lowestIdx, val);
                    i = 0;
                }
            }
            System.out.println("Valid. Middle value is " + update.get(update.size() / 2));
            if (didModify) {
                sum.addAndGet(update.get(update.size() / 2));
            }
        });
        System.out.println(sum.get());
    }
    public static Set<Integer> getRulesForValue(int val) {
        return rules.getOrDefault(val, Set.of());
    }
}