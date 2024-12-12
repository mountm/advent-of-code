package year2024;

import base.AoCDay;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BridgeRepair extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 7, false, 0);
        List<List<Long>> equations = new ArrayList<>();
        for (String line : lines) {
            List<Long> equation = new ArrayList<>();
            equation.add(Long.parseLong(line.split(":")[0],10));
            equation.addAll(Arrays.stream(line.split(":")[1].trim().split(" ")).map(Long::parseLong).collect(Collectors.toList()));
            equations.add(equation);
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = checkEquations(copyNestedList(equations), false);
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = checkEquations(equations, true);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long checkEquations(List<List<Long>> equations, boolean useConcat) {
        long sum = 0;
        for (List<Long> equation : equations) {
            long desiredTotal = equation.remove(0);
            if (canGetDesiredTotal(desiredTotal, equation, useConcat)) {
                sum += desiredTotal;
            }
        }
        return sum;
    }

    private static boolean canGetDesiredTotal(long desiredTotal, List<Long> equation, boolean useConcat) {
        if (equation.isEmpty()) return false;
        if (equation.size() < 2) return equation.get(0) == desiredTotal;
        if (desiredTotal < equation.get(0)) return false;
        List<Long> addList = new ArrayList<>();
        List<Long> mulList = new ArrayList<>();
        List<Long> concatList = new ArrayList<>();
        addList.add(equation.get(0) + equation.get(1));
        mulList.add(equation.get(0) * equation.get(1));
        if (useConcat) {
            concatList.add(Long.parseLong(String.valueOf(equation.get(0)) + equation.get(1), 10));
            concatList.addAll(equation.subList(2, equation.size()));
        }
        addList.addAll(equation.subList(2, equation.size()));
        mulList.addAll(equation.subList(2, equation.size()));
        return canGetDesiredTotal(desiredTotal, addList, useConcat) || canGetDesiredTotal(desiredTotal, mulList, useConcat) || canGetDesiredTotal(desiredTotal, concatList, useConcat);
    }
}
