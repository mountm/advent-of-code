package year2024;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DiskFragmenter extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 9, false, 0);
        List<Long> memory = new ArrayList<>();
        boolean isFile = true;
        long fileId = 0;
        for (String line : lines) {
            for (char val : line.toCharArray()) {
                long count = Long.parseLong(String.valueOf(val), 10);
                if (isFile) {
                    for (int i = 0; i < count; i++) {
                        memory.add(fileId);
                    }
                    fileId++;
                } else {
                    for (int i = 0; i < count; i++) {
                        memory.add(null);
                    }
                }
                isFile = !isFile;
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = part1(new ArrayList<>(memory));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = part2(memory, --fileId);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long part1(List<Long> memory) {
        int firstNullIdx = memory.indexOf(null);
        while (firstNullIdx >= 0) {
            while (memory.get(memory.size() - 1) == null) {
                memory.remove(memory.size() - 1);
            }
            long val = memory.remove(memory.size() - 1);
            memory.set(firstNullIdx, val);
            firstNullIdx = memory.indexOf(null);
        }
        return getSum(memory);
    }

    private long getSum(List<Long> memory) {
        long sum = 0;
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) != null) {
                sum += memory.get(i) * i;
            }
        }
        return sum;
    }

    private long part2(List<Long> memory, long fileId) {
        int firstNullIdx = memory.indexOf(null);
        int firstFileIdx = memory.indexOf(fileId);
        while(fileId > 0) {
            System.out.println("Checking file id " + fileId);
            moveFile(memory, firstNullIdx, firstFileIdx);
            fileId--;
            firstNullIdx = memory.indexOf(null);
            firstFileIdx = memory.indexOf(fileId);
        }
        return getSum(memory);
    }

    private void moveFile(List<Long> memory, int nullIdx, int firstFileIdx) {
        if (nullIdx > firstFileIdx) return;
        long fileVal = memory.get(firstFileIdx);
        long count = memory.stream().filter(e -> e != null && e == fileVal).count();
        boolean doesFitHere = true;
        int i = 1;
        while (i < count) {
            if (memory.get(nullIdx + i) != null) {
                doesFitHere = false;
                break;
            }
            i++;
        }
        if (doesFitHere) {
            for (i = 0; i < count; i++) {
                memory.set(nullIdx + i, fileVal);
                memory.set(firstFileIdx + i, null);
            }
            return;
        }
        int nextNullBlockIdx = memory.subList(nullIdx + i, memory.size()).indexOf(null) + nullIdx + i;
        if (nextNullBlockIdx < firstFileIdx) {
            moveFile(memory, nextNullBlockIdx, firstFileIdx);
        }
    }
}
