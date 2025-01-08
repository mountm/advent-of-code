package year2024;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
                        memory.add(-1);
                    }
                }
                isFile = !isFile;
            }
        }
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = moveFileBlocks(new ArrayList<>(memory));
        timeMarkers[2] = Instant.now().toEpochMilli();
        part2Answer = moveFiles(memory);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long moveFileBlocks(List<Integer> memory) {
        int idx = 0;
        while (idx < memory.size()) {
            if (memory.get(idx) == -1) {
                while (memory.get(memory.size() - 1) == -1) {
                    memory.remove(memory.size() - 1);
                }
                int val = memory.remove(memory.size() - 1);
                memory.set(idx, val);
            }
            idx++;
        }
        return getSum(memory);
    }

    private long getSum(List<Integer> memory) {
        long sum = 0;
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) != -1) {
                sum += memory.get(i) * i;
            }
        }
        return sum;
    }

    private long moveFiles(List<Integer> memory) {
        List<PriorityQueue<Integer>> emptyBlocks = findEmptyBlocks(memory);
        Map<Integer, Pair<Integer, Integer>> fileBlocks = findFileBlocks(memory);
        List<Integer> fileIndices = fileBlocks.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (int index : fileIndices) {
            moveFile(index, fileBlocks, emptyBlocks);
        }
        return getSum(fileBlocks);
    }

    private long getSum(Map<Integer, Pair<Integer, Integer>> fileBlocks) {
        AtomicLong sum = new AtomicLong();
        fileBlocks.forEach((key, val) -> {
            for (int i = 0; i < val.getRight(); i++) {
                sum.addAndGet((long) val.getLeft() * (key + i));
            }
        });
        return sum.get();
    }

    // key: start index
    // value: file ID, length
    private Map<Integer, Pair<Integer, Integer>> findFileBlocks(List<Integer> memory) {
        Map<Integer, Pair<Integer, Integer>> result = new HashMap<>();
        int currentId = 0;
        int currentIdStartIdx = 0;
        int i = 0;
        int count = 0;
        while (i < memory.size()) {
            if (memory.get(i) != -1) {
                if (memory.get(i) == currentId) count++;
                else {
                    if (count > 0) {
                        result.put(currentIdStartIdx, Pair.of(currentId, count));
                    }
                    if (memory.get(i) == ++currentId) {
                        currentIdStartIdx = i;
                        count = 1;
                    } else count = 0;
                }
            }
            i++;
        }
        if (count > 0) result.put(currentIdStartIdx, Pair.of(currentId, count));
        return result;
    }

    // list is ordered by the length of the empty space within
    // value: priority queue of starting indices
    private List<PriorityQueue<Integer>> findEmptyBlocks(List<Integer> memory) {
        List<PriorityQueue<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            result.add(new PriorityQueue<>());
        }
        boolean isPrevEmpty = false;
        int emptyCount = 0;
        int startIdx = 0;
        for (int i = 0; i < memory.size(); i++) {
            if (memory.get(i) == -1) {
                if (isPrevEmpty) emptyCount++;
                else {
                    isPrevEmpty = true;
                    startIdx = i;
                    emptyCount = 1;
                }
            } else {
                if (isPrevEmpty) {
                    result.get(emptyCount - 1).add(startIdx);
                    isPrevEmpty = false;
                    emptyCount = 0;
                }
            }
        }
        return result;
    }

    private void moveFile(int fileIdx, Map<Integer, Pair<Integer, Integer>> fileBlockSizes, List<PriorityQueue<Integer>> emptyBlocks) {
        Pair<Integer, Integer> fileData = fileBlockSizes.get(fileIdx);
        int heapIdx = -1;
        int newStartingIdx = Integer.MAX_VALUE;
        for (int i = fileData.getRight(); i <= 9; i++) {
            if (!emptyBlocks.get(i-1).isEmpty()) {
                int newIdx = emptyBlocks.get(i-1).poll();
                if (newIdx > fileIdx || newIdx > newStartingIdx) {
                    emptyBlocks.get(i-1).add(newIdx);
                    continue;
                }
                if (heapIdx != -1) {
                    emptyBlocks.get(heapIdx).add(newStartingIdx);
                }
                newStartingIdx = newIdx;
                heapIdx = i - 1;
            }
        }
        if (heapIdx >= 0) {
            fileBlockSizes.put(newStartingIdx, fileData);
            fileBlockSizes.remove(fileIdx);
            if (heapIdx > fileData.getRight() - 1) {
                emptyBlocks.get(heapIdx - fileData.getRight()).add(newStartingIdx + fileData.getRight());
            }
        }
    }
}
