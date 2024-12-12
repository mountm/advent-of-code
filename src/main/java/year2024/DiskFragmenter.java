package year2024;

import base.AoCDay;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class DiskFragmenter extends AoCDay {

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2024, 9, false, 0);
        List<Integer> memory = new ArrayList<>();
        boolean isFile = true;
        int fileId = 0;
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
        part2Answer = part2(memory);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long part1(List<Integer> memory) {
        int firstNullIdx = memory.indexOf(null);
        while (firstNullIdx >= 0) {
            while (memory.get(memory.size() - 1) == null) {
                memory.remove(memory.size() - 1);
            }
            int val = memory.remove(memory.size() - 1);
            memory.set(firstNullIdx, val);
            firstNullIdx = memory.indexOf(null);
        }
        return getSum(memory);
    }

    private long getSum(List<Integer> memory) {
        long sum = 0;
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) != null) {
                sum += memory.get(i) * i;
            }
        }
        return sum;
    }

    private long part2(List<Integer> memory) {
        Map<Integer, Integer> nullBlocks = findNullBlocks(memory);
        Map<Integer, Integer> fileBlockSizes = findFileBlockSizes(memory);
        int fileId = fileBlockSizes.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        while(fileId > 0) {
            moveFile(memory, fileId, fileBlockSizes, nullBlocks);
            fileId--;
        }
        return getSum(memory);
    }

    // key: file ID
    // value: length
    private Map<Integer, Integer> findFileBlockSizes(List<Integer> memory) {
        Map<Integer, Integer> result = new HashMap<>();
        int currentId = 0;
        int i = 0;
        int count = 0;
        while (i < memory.size()) {
            if (memory.get(i) != null) {
                if (memory.get(i) == currentId) count++;
                else {
                    if (count > 0) {
                        result.put(currentId, count);
                    }
                    if (memory.get(i) == ++currentId) {
                        count = 1;
                    } else count = 0;
                }
            }
            i++;
        }
        if (count > 0) result.put(currentId, count);
        return result;
    }

    // key: start index
    // value: length
    private Map<Integer, Integer> findNullBlocks(List<Integer> memory) {
        Map<Integer, Integer> result = new HashMap<>();
        boolean isPrevNull = false;
        int nullCount = 0;
        int startIdx = 0;
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) == null) {
                if (isPrevNull) nullCount++;
                else {
                    isPrevNull = true;
                    startIdx = i;
                    nullCount = 1;
                }
            } else {
                if (isPrevNull) {
                    result.put(startIdx, nullCount);
                    isPrevNull = false;
                    nullCount = 0;
                }
            }
        }
        return result;
    }

    private void moveFile(List<Integer> memory, int fileId, Map<Integer, Integer> fileBlockSizes, Map<Integer, Integer> nullBlocks) {
        int firstFileIdx = memory.indexOf(fileId);
        int fileLength = fileBlockSizes.get(fileId);
        Map.Entry<Integer, Integer> firstAvailableOpenSpot = nullBlocks.entrySet().stream().filter(e -> e.getKey() < firstFileIdx && e.getValue() >= fileLength).min(Map.Entry.comparingByKey()).orElse(null);
        if (firstAvailableOpenSpot != null) {
            int newStartIdx = firstAvailableOpenSpot.getKey();
            for (int i = 0; i < fileLength; i++) {
                memory.set(newStartIdx + i, fileId);
                memory.set(firstFileIdx + i, null);
            }
            fixNullBlocks(memory, firstFileIdx, fileLength, firstAvailableOpenSpot, nullBlocks);
        }
    }

    private void fixNullBlocks(List<Integer> memory, int firstFileIdx, int fileLength, Map.Entry<Integer, Integer> insertionPoint, Map<Integer, Integer> nullBlocks) {
        // first, remove the entry for the overwritten block
        if (insertionPoint.getValue() > fileLength) {
            nullBlocks.put(insertionPoint.getKey() + fileLength, insertionPoint.getValue() - fileLength);
        }
        nullBlocks.remove(insertionPoint.getKey());
        // then, update the higher entries to account for the nulls that have been shifted there.
        int extraNulls = Optional.ofNullable(nullBlocks.remove(firstFileIdx + fileLength)).orElse(0);
        if (memory.get(firstFileIdx - 1) == null) { // must increase length of prior block
            Map.Entry<Integer, Integer> prevNullBlock = nullBlocks.entrySet().stream().filter(e -> e.getKey() < firstFileIdx).max(Map.Entry.comparingByKey()).orElse(null);
            assert prevNullBlock != null;
            prevNullBlock.setValue(prevNullBlock.getValue() + fileLength + extraNulls);
        } else {
            nullBlocks.put(firstFileIdx, fileLength + extraNulls);
        }
    }
}
