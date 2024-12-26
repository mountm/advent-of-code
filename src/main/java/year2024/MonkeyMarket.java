package year2024;

import base.AoCDay;
import org.javatuples.Quartet;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MonkeyMarket extends AoCDay {

    private final int NUM_STEPS = 2000;

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<Integer> lines = readResourceFile(2024, 22, false, 0).stream().map(Integer::parseInt).collect(Collectors.toList());
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = solvePartOne(lines);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = solvePartTwo(lines);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private int solvePartTwo(List<Integer> lines) {
        Map<Quartet<Integer, Integer, Integer, Integer>, Integer> salePrices = new HashMap<>();
        for (int startingVal : lines) {
            makeSalePriceMap(startingVal).forEach((key, value) -> salePrices.merge(key, value, Integer::sum));
        }
        return salePrices.values().stream().max(Comparator.naturalOrder()).orElse(-1);
    }

    private Map<Quartet<Integer, Integer, Integer, Integer>, Integer> makeSalePriceMap(int startingVal) {
        Map<Quartet<Integer, Integer, Integer, Integer>, Integer> output = new HashMap<>();
        List<Integer> prices = getPrices(startingVal);
        List<Integer> priceChanges = getPriceChanges(startingVal);
        for (int i = 0; i < priceChanges.size() - 3; i++) {
            Quartet<Integer, Integer, Integer, Integer> key = Quartet.with(priceChanges.get(i), priceChanges.get(i+1), priceChanges.get(i+2), priceChanges.get(i+3));
            if (!output.containsKey(key)) {
                output.put(key, prices.get(i+3));
            }
        }
        return output;
    }

    private List<Integer> getPrices(int startingVal) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < NUM_STEPS; i++) {
            startingVal = getNextValue(startingVal);
            list.add((startingVal % 10));
        }
        return list;
    }

    private List<Integer> getPriceChanges(int startingVal) {
        List<Integer> list = new LinkedList<>();
        int nextVal;
        for (int i = 0; i < NUM_STEPS; i++) {
            nextVal = getNextValue(startingVal);
            list.add((nextVal % 10) - (startingVal % 10));
            startingVal = nextVal;
        }
        return list;
    }

    private long solvePartOne(List<Integer> lines) {
        long sum = 0;
        for (int val : lines) {
            for (int i = 0; i < NUM_STEPS; i++) {
                val = getNextValue(val);
            }
            sum += val;
        }
        return sum;
    }

    private int getNextValue(int val) {
        val = ((val << 6) ^ val) & 16777215;
        val = ((val >>> 5) ^ val) & 16777215;
        return ((val << 11) ^ val) & 16777215;
    }
}
