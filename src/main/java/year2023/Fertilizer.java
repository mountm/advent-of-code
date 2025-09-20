package year2023;

import base.AoCDay;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Fertilizer extends AoCDay {

    // keys are a pair indicating the source range
    // values are a pair indicating the destination range
    Map<Pair<Long, Long>, Pair<Long, Long>> conversionMap = new HashMap<>();

    public void solve() {
        timeMarkers[0] = Instant.now().toEpochMilli();
        List<String> lines = readResourceFile(2023, 5, false, 0);
        List<Long> seeds = Arrays.stream(lines.get(0).split(": +")[1].split(" +")).map(Long::parseLong).toList();
        populateMap(lines);
        timeMarkers[1] = Instant.now().toEpochMilli();
        part1Answer = getMinLocation(seeds);
        timeMarkers[2] = Instant.now().toEpochMilli();
        Set<Pair<Long, Long>> seedRanges = generateSeedRanges(seeds);
        part2Answer = getMinLocationInRange(seedRanges);
        timeMarkers[3] = Instant.now().toEpochMilli();
    }

    private long getMinLocationInRange(Set<Pair<Long, Long>> seedRanges) {
        AtomicLong minLocation = new AtomicLong(Long.MAX_VALUE);
        AtomicLong minSeedNumber = new AtomicLong(Long.MAX_VALUE);
        for (Pair<Long, Long> seedRange : seedRanges) {
            conversionMap.entrySet().stream()
                    .filter(e -> e.getKey().getLeft() <= seedRange.getRight() - 1 && seedRange.getLeft() <= e.getKey().getRight() - 1)
                    .forEach( entry -> {
                        long delta = entry.getValue().getLeft() - entry.getKey().getLeft();
                        if (entry.getKey().getLeft() < seedRange.getLeft()) {
                            if (minLocation.get() > seedRange.getLeft() + delta) {
                                minLocation.set(seedRange.getLeft() + delta);
                                minSeedNumber.set(seedRange.getLeft());
                            }
                        } else if (minLocation.get() > entry.getKey().getLeft() + delta){
                            minLocation.set(entry.getKey().getLeft() + delta);
                            minSeedNumber.set(entry.getKey().getLeft());
                        }
                    });
        }

        System.out.println("Seed number " + minSeedNumber);
        return minLocation.get();
    }

    private Set<Pair<Long, Long>> generateSeedRanges(List<Long> seeds) {
        Set<Pair<Long, Long>> result = new HashSet<>();
        for (int i = 0; i < seeds.size() - 1; i+= 2) {
            result.add(Pair.of(seeds.get(i), seeds.get(i) + seeds.get(i+1)));
        }
        return result;
    }

    private long getMinLocation(List<Long> seeds) {
        return seeds.stream().mapToLong(this::findSeedLocation).min().orElse(Long.MAX_VALUE);
    }

    private long findSeedLocation(long seedNum) {
        Pair<Long, Long> sourceRange = conversionMap.keySet().stream().filter(k -> k.getLeft() <= seedNum && k.getRight() > seedNum).findAny().orElse(Pair.of(0L, 0L));
        long delta = 0;
        if (sourceRange.getLeft() <= seedNum && sourceRange.getRight() > seedNum) {
            delta = conversionMap.get(sourceRange).getLeft() - sourceRange.getLeft();
        }
        return seedNum + delta;
    }

    private void populateMap(List<String> lines) {
        updateMap(lines, "seed-to-soil map:");
        updateMap(lines, "soil-to-fertilizer map:");
        updateMap(lines, "fertilizer-to-water map:");
        updateMap(lines, "water-to-light map:");
        updateMap(lines, "light-to-temperature map:");
        updateMap(lines, "temperature-to-humidity map:");
        updateMap(lines, "humidity-to-location map:");
    }

    private void updateMap(List<String> lines, String checkingFor) {
        Map<Pair<Long, Long>, Pair<Long, Long>> nextLevelMap = new HashMap<>();
        int i = 0;
        while (!checkingFor.equals(lines.get(i))) {
            i++;
        }
        i++;
        while (i < lines.size() && !lines.get(i).isBlank()) {
            long sourceRangeStart = Long.parseLong(lines.get(i).split(" +")[1], 10);
            long destRangeStart = Long.parseLong(lines.get(i).split(" +")[0], 10);
            long rangeLength = Long.parseLong(lines.get(i).split(" +")[2], 10);
            long sourceRangeEnd = sourceRangeStart + rangeLength;
            long destRangeEnd = destRangeStart + rangeLength;
            nextLevelMap.put(Pair.of(sourceRangeStart, sourceRangeEnd), Pair.of(destRangeStart, destRangeEnd));
            i++;
        }
        reconcileMaps(nextLevelMap);
    }

    private void reconcileMaps(Map<Pair<Long, Long>, Pair<Long, Long>> nextLevelMap) {
        // conversionMap gets us from seeds to the current level
        // to prepare for the next level, we need to split source ranges before updating destinations
        nextLevelMap.keySet().forEach(newSourceRange -> {
            Set<Map.Entry<Pair<Long, Long>, Pair<Long, Long>>> overlappingEntries = conversionMap.entrySet().stream()
                    .filter(e -> e.getValue().getLeft() <= newSourceRange.getRight() - 1 && newSourceRange.getLeft() <= e.getValue().getRight() - 1)
                    .collect(Collectors.toSet());
            if (!overlappingEntries.isEmpty()) {
                for (Map.Entry<Pair<Long, Long>, Pair<Long, Long>> prevEntry : overlappingEntries) {
                    Pair<Long, Long> oldDestRange = prevEntry.getValue();
                    Pair<Long, Long> oldSourceRange = prevEntry.getKey();
                    // only split the old destination if it extends outside the bounds of the new source
                    if (oldDestRange.getLeft() < newSourceRange.getLeft() || oldDestRange.getRight() > newSourceRange.getRight()) {
                        conversionMap.remove(oldSourceRange);
                        long oldDelta = oldDestRange.getLeft() - oldSourceRange.getLeft();

                        /*
                        Old range: aaa---AAA
                        New range: bbb---BBB
                        Three possibilities:
                        1. old range ends in the middle of new range and starts lower
                            (aaa---bbb---AAA---BBB)
                            Split into two segments aaa---bbb and bbb---AAA
                            The range AAA---BBB will be split off as its own segment of the new range when we invert
                        2. old range starts in the middle of new range and ends higher
                            (bbb---aaa---BBB---AAA)
                            Split into two segments aaa---BBB and BBB---AAA
                        3. old range is strictly wider than new range
                            aaa---bbb---BBB---AAA
                            Split into three segments aaa---bbb, bbb---BBB, BBB---AAA
                         */

                        // case 3
                        if (oldDestRange.getLeft() < newSourceRange.getLeft() && oldDestRange.getRight() > newSourceRange.getRight()) {
                            conversionMap.put(Pair.of(oldSourceRange.getLeft(), newSourceRange.getLeft() - oldDelta), Pair.of(oldDestRange.getLeft(), newSourceRange.getLeft()));
                            conversionMap.put(Pair.of(newSourceRange.getLeft() - oldDelta, newSourceRange.getRight() - oldDelta), Pair.of(newSourceRange.getLeft(), newSourceRange.getRight()));
                            conversionMap.put(Pair.of(newSourceRange.getRight() - oldDelta, oldSourceRange.getRight()), Pair.of(newSourceRange.getRight(), oldDestRange.getRight()));
                        } else if (oldDestRange.getLeft() < newSourceRange.getLeft()) {
                            conversionMap.put(Pair.of(oldSourceRange.getLeft(), newSourceRange.getLeft() - oldDelta), Pair.of(oldDestRange.getLeft(), newSourceRange.getLeft()));
                            conversionMap.put(Pair.of(newSourceRange.getLeft() - oldDelta, oldSourceRange.getRight()), Pair.of(newSourceRange.getLeft(), oldDestRange.getRight()));
                        } else {
                            conversionMap.put(Pair.of(oldSourceRange.getLeft(), newSourceRange.getRight() - oldDelta), Pair.of(oldDestRange.getLeft(), newSourceRange.getRight()));
                            conversionMap.put(Pair.of(newSourceRange.getRight() - oldDelta, oldSourceRange.getRight()), Pair.of(newSourceRange.getRight(), oldDestRange.getRight()));
                        }
                    }
                }

            }
        });

        // repeat process but now split the new maps based on previous maps
        conversionMap.values().forEach(oldDestRange -> {
            Set<Map.Entry<Pair<Long, Long>, Pair<Long, Long>>> overlappingEntries = nextLevelMap.entrySet().stream()
                    .filter(e -> e.getKey().getLeft() <= oldDestRange.getRight() - 1 && oldDestRange.getLeft() <= e.getKey().getRight() - 1)
                    .collect(Collectors.toSet());
            if (!overlappingEntries.isEmpty()) {
                for (Map.Entry<Pair<Long, Long>, Pair<Long, Long>> newEntry : overlappingEntries) {
                    Pair<Long, Long> newDestRange = newEntry.getValue();
                    Pair<Long, Long> newSourceRange = newEntry.getKey();
                    // only split the new source if it extends outside the bounds of the old destination
                    if (newSourceRange.getLeft() < oldDestRange.getLeft() || newSourceRange.getRight() > oldDestRange.getRight()) {
                        nextLevelMap.remove(newSourceRange);
                        long newDelta = newDestRange.getLeft() - newSourceRange.getLeft();

                        /*
                        Old range: aaa---AAA
                        New range: bbb---BBB
                        Three possibilities:
                        1. new range ends at the top of old range and starts lower
                            (bbb---aaa---------BBB)
                            Split into two segments bbb---aaa and aaa---BBB
                        2. new range starts at the bottom of old range and ends higher
                            (bbb---------AAA---BBB)
                            Split into two segments bbb---AAA and AAA---BBB
                        3. new range is strictly wider than old range
                            bbb---aaa---AAA---BBB
                            Split into three segments bbb---aaa, aaa---AAA, AAA---BBB
                         */
                        if (newSourceRange.getLeft() < oldDestRange.getLeft() && newSourceRange.getRight() > oldDestRange.getRight()) {
                            nextLevelMap.put(Pair.of(newSourceRange.getLeft(), oldDestRange.getLeft()), Pair.of(newDestRange.getLeft(), oldDestRange.getLeft() + newDelta));
                            nextLevelMap.put(Pair.of(oldDestRange.getLeft(), oldDestRange.getRight()), Pair.of(oldDestRange.getLeft() + newDelta, oldDestRange.getRight() + newDelta));
                            nextLevelMap.put(Pair.of(oldDestRange.getRight(), newSourceRange.getRight()), Pair.of(oldDestRange.getRight() + newDelta, newDestRange.getRight()));
                        } else if (newSourceRange.getLeft() < oldDestRange.getLeft()) {
                            nextLevelMap.put(Pair.of(newSourceRange.getLeft(), oldDestRange.getLeft()), Pair.of(newDestRange.getLeft(), oldDestRange.getLeft() + newDelta));
                            nextLevelMap.put(Pair.of(oldDestRange.getLeft(), newSourceRange.getRight()), Pair.of(oldDestRange.getLeft() + newDelta, newDestRange.getRight()));
                        } else {
                            nextLevelMap.put(Pair.of(newSourceRange.getLeft(), oldDestRange.getRight()), Pair.of(newDestRange.getLeft(), oldDestRange.getRight() + newDelta));
                            nextLevelMap.put(Pair.of(oldDestRange.getRight(), newSourceRange.getRight()), Pair.of(oldDestRange.getRight() + newDelta, newDestRange.getRight()));
                        }
                    }
                }
            }
        });
        // update conversion map now that all ranges should match
        for (Map.Entry<Pair<Long, Long>, Pair<Long, Long>> entry : conversionMap.entrySet()) {
            if (nextLevelMap.containsKey(entry.getValue())) {
                entry.setValue(nextLevelMap.remove(entry.getValue()));
            }
        }
        conversionMap.putAll(nextLevelMap);
    }

}
