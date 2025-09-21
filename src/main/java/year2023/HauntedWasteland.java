package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HauntedWasteland extends AoCDay {

    Map<String, Pair<String, String>> network = new HashMap<>();
    Pattern networkLine = Pattern.compile("(\\S{3}) = \\((\\S{3}), (\\S{3})\\)");

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 8, false, 0);
        timeMarkers[1] = Instant.now().toEpochMilli();
        buildNetwork(lines.subList(2, lines.size()));
        part1Answer = countSteps(lines.get(0));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = doChineseRemainderStuff(lines.get(0));
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long doChineseRemainderStuff(String instructions) {
        Set<String> startingPositions = network.keySet().stream().filter(key -> key.endsWith("A")).collect(Collectors.toSet());
        List<Pair<Long, Long>> remainders = new LinkedList<>();
        for (String startPos : startingPositions) {
            int i = 0;
            long stepCount = 0L;
            long remainder, modulus;
            String currentPos = startPos;
            while (!currentPos.endsWith("Z")) {
                if (i == instructions.length()) i = 0;
                currentPos = instructions.charAt(i++) == 'L' ? network.get(currentPos).getLeft() : network.get(currentPos).getRight();
                stepCount++;
            }
            remainder = stepCount;
            if (i == instructions.length()) i = 0;
            currentPos = instructions.charAt(i++) == 'L' ? network.get(currentPos).getLeft() : network.get(currentPos).getRight();
            stepCount++;
            while (!currentPos.endsWith("Z")) {
                if (i == instructions.length()) i = 0;
                currentPos = instructions.charAt(i++) == 'L' ? network.get(currentPos).getLeft() : network.get(currentPos).getRight();
                stepCount++;
            }
            modulus = stepCount - remainder;
            remainders.add(Pair.of(remainder % modulus, modulus));
        }
        BigInteger result = BigInteger.ONE;
        for (Pair<Long, Long> remainder : remainders) {
            BigInteger nextVal = BigInteger.valueOf(remainder.getRight());
            BigInteger gcd = result.gcd(nextVal);
            result = result.multiply(nextVal).abs().divide(gcd);
        }
        return result.longValue();
    }

    private long countSteps(String instructions) {
        int i = 0;
        long stepCount = 0L;
        String currentPos = "AAA";
        while(!currentPos.equals("ZZZ")) {
            if (i == instructions.length()) i = 0;
            currentPos = instructions.charAt(i++) == 'L' ? network.get(currentPos).getLeft() : network.get(currentPos).getRight();
            stepCount++;
        }
        return stepCount;
    }

    private void buildNetwork(List<String> lines) {
        for (String line : lines) {
            Matcher m = networkLine.matcher(line);
            m.find();
            network.put(m.group(1), Pair.of(m.group(2), m.group(3)));
        }
    }
}
