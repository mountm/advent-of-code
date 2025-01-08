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
            if (canGetDesiredTotal(desiredTotal, equation.get(0), equation.subList(1, equation.size()), useConcat)) {
                sum += desiredTotal;
            }
        }
        return sum;
    }

    private static boolean canGetDesiredTotal(long desiredTotal, long currentTotal, List<Long> remainingOperands, boolean useConcat) {
        if (remainingOperands.isEmpty()) return desiredTotal == currentTotal;
        if (desiredTotal < currentTotal) return false;
        return canGetDesiredTotal(desiredTotal, currentTotal + remainingOperands.get(0), remainingOperands.subList(1, remainingOperands.size()), useConcat)
                || canGetDesiredTotal(desiredTotal, currentTotal * remainingOperands.get(0), remainingOperands.subList(1, remainingOperands.size()), useConcat)
                || (useConcat && canGetDesiredTotal(desiredTotal, Long.parseLong(String.valueOf(currentTotal) + remainingOperands.get(0)), remainingOperands.subList(1, remainingOperands.size()), true));
    }
}
