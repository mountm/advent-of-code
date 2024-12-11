package Day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11Runner {
    private static final int BLINK_COUNT = 75;
    public static void main(String[] args) {
        BufferedReader input = null;
        String currentLine;
        Map<Long, Long> stoneCount = new HashMap<>();
        try {
            input = new BufferedReader(new FileReader("inputs/stones.txt"));
            while ((currentLine = input.readLine()) != null) {
                List<Long> stoneList = Arrays.stream(currentLine.split(" ")).map(Long::parseLong).collect(Collectors.toList());
                for (Long val : new HashSet<>(stoneList)) {
                    stoneCount.put(val, stoneList.stream().filter((e -> e == val)).count());
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

        for (int i = 0; i < BLINK_COUNT; i++) {
            Map<Long, Long> newStoneCount = new HashMap<>();
            System.out.println("blink count: " + i + "\nlist size: " + getSize(stoneCount));
            for (Map.Entry<Long, Long> entry: stoneCount.entrySet()) {
                long key = entry.getKey();
                if (key == 0L) {
                    newStoneCount.merge(1L, entry.getValue(), Long::sum);
                } else if (String.valueOf(key).length() % 2 == 0) {
                    long splitPoint = 1;
                    for (int j = 0; j < String.valueOf(key).length() / 2; j++) {
                        splitPoint *= 10;
                    }
                    newStoneCount.merge(key / splitPoint, entry.getValue(), Long::sum);
                    newStoneCount.merge(key % splitPoint, entry.getValue(), Long::sum);
                } else {
                    newStoneCount.merge(key * 2024, entry.getValue(), Long::sum);
                }
                stoneCount = newStoneCount;
            }
        }
        System.out.println("blink count: " + BLINK_COUNT + "\nlist size: " + getSize(stoneCount));
    }

    private static long getSize(Map<Long, Long> stoneCount) {
        return stoneCount.values().stream().reduce(0L, Long::sum);
    }
}
